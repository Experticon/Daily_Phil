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

public class Add_NewNote extends AppCompatActivity {

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
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String date = dateFormat.format(currentDate);
                mDataBase.child(current_key).child("text").setValue(text);
                mDataBase.child(current_key).child("day").setValue(date);
                finish();
                ////////Для вставки новых пустых элементов
                /*
                String id = mDataBase.getKey();
                DayCount dayCount = new DayCount(id, "", "", "", "");
                mDataBase.push().setValue(dayCount);
                 */
                //////////
            }
        });
    }

    private void getDataFromDB() {
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DayCount n = ds.getValue(DayCount.class);
                    if ((n.getDay().isEmpty())) {
                        String key = ds.getKey();
                        current_key = key;
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
