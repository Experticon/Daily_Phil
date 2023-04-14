package com.example.dailyphill;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    View view_name;
    View v;
    RelativeLayout lLayout;

    private String current_key;
    private String user;
    private String cur_image;
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

        lLayout = findViewById(R.id.put_image);
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.image, lLayout, false);
        view_name=v.findViewById(R.id.image_inflate);

        Bundle arguments = getIntent().getExtras();
        user = arguments.get("current_user").toString();
        year = arguments.get("year").toString();
        current_key = arguments.get("cur_key").toString();
        textView.setText(arguments.get("showText").toString());
        cur_image = arguments.get("image_url").toString();
        put_image(cur_image);


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
        view_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_New_Under_Note.this, Watch_Image.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("image_url", cur_image);
                startActivity(intent);
            }
        });
    }
    void put_image (String url) {
        Glide.with(this)
                .load(url)
                .into((ImageView) view_name);
        lLayout.addView(v);
    }
    public void onResume(){
        super.onResume();
        lLayout.removeView(v);
    }
}
