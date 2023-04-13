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

public class Add_New_Under_Note extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private final String USER_KEY = "User";
    private String year;

    TextView textView;
    EditText editText;

    Button back_btn;
    ImageButton save_btn;

    ImageView imageView;
    private boolean isImageScaled = false;

    private String current_key;
    private String user;
    private DayCount dayCount;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);

        save_btn = findViewById(R.id.save_btn);
        save_btn.setImageResource(R.drawable.save_plate);
        back_btn = findViewById(R.id.back_btn);
        textView = findViewById(R.id.question);
        editText = findViewById(R.id.your_note);
        imageView = findViewById(R.id.this_image);

        Bundle arguments = getIntent().getExtras();
        user = arguments.get("current_user").toString();
        year = arguments.get("year").toString();
        current_key = arguments.get("cur_key").toString();
        textView.setText(arguments.get("showText").toString());
        put_image(arguments.get("image_url").toString());

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        date = dateFormat.format(currentDate);




        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
                String id = mDataBase.getKey();
                String text = editText.getText().toString();
                Under_note under_note = new Under_note(id, date,  text);
                mDataBase.child(user).child(year).child("Day").child(current_key).child("text").push().setValue(under_note);

                finish();
            }
        });
        imageView.setOnClickListener(v -> {
            if (!isImageScaled) v.animate().scaleX(1.4f).scaleY(1.4f).setDuration(500);
            if (isImageScaled) v.animate().scaleX(1f).scaleY(1f).setDuration(500);
            isImageScaled = !isImageScaled;
        });
    }
    void put_image (String url) {
        Glide.with(this)
                .load(url)
                .into(imageView);
    }
}
