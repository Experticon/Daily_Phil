package com.example.dailyphill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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
    private String cur_image;
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
        imageView = findViewById(R.id.this_image);

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
                dayCount = new DayCount(id, full_date, text, textView.getText().toString(), cur_image);
                mDataBase.child(user).child(year).child("Day").push().setValue(dayCount);

                mDataBase = FirebaseDatabase.getInstance().getReference(DAY_KEY);
                mDataBase.child(current_key).child("text").setValue("");

                finish();
                ////////Для вставки новых пустых элементов
                /*for (int i = 1; i < 31; i ++) {
                    String id = mDataBase.getKey();
                    DayCount dayCount = new DayCount(id, "", "", "", "");
                    mDataBase.push().setValue(dayCount);
                }
                finish();
                */
                //////////

            }
        });
        imageView.setOnClickListener(v -> {
            if (!isImageScaled) v.animate().scaleX(1.4f).scaleY(1.4f).setDuration(500);
            if (isImageScaled) v.animate().scaleX(1f).scaleY(1f).setDuration(500);
            isImageScaled = !isImageScaled;
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
                            cur_image = n.getImage();
                            //imageView.setImageBitmap(mIcon_val);
                            //imageView.setImageResource(R.drawable.);
                            put_image(n.getImage());
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
    void put_image (String url) {
        Glide.with(this)
                .load(url)
                .into(imageView);
    }
}
