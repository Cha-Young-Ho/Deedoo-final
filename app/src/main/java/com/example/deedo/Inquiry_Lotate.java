package com.example.deedo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Inquiry_Lotate extends AppCompatActivity {

    Button create_lotate_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_lotate);

        create_lotate_btn = findViewById(R.id.create_lotate_btn);

        create_lotate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inquiry_Lotate.this, Create_Lotate.class);

                startActivity(intent);
            }
        });

    }


}