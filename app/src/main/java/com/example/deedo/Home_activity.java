package com.example.deedo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Home_activity extends AppCompatActivity {
    private TextView main_id, main_pass;
    ImageButton map_button, plan_button, inquiry_button, friend_setting_button;
    String login_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //여기부터는 id값 넘어오는 지 확인
        Intent intent = getIntent();
        String userId = intent.getStringExtra("id");

        //여기까지

        login_id = intent.getStringExtra("id"); // 로그인한 id값 (primary key)
        map_button = findViewById(R.id.map_Button);
        plan_button = findViewById(R.id.plan_Button);
        inquiry_button = findViewById(R.id.inquiry_Button);
        friend_setting_button = findViewById(R.id.friend_setting_Button);
        main_id = findViewById(R.id.main_id);
        main_pass = findViewById(R.id.main_pass);
        
        
        
        //구역정하기 버튼 클릭 시
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, Create_Lotate.class);
                intent.putExtra("id", login_id);
                startActivity(intent);
            }
        });
        
        // 계획세우기 버튼 클릭 시
        plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, Plan.class);
                intent.putExtra("id", login_id);
                startActivity(intent);
            }
        });
        
        // 내 일과 확인 버튼 클릭 시
        inquiry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, Inquiry.class);
                intent.putExtra("id", login_id);
                startActivity(intent);
            }
        });
        
        // 친구 관리 버튼 클릭 시
        friend_setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, Modify_Friend.class);
                intent.putExtra("id", login_id);
                startActivity(intent);
            }
        });




    }
}