package com.example.dailyphill;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Watch_Under extends AppCompatActivity {
    TextView textView;
    Button back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_under);

        back_btn = findViewById(R.id.back_btn);
        textView = findViewById(R.id.text);

        Bundle arguments = getIntent().getExtras();
        textView.setText(arguments.get("text").toString());

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
