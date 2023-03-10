package com.example.dailyphill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private String DAY_KEY = "Day";


    private ArrayList<Day_Name> days_list;
    private static RecyclerView recyclerView;
    private static RecyclerAdapter recyclerAdapter;

    Button add_btn;
    ImageButton delete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        add_btn = findViewById(R.id.add_btn);
        delete_btn = findViewById(R.id.delete_button);

        mDataBase = FirebaseDatabase.getInstance().getReference(DAY_KEY);

        days_list = new ArrayList<>();

        recyclerAdapter = new RecyclerAdapter(days_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);



        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FragmentManager manager = getSupportFragmentManager();
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                myDialogFragment.show(manager, "myDialog");
                myDialogFragment.setOnDialogClickListener(new MyDialogFragment.OnButtonClick() {
                    @Override
                    public void onDialogClickListener() {
                        String key = days_list.get(position).getKey();
                        days_list.remove(position);
                        mDataBase.child(key).child("text").setValue("");
                        mDataBase.child(key).child("day").setValue("");
                        reload_list();
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onClick(int position) {
                String key = days_list.get(position).getKey();
                Intent intent = new Intent(MainActivity.this, Edit_Note.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("edit_day", key);
                startActivity(intent);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_NewNote.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
    private void getDataFromDB() {
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        DayCount n = ds.getValue(DayCount.class);
                        if (!(n.getDay().isEmpty())) {
                            String key = ds.getKey();
                            days_list.add(new Day_Name(n.getId(), key, n.getDay(), n.getShowText()));
                        }
                    }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void ReloadAdapter()
    {
        recyclerView.setAdapter(null);
        recyclerAdapter = new RecyclerAdapter(days_list);
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
        days_list.clear();
        getDataFromDB();
        ReloadAdapter();
    }
}