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
import com.example.deedo.compare.Compare_daily_plan;

import java.util.ArrayList;

public class Activity_daily_details extends AppCompatActivity {

    Button daily_details_compare_btn, daily_details_cancel_btn;
    ArrayList<daily_data> daily_details_Data_list; //담아온 데이터
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String userId; //로그인된 유저 아이디
    String[] DATE; // Plan 액티비티에서 넘어온 날짜 배열
    TextView textview_daily_details_name;
    String year;
    String month;
    String day;
    DBHelperFirebase firebase;
    String friend_Id;


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
        setContentView(R.layout.activity_daily_details);
        firebase = new DBHelperFirebase();
        userId = getIntent().getStringExtra("id");
        friend_Id = getIntent().getStringExtra("friend_Id");

        this.DATE = getIntent().getStringArrayExtra("DATE");


        year = DATE[0];
        month = String.valueOf(Integer.parseInt(DATE[1])+1);
        day = DATE[2];

        String date = year + month + day;

        textview_daily_details_name = findViewById(R.id.textview_daily_details_name);
        textview_daily_details_name.setText(year + "년 " + month + "월 " + day+"일 ");




        InitializeData(userId);


        daily_details_cancel_btn = findViewById(R.id.daily_details_cancel_btn);
        daily_details_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.example.deedo.daily.Activity_daily_details.this.finish();
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_daily_details);

        userId = getIntent().getStringExtra("id");

        this.DATE = getIntent().getStringArrayExtra("DATE");

        textview_daily_details_name = findViewById(R.id.textview_daily_details_name);
        textview_daily_details_name.setText(year + "년 " + month + "월 " + day+"일 ");

        recyclerView = findViewById(R.id.daily_details_recyclerview); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (daily_details_Data_list != null) {
            daily_details_Data_list.clear();
            daily_details_Data_list = new ArrayList<>();
        } else {
            daily_details_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }
        InitializeData(userId); //데이터 정보를 리스트에 담아서옴



        daily_details_compare_btn = findViewById(R.id.daily_details_compare_btn);
        daily_details_compare_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_daily_details.this, Compare_daily_plan.class);
                intent.putExtra("id", userId);
                intent.putExtra("friend_Id", friend_Id);
                intent.putExtra("date", DATE);
                startActivity(intent);
            }
        });

        daily_details_cancel_btn = findViewById(R.id.daily_details_cancel_btn);
        daily_details_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity_daily_details.this.finish();
            }
        });
//        adapter.notifyDataSetChanged();
    }


    public void First_InitializeData() {


    }

    public void InitializeData(String userId) {

        //DB에서 데이터리스트에 담아야하는 메서드

        if (daily_details_Data_list != null) {
            daily_details_Data_list.clear();
            daily_details_Data_list = new ArrayList<>();
        } else {
            daily_details_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }

      firebase.Get_daily_details_info(new Get_Daily_Detail_info() {

          @Override
          public void get_Daily_details_onCallback(ArrayList<daily_data> daily_data_list, Context con) {
              Log.v("get_Daily_onCallback", "이후");
              daily_details_Data_list = daily_data_list;
              Log.v("dadada길이 = " , ""+daily_data_list.size());
              adapter = new daily_details_recyclerview_Adapter(daily_details_Data_list, con, userId, DATE);
              Log.v("4번이요", "");
              recyclerView.setAdapter(adapter); // 리사이클러뷰 연결
              Log.v("5번이요", "");
          }
      },userId, DATE, this);//리스트에 데이터 담기


    }



}