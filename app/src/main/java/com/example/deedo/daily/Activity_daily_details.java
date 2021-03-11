package com.example.deedo.daily;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.Friend.Search_Somebody;
import com.example.deedo.R;
import com.example.deedo.callback.Get_Daily_Detail_info;
import com.example.deedo.inquiry_plan.Dialog_Plan_details_create;
import com.example.deedo.inquiry_plan.Plan_details_Data;
import com.example.deedo.inquiry_plan.Plan_details_recyclerview_Adapter;

import java.util.ArrayList;

public class Activity_daily_details extends AppCompatActivity {

    Button plan_details_compare_btn, plan_details_cancel_btn;
    ArrayList<Plan_details_Data> Plan_details_Data_list; //담아온 데이터
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String userId; //로그인된 유저 아이디
    String[] DATE; // Plan 액티비티에서 넘어온 날짜 배열
    TextView textview_plan_details_name;
    int year;
    int month;
    int day;
    DBHelperFirebase firebase;


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

            Intent intent = new Intent(com.example.deedo.daily.Activity_daily_details.this, Search_Somebody.class);
            intent.putExtra("id", userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        firebase = new DBHelperFirebase();
        userId = getIntent().getStringExtra("id");

        this.DATE = getIntent().getStringArrayExtra("DATE");


        year = Integer.parseInt(DATE[0]);
        month = Integer.parseInt(DATE[1])+1;
        day = Integer.parseInt(DATE[2]);


        textview_plan_details_name = findViewById(R.id.textview_plan_details_name);
        textview_plan_details_name.setText(year + "년 " + month + "월 " + day+"일 ");


        recyclerView = findViewById(R.id.plan_details_recyclerview); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        InitializeData(userId);


        plan_details_cancel_btn = findViewById(R.id.plan_details_cancel_btn);
        plan_details_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.example.deedo.daily.Activity_daily_details.this.finish();
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_plan_details);

        userId = getIntent().getStringExtra("id");

        this.DATE = getIntent().getStringArrayExtra("DATE");

        textview_plan_details_name = findViewById(R.id.textview_plan_details_name);
        textview_plan_details_name.setText(year + "년 " + month + "월 " + day+"일 ");


        if (Plan_details_Data_list != null) {
            Plan_details_Data_list.clear();
            Plan_details_Data_list = new ArrayList<>();
        } else {
            Plan_details_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }
        InitializeData(userId); //데이터 정보를 리스트에 담아서옴



        plan_details_compare_btn = findViewById(R.id.plan_details_compare_btn);
        plan_details_compare_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Plan_details_create dialog_plan_details_create =
                        new Dialog_Plan_details_create(com.example.deedo.daily.Activity_daily_details.this, userId, DATE);

                dialog_plan_details_create.show();



            }
        });

        plan_details_cancel_btn = findViewById(R.id.plan_details_cancel_btn);
        plan_details_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity_daily_details.this.finish();
            }
        });
        adapter.notifyDataSetChanged();
    }


    public void First_InitializeData() {


    }

    public void InitializeData(String userId) {

        //DB에서 데이터리스트에 담아야하는 메서드
        Plan_details_Data_list.clear();

        if (Plan_details_Data_list != null) {
            Plan_details_Data_list.clear();
            Plan_details_Data_list = new ArrayList<>();
        } else {
            Plan_details_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }

      firebase.Get_daily_details_info(new Get_Daily_Detail_info() {

          @Override
          public void get_Daily_details_onCallback(Context con) {
              Log.v("get_Daily_onCallback", "이후");
              adapter = new daily_details_recyclerview_Adapter(Plan_details_Data_list, con, userId, DATE);
              recyclerView.setAdapter(adapter); // 리사이클러뷰 연결
          }
      },userId, DATE, this);//리스트에 데이터 담기


    }

}