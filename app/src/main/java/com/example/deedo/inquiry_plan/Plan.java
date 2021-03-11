package com.example.deedo.inquiry_plan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.decorator.EventDecorator;
import com.example.deedo.decorator.OneDayDecorator;
import com.example.deedo.R;
import com.example.deedo.decorator.SaturdayDecorator;
import com.example.deedo.Friend.Search_Somebody;
import com.example.deedo.decorator.SundayDecorator;
import com.example.deedo.decorator.minMaxDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Plan extends AppCompatActivity {
    String userId;
    MaterialCalendarView plan_calendarView;
    final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    AnyChartView plan_chart_view;
    String[] chart_d = {"first", "second", "third"};
    int [] earning = {500, 800, 1000};
    TextView textView;
    DBHelperFirebase firebase;



    /*
    캘린더 뷰 클릭 이벤트에 필요한 변수
     */
    static CalendarDay selectedDay = null;
    static boolean Selected;
    String DATE;
    int year;
    int month;
    int day;
    int hour;
    int min;


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

            Intent intent = new Intent(Plan.this, Search_Somebody.class);
            intent.putExtra("id", userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        firebase = new DBHelperFirebase();
        userId = getIntent().getStringExtra("id");
        textView = findViewById(R.id.na);

        /*
        캘린더 뷰 추가
         */
        plan_calendarView = findViewById(R.id.plan_calendarView);
    
        /*
        캘린더 뷰에 설정 넣기
         */
        Calendar today = Calendar.getInstance();
        plan_calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 1)) //최소 날짜
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 최대 날짜
                .setCalendarDisplayMode(CalendarMode.MONTHS)    //날짜단위
                .commit();




        //이전 날짜 데코
        plan_calendarView.addDecorators(new minMaxDecorator(CalendarDay.today(), CalendarDay.from(2030, 11, 31)));
        
        plan_calendarView.addDecorator(new SundayDecorator()); //일요일 색깔넣기
        plan_calendarView.addDecorator(new SaturdayDecorator()); // 토요일 색깔넣기
        plan_calendarView.addDecorator(oneDayDecorator); //현재날짜 색깔넣기
        
        //빨간점 넣기
        plan_calendarView.addDecorator(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today()), this)); //빨간점 넣기
        
        /*
        캘린더 클릭 이벤트
         */
        plan_calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            /*
            익명 클래스 오버라이드
             */
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                
                
                Log.v("캘린더 클릭 이벤트 첫 부분 성공", " 성공");
                selectedDay = date;
                DATE = selectedDay.toString(); // ex : Calender{2021-02-28}

                String[] parsedDATA = DATE.split("[{]"); // ex : [0] = Calender || [1] = 2021-02-28}

                parsedDATA = parsedDATA[1].split("[}]"); // ex : [0] = 2021-02-28 || [1] = ""

                parsedDATA = parsedDATA[0].split("-"); // ex : [0] = 2021 || [1] = 02 || [2] = 28


                year = Integer.parseInt(parsedDATA[0]);

                month = Integer.parseInt(parsedDATA[1])+1;

                day = Integer.parseInt(parsedDATA[2]);

                Intent intent = new Intent(Plan.this, Activity_plan_details.class);
                intent.putExtra("id", userId);
                intent.putExtra("DATE", parsedDATA);
                Log.v("캘린더 클릭 이벤트 네번째 부분 성공", " 성공");
                startActivity(intent); //날짜 데이터 담아서 넘겨줌



///////////////////////////////////////////////////////////////////////테이블 만들고 다이알로그나 액티비티이동 후 계획 조회가능하게 리사이클뷰로 만들어야함

            }
        });

         /*
        Chart View 생성
         */
        plan_chart_view = findViewById(R.id.plan_chart_view);
        Setup_Pie_Chart();

    }
    public void Setup_Pie_Chart(){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < chart_d.length; i++) {
            dataEntries.add(new ValueDataEntry(chart_d[i], earning[i]));
        }

        pie.data(dataEntries);
        plan_chart_view.setChart(pie);
    }

}