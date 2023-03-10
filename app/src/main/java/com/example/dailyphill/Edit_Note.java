package com.example.dailyphill;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Edit_Note extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private String DAY_KEY = "Day";

    TextView textView;
    EditText editText;
    Button back_btn;
    ImageButton save_btn;

    private String current_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);

        Bundle arguments = getIntent().getExtras();
        current_key = arguments.get("edit_day").toString();

        save_btn = findViewById(R.id.save_btn);
        save_btn.setImageResource(R.drawable.save_plate);
        back_btn = findViewById(R.id.back_btn);
        textView = findViewById(R.id.question);
        editText = findViewById(R.id.your_note);

        mDataBase = FirebaseDatabase.getInstance().getReference(DAY_KEY);
        getDataFromDB();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                mDataBase.child(current_key).child("text").setValue(text);
                finish();
            }
        });
    }

    private void getDataFromDB() {
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DayCount n = ds.getValue(DayCount.class);
                    String key = ds.getKey();
                    if (Objects.equals(key, current_key))
                    {
                        editText.setText(n.getText());
                        textView.setText(n.getShowText());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

