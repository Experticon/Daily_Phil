package com.example.dailyphill;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    // [END auth_fui_create_launcher]

    private DatabaseReference mDataBase;
    private final String USER_KEY = "User";

    private String current_user = "";

    private ArrayList<User_day> days_list;
    private static RecyclerView recyclerView;
    private static RecyclerAdapter recyclerAdapter;

    Button add_btn;
    ImageButton delete_btn;

    private String year;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        themeAndLogo();

        recyclerView = findViewById(R.id.recyclerview);

        add_btn = findViewById(R.id.add_btn);
        delete_btn = findViewById(R.id.delete_button);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        days_list = new ArrayList<>();

        recyclerAdapter = new RecyclerAdapter(days_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        Date currentDate = new Date();
        DateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        year = yearFormat.format(currentDate);
        date = dateFormat.format(currentDate);

        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!check_date_delete(position)) {
                    FragmentManager manager = getSupportFragmentManager();
                    MyDialogFragment myDialogFragment = new MyDialogFragment();
                    myDialogFragment.show(manager, "myDialog");
                    myDialogFragment.setOnDialogClickListener(new MyDialogFragment.OnButtonClick() {
                        @Override
                        public void onDialogClickListener() {
                            String key = days_list.get(position).getKey();
                            days_list.remove(position);
                            mDataBase.child(current_user).child(year).child("Day").child(key).removeValue();
                            reload_list();
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            @Override
            public void onClick(int position) {
                String key = days_list.get(position).getKey();
                Intent intent = new Intent(MainActivity.this, Watch_Note.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("edit_day", key);
                intent.putExtra("current_user", current_user);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check_date_add()) {
                    Intent intent = new Intent(MainActivity.this, Add_NewNote.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("current_user", current_user);
                    startActivity(intent);
                }
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
                            days_list.add(new User_day(n.getId(), key, n.getDay(), n.getShowText()));
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
    boolean check_date_add() {
            if (!days_list.isEmpty() && Objects.equals(days_list.get(days_list.size() - 1).getDay(), date))
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Возвращайтесь завтра!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        return false;
    }
    boolean check_date_delete(int position) {
        if (Objects.equals(days_list.get(position).getDay(), date))
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Возвращайтесь завтра!", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return false;
    }
    public void themeAndLogo() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.my_great_logo)      // Set logo drawable
                .setTheme(R.style.MySuperAppTheme)      // Set theme
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_theme_logo]
    }
    // [START auth_fui_result]
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            current_user = uid;
        } else {
            response.getError().getErrorCode(); //Если вход отменён, но это пока что
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
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