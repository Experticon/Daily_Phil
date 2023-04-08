package com.example.dailyphill;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Watch_Note extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private final String USER_KEY = "User";

    TextView textView;
    TextView textView1;
    Button back_btn;

    private String current_key;
    private String current_user;
    private String year;
    private String cur_image;

    ImageView imageView;
    private boolean isImageScaled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        Bundle arguments = getIntent().getExtras();
        current_key = arguments.get("edit_day").toString();
        year = arguments.get("year").toString();
        current_user = arguments.get("current_user").toString();

        back_btn = findViewById(R.id.back_btn);
        textView = findViewById(R.id.question);
        textView1 = findViewById(R.id.your_note);
        imageView = findViewById(R.id.this_image);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        getDataFromDB();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView.setOnClickListener(v -> {
            if (!isImageScaled) v.animate().scaleX(1.4f).scaleY(1.4f).setDuration(500);
            if (isImageScaled) v.animate().scaleX(1f).scaleY(1f).setDuration(500);
            isImageScaled = !isImageScaled;
        });
    }

    private void getDataFromDB() {
        mDataBase.child(current_user).child(year).child("Day").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DayCount n = ds.getValue(DayCount.class);
                    String text = n.getText();
                    String key = ds.getKey();
                    if (Objects.equals(key, current_key))
                    {
                        textView1.setText(text);
                        textView.setText(n.getShowText());
                        cur_image = n.getImage();
                        put_image(cur_image);
                        break;
                    }
                }
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
}

