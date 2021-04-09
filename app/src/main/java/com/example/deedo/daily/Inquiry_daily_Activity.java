package com.example.deedo.daily;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.anychart.AnyChartView;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.Friend.Search_Somebody;
import com.example.deedo.R;
import com.example.deedo.decorator.OneDayDecorator;
import com.example.deedo.decorator.SaturdayDecorator;
import com.example.deedo.decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Inquiry_daily_Activity extends AppCompatActivity {
    final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    String userId;

    AnyChartView inquiry_daily_chart_view;
    MaterialCalendarView daily_calendarView;
    TextView textView_inquiry_daily_text;
    Button daily_compare_btn;
    ArrayList<daily_data> daily_data_list = new ArrayList<>();
    DBHelperFirebase firebase;
    int period;
    int inquiry_total_date;
    int year;
    int month;
    int day;
    static CalendarDay daily_selectedDay = null;
    String DATE;
    String daily_Tag = "오늘";
    private ViewPager viewPager ;
    private Chart_View_Page_Adapter chart_view_page_adapter;
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


                daily_selectedDay = date;
                DATE = daily_selectedDay.toString(); // ex : Calender{2021-02-28}

                String[] parsedDATA = DATE.split("[{]");

                parsedDATA = parsedDATA[1].split("[}]");

                parsedDATA = parsedDATA[0].split("-");

                year = Integer.parseInt(parsedDATA[0]);

                month = Integer.parseInt(parsedDATA[1]) + 1;

                day = Integer.parseInt(parsedDATA[2]);

                Intent intent = new Intent(Inquiry_daily_Activity.this, Activity_daily_details.class);
                intent.putExtra("id", userId);
                intent.putExtra("friend_Id", userId);
                intent.putExtra("DATE", parsedDATA);
                startActivity(intent); //날짜 데이터 담아서 넘겨줌
            }
        });


        //페이지 뷰
        viewPager = (ViewPager) findViewById(R.id.chart_view_page) ;
        chart_view_page_adapter = new Chart_View_Page_Adapter(this, userId) ;
        viewPager.setAdapter(chart_view_page_adapter) ;


    }


}