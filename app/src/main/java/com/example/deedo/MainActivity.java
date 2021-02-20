package com.example.deedo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView main_id, main_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        main_id = findViewById(R.id.main_id);
        main_pass = findViewById(R.id.main_pass);

        Intent intent = getIntent();

        String userId = intent.getStringExtra("main_id");
        String userPass = intent.getStringExtra("main_pass");

        main_id.setText(userId);
        main_id.setText(userPass);

        ImageButton imageButton;
        TextView textView;
        imageButton = findViewById(R.id.map_Button);
        textView = findViewById(R.id.map_text);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("바꼈음");
            }
        });
    }
}