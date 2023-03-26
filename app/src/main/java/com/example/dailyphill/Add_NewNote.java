package com.example.dailyphill;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
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

public class Add_NewNote extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private final String DAY_KEY = "Day";
    private final String USER_KEY = "User";
    private String year;

    TextView textView;
    EditText editText;

    Button back_btn;
    ImageButton save_btn;

    private String current_key;
    private String user;
    private DayCount dayCount;
    private String date;
    private String full_date;
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

        Bundle arguments = getIntent().getExtras();
        user = arguments.get("current_user").toString();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        DateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        year = yearFormat.format(currentDate);
        date = dateFormat.format(currentDate);
        full_date = date + "." + year;

        getDataFromDB();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBase.child(current_key).child("text").setValue(editText.getText().toString());
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
                String id = mDataBase.getKey();

                String text = editText.getText().toString();
                dayCount = new DayCount(id, full_date, text, textView.getText().toString());
                mDataBase.child(user).child(year).child("Day").push().setValue(dayCount);

                mDataBase = FirebaseDatabase.getInstance().getReference(DAY_KEY);
                mDataBase.child(current_key).child("text").setValue("");

                finish();
                ////////Для вставки новых пустых элементов
                /*
                String id = mDataBase.getKey();
                DayCount dayCount = new DayCount(id, "", "", "");
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
                        String key = ds.getKey();
                        if (Objects.equals(date, n.getDay())) {
                            current_key = key;
                            textView.setText(n.getShowText());
                            editText.setText(n.getText());
                            break;
                        }
                        mDataBase.child(key).child("text").setValue("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
    }
}
