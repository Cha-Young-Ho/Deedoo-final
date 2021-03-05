package com.example.deedo.daily;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.deedo.DB.DBHelper;
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
    DBHelper db;
    String userId;
    String[] chart_d = {"first", "second", "third"};
    int[] earning = {500, 800, 1000};
    AnyChartView inquiry_daily_chart_view;
    MaterialCalendarView daily_calendarView;
    Spinner inquiry_spinner;
    TextView textView_inquiry_daily_text;
    Button daily_compare_btn;
    ArrayList<daily_data> daily_data_list = new ArrayList<>();

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
        db = new DBHelper(this);

        String[] str = getResources().getStringArray(R.array.spinner_array);

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

        //드롭박스 스피너 생성
        inquiry_spinner = (Spinner) findViewById(R.id.inquiry_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, str);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        inquiry_spinner.setAdapter(adapter);

        //spinner 이벤트 리스너

        inquiry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //선택된 항목
                    textView_inquiry_daily_text.setText("지난 "+parent.getItemAtPosition(position)+" 동안의 일과입니다!");
                     daily_compare_btn.setText(parent.getItemAtPosition(position)+"계획이랑 비교하기!");
                        /*

                        switch(position){

                            case 0: // 1년
                                this.inquiry_total_date = 365;
                                daily_data_list = db.get_daily_info(365, year, month, day);
                                break;
                            case 1: // 6개월
                                this.inquiry_total_date = 183;
                                daily_data_list = db.get_daily_info(183, year, month, day);
                                break;
                            case 2: // 3개월
                                this.inquiry_total_date = 91;
                                daily_data_list = db.get_daily_info(91, year, month, day);
                                break;
                            case 3: // 1개월
                                this.inquiry_total_date = 30;
                                daily_data_list = db.get_daily_info(30, year, month, day);
                                break;
                            case 4: // 1주일
                                this.inquiry_total_date = 7;
                                daily_data_list = db.get_daily_info(7, year, month, day);
                                break;
                        }

                        if(daily_data_list == null){

                        }else{
                            Setup_Pie_Chart(daily_data_list);
                        }

                         */
                    Log.v("알림", inquiry_spinner.getSelectedItem().toString() + "is selected");


            }

            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

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