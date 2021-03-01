package com.example.deedo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Activity_plan_details extends AppCompatActivity {

    Button plan_details_add_btn, plan_details_cancel_btn;
    ArrayList<Plan_details_Data> Plan_details_Data_list; //담아온 데이터
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    DBHelper db;
    String userId; //로그인된 유저 아이디
    String[] DATE; // Plan 액티비티에서 넘어온 날짜 배열

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

            Intent intent = new Intent(Activity_plan_details.this, Search_Somebody.class);
            intent.putExtra("id", userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        userId = getIntent().getStringExtra("id");

        this.DATE = getIntent().getStringArrayExtra("DATE");



        recyclerView = findViewById(R.id.plan_details_recyclerview); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Plan_details_Data_list != null) {
            Plan_details_Data_list.clear();
            Plan_details_Data_list = new ArrayList<>();
        } else {
            Plan_details_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }


        First_InitializeData();  //첫 리사이클 뷰는 검색어가 입력되지 않아서, First_InitializeData 메서드 호출 - 리스트에 데이터 담기
        Log.v("resume", "여기 실행됨2");
        adapter = new Plan_details_recyclerview_Adapter(Plan_details_Data_list, this, userId, DATE);
        recyclerView.setAdapter(adapter); // 리사이클러뷰 연결
        
        /*
        일정 생성 버튼 및 클릭 이벤트
         */
        plan_details_add_btn = findViewById(R.id.plan_details_add_btn);
        plan_details_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Plan_details_create dialog_plan_details_create =
                        new Dialog_Plan_details_create(Activity_plan_details.this, userId, DATE);
            }
        });

        plan_details_cancel_btn = findViewById(R.id.plan_details_cancel_btn);
        plan_details_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_plan_details.this.finish();
            }
        });
        
        

    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_plan_details);

        userId = getIntent().getStringExtra("id");


        InitializeData(userId);


    }


    public void First_InitializeData() {


    }

    public void InitializeData(String userId) {

        //DB에서 데이터리스트에 담아야하는 메서드
        Plan_details_Data_list.clear();

        recyclerView = findViewById(R.id.plan_details_recyclerview); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Plan_details_Data_list != null) {
            Plan_details_Data_list.clear();
            Plan_details_Data_list = new ArrayList<>();
        } else {
            Plan_details_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }
        db = new DBHelper(this);


        Plan_details_Data_list = db.get_plan_details_info(DATE, userId);//리스트에 데이터 담기


        Log.v("resume", "여기 실행됨");
        adapter = new Plan_details_recyclerview_Adapter(Plan_details_Data_list, this, userId, DATE);
        recyclerView.setAdapter(adapter); // 리사이클러뷰 연결

    }
}