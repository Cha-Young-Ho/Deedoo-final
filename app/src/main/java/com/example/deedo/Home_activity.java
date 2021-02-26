package com.example.deedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Home_activity extends AppCompatActivity {
    private TextView main_id, main_pass;
    ImageButton map_button, plan_button, inquiry_button, friend_setting_button;
    String userId;

    /*
    액션바에 돋보기 추가
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*
   액션바에 돋보기 이벤트 추가
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_search_btn) {

            Intent intent = new Intent(Home_activity.this, Search_Somebody.class);
            intent.putExtra("id", userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //여기부터는 id값 넘어오는 지 확인
        Intent intent = getIntent();
        userId = intent.getStringExtra("id");

        //여기까지

        userId = intent.getStringExtra("id"); // 로그인한 id값 (primary key)
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
                Log.v("홈에서 id = ", "" + userId);
                Intent intent = new Intent(Home_activity.this, Inquiry_Lotate.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });

        // 계획세우기 버튼 클릭 시
        plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, Plan.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });

        // 내 일과 확인 버튼 클릭 시
        inquiry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, Inquiry.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });

        // 친구 관리 버튼 클릭 시
        friend_setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, Modify_Friend.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });


    }
}