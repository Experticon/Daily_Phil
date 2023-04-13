package com.example.dailyphill;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Watch_Note extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private final String USER_KEY = "User";

    private ArrayList<Under_note> notes_list;
    private static RecyclerView recyclerView;
    private static under_note_adapter recyclerAdapter;

    TextView textView;
    Button back_btn;
    Button add_btn;

    private String current_key;
    private String current_user;
    private String year;
    private String cur_image;

    ImageView imageView;
    private boolean isImageScaled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_note);

        recyclerView = findViewById(R.id.recycler_unnote_view);

        Bundle arguments = getIntent().getExtras();
        current_key = arguments.get("edit_day").toString();
        year = arguments.get("year").toString();
        current_user = arguments.get("current_user").toString();

        notes_list = new ArrayList<>();

        add_btn = findViewById(R.id.add_unnote_btn);
        back_btn = findViewById(R.id.back_btn);
        textView = findViewById(R.id.question);
        imageView = findViewById(R.id.this_image);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        recyclerAdapter = new under_note_adapter(notes_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(Watch_Note.this, Add_New_Under_Note.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("current_user", current_user);
                    intent.putExtra("year", year);
                    intent.putExtra("cur_key", current_key);
                    intent.putExtra("showText", textView.getText().toString());
                    intent.putExtra("image_url", cur_image);
                    startActivity(intent);
            }
        });
        imageView.setOnClickListener(v -> {
            if (!isImageScaled) v.animate().scaleX(1.4f).scaleY(1.4f).setDuration(500);
            if (isImageScaled) v.animate().scaleX(1f).scaleY(1f).setDuration(500);
            isImageScaled = !isImageScaled;
        });
        recyclerAdapter.setOnItemClickListener(new under_note_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String text = notes_list.get(position).getText();
                Intent intent = new Intent(Watch_Note.this, Watch_Under.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("text", text);
                startActivity(intent);
            }
        });
    }

    private void getDataFromDB() {
        mDataBase.child(current_user).child(year).child("Day").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DayCount n = ds.getValue(DayCount.class);
                    String key = ds.getKey();
                    if (Objects.equals(current_key, key)) {
                        //String text = n.getText();
                        //textView1.setText(text);
                        textView.setText(n.getShowText());
                        cur_image = n.getImage();
                        put_image(cur_image);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mDataBase.child(current_user).child(year).child("Day").child(current_key).child("text").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Under_note n = ds.getValue(Under_note.class);
                    notes_list.add(new Under_note(n.getId(), n.getDay(), n.getText()));
                }
                recyclerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void put_image (String url) {
        Glide.with(this)
                .load(url)
                .into(imageView);
    }
    private void ReloadAdapter()
    {
        recyclerView.setAdapter(null);
        recyclerAdapter = new under_note_adapter(notes_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }
    public void onResume(){
        super.onResume();
        reload_list();
    }
    void reload_list()
    {
        notes_list.clear();
        getDataFromDB();
        ReloadAdapter();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem search = menu.findItem(R.id.search_View); // Подключаем SearchView
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}

