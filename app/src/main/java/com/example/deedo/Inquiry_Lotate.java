package com.example.deedo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Inquiry_Lotate extends AppCompatActivity {
    ArrayList<String> list;
    Button create_lotate_btn;
    String userId;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_lotate);

        db = new DBHelper(this);
        list = new ArrayList<>();
        list.add("아이템 1");
        list.add("아이템 2");
        list.add("아이템 3");
        list.add("아이템 4");

        Adapter adapter = new Adapter(list);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_lotate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        create_lotate_btn = findViewById(R.id.create_lotate_btn);
        userId = getIntent().getStringExtra("id");
        create_lotate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inquiry_Lotate.this, Create_Lotate.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });

    }


}