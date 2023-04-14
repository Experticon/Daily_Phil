package com.example.dailyphill;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Watch_Image extends AppCompatActivity {
    ImageView imageview;
    Button back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_image);

        back_btn = findViewById(R.id.back_btn);
        imageview = findViewById(R.id.watch_image);

        Bundle arguments = getIntent().getExtras();
        String url = arguments.get("image_url").toString();
        Glide.with(this)
                .load(url)
                .into(imageview);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
