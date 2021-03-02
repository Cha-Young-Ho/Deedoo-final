package com.example.deedo.HOME_ETC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.deedo.Friend.Modify_Friend;
import com.example.deedo.Friend.Search_Somebody;
import com.example.deedo.R;
import com.example.deedo.area.Inquiry_Lotate;
import com.example.deedo.daily.Inquiry_daily_Activity;
import com.example.deedo.inquiry_plan.Plan;

import java.util.ArrayList;
import java.util.List;

public class Home_activity extends AppCompatActivity {
    private TextView main_id;
    ImageButton map_button, plan_button, inquiry_button, friend_setting_button;
    String userId;
    String[] chart_d = {"first", "second", "third"};
    int [] earning = {500, 800, 1000};
    AnyChartView home_chart_view;

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
        
        /*
        Chart View 생성
         */
        home_chart_view = findViewById(R.id.plan_chart_view);
        Setup_Pie_Chart();

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
        main_id.setText(userId + "님 어서오세요!");



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
                Intent intent = new Intent(Home_activity.this, Inquiry_daily_Activity.class);
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

    public void Setup_Pie_Chart(){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < chart_d.length; i++) {
            dataEntries.add(new ValueDataEntry(chart_d[i], earning[i]));
        }

        pie.data(dataEntries);
        home_chart_view.setChart(pie);
    }
}