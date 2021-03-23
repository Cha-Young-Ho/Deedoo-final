package com.example.deedo.daily;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.Friend.Search_Somebody;
import com.example.deedo.R;
import com.example.deedo.compare.Compare_daily_plan;
import com.example.deedo.decorator.OneDayDecorator;
import com.example.deedo.decorator.SaturdayDecorator;
import com.example.deedo.decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Inquiry_daily_Activity extends AppCompatActivity {
    final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    String userId;
    String[] chart_d = {"first", "second", "third"};
    int[] earning = {500, 800, 1000};
    AnyChartView inquiry_daily_chart_view;
    MaterialCalendarView daily_calendarView;
    TextView textView_inquiry_daily_text;
    Button daily_compare_btn;
    ArrayList<daily_data> daily_data_list = new ArrayList<>();
    DBHelperFirebase firebase;

    int inquiry_total_date;
    int year;
    int month;
    int day;
    static CalendarDay daily_selectedDay = null;
    String DATE;
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

            Intent intent = new Intent(Inquiry_daily_Activity.this, Search_Somebody.class);
            intent.putExtra("id", userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_daily);
        firebase = new DBHelperFirebase();
        userId = getIntent().getStringExtra("id");


        //비교하러가기 버튼
        daily_compare_btn = findViewById(R.id.daily_compare_btn);
        daily_compare_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inquiry_daily_Activity.this, Compare_daily_plan.class);
                intent.putExtra("compare_date_amount", inquiry_total_date);
                intent.putExtra("daily_data_list", daily_data_list);
                startActivity(intent);
            }

        });
        textView_inquiry_daily_text = findViewById(R.id.textView_inquiry_daily_text);
        textView_inquiry_daily_text.setText("오늘의 일과");




        /*
        Chart View 생성
         */
        inquiry_daily_chart_view = findViewById(R.id.inquiry_daily_chart_view);
        Setup_Pie_Chart();


        /*
        캘린더 뷰
         */
        daily_calendarView = findViewById(R.id.daily_calendarView);
        Calendar today = Calendar.getInstance();
        daily_calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019, 11, 31)) //최소 날짜
                .setMaximumDate(CalendarDay.from(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE))) // 최대 날짜
                .setCalendarDisplayMode(CalendarMode.MONTHS)    //날짜단위
                .commit();


        daily_calendarView.addDecorator(new SundayDecorator()); //일요일 색깔넣기
        daily_calendarView.addDecorator(new SaturdayDecorator()); // 토요일 색깔넣기
        daily_calendarView.addDecorator(oneDayDecorator); //현재날짜 색깔넣기
        textView_inquiry_daily_text = findViewById(R.id.textView_inquiry_daily_text);

         /*
        캘린더 클릭 이벤트
         */
        daily_calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            /*
            익명 클래스 오버라이드
             */
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


                Log.v("캘린더 클릭 이벤트 첫 부분 성공", " 성공");
                daily_selectedDay = date;
                DATE = daily_selectedDay.toString(); // ex : Calender{2021-02-28}

                String[] parsedDATA = DATE.split("[{]"); // ex : [0] = Calender || [1] = 2021-02-28}

                parsedDATA = parsedDATA[1].split("[}]"); // ex : [0] = 2021-02-28 || [1] = ""

                parsedDATA = parsedDATA[0].split("-"); // ex : [0] = 2021 || [1] = 02 || [2] = 28

                year = Integer.parseInt(parsedDATA[0]);

                month = Integer.parseInt(parsedDATA[1]) + 1;

                day = Integer.parseInt(parsedDATA[2]);

                Intent intent = new Intent(Inquiry_daily_Activity.this, Activity_daily_details.class);
                intent.putExtra("id", userId);
                intent.putExtra("friend_Id", userId);
                intent.putExtra("DATE", parsedDATA);
                Log.v("캘린더 클릭 이벤트 네번째 부분 성공", " 성공");
                startActivity(intent); //날짜 데이터 담아서 넘겨줌
            }
        });


    }

    /*
    public void Setup_Pie_Chart(ArrayList<daily_data> daily_data_list){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < daily_data_list.size(); i++) {
            int total_minute = 0; // hour -> minute으로 바꿔주기 위함
            for (int j = 0; j < daily_data_list.size(); j++) {
                total_minute += (daily_data_list.get(i).getHour()* 60) + daily_data_list.get(i).getMinute();
            }

            dataEntries.add(new ValueDataEntry(daily_data_list.get(i).getDaily_name(), total_minute));
        }

        pie.data(dataEntries);
        inquiry_daily_chart_view.setChart(pie);
    }

     */
    public void Setup_Pie_Chart(){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < chart_d.length; i++) {
            dataEntries.add(new ValueDataEntry(chart_d[i], earning[i]));
        }

        pie.data(dataEntries);
        inquiry_daily_chart_view.setChart(pie);
    }

}