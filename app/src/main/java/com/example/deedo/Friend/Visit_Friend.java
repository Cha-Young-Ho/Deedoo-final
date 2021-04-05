package com.example.deedo.Friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deedo.R;
import com.example.deedo.decorator.OneDayDecorator;
import com.example.deedo.decorator.SaturdayDecorator;
import com.example.deedo.decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

public class Visit_Friend extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    TextView textView_Explain, textView_friend_id;
    Button cancle_button;
    String DATE;
    String userId, friendId;
    String year;
    String month;
    String day;
    final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    static CalendarDay daily_selectedDay = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit__friend);

        materialCalendarView = findViewById(R.id.visit_friend_calendarView);
        textView_Explain = findViewById(R.id.textView_friend_id);
        textView_friend_id = findViewById(R.id.textView_friend_id);

        userId = getIntent().getStringExtra("id");
        friendId = getIntent().getStringExtra("friendId");


        cancle_button = findViewById(R.id.visit_friend_cancle_button);
        cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Visit_Friend.this.finish();
            }
        });

        textView_friend_id.setText(friendId + "님의 일정확인하기");
        textView_Explain.setText("일정확인하고 싶은 날짜를 선택!");

        Calendar today = Calendar.getInstance();
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019, 11, 31)) //최소 날짜
                .setMaximumDate(CalendarDay.from(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE))) // 최대 날짜
                .setCalendarDisplayMode(CalendarMode.MONTHS)    //날짜단위
                .commit();


        materialCalendarView.addDecorator(new SundayDecorator()); //일요일 색깔넣기
        materialCalendarView.addDecorator(new SaturdayDecorator()); // 토요일 색깔넣기
        materialCalendarView.addDecorator(oneDayDecorator); //현재날짜 색깔넣기


         /*
        캘린더 클릭 이벤트
         */
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
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

                /*year = parsedDATA[0];

                if ( String.valueOf(Integer.parseInt(parsedDATA[1]) + 1).length() == 1) {
                    month = "0" + String.valueOf(Integer.parseInt(parsedDATA[1]) + 1).length();
                }

                month = String.valueOf(Integer.parseInt(parsedDATA[1]) + 1);

                day = parsedDATA[2];*/

                String year = parsedDATA[0];
                String __month = Integer.toString(Integer.parseInt(parsedDATA[1]) + 1);
                String month;

                if (__month.length() == 1) {
                    month = "0" + __month;
                } else {
                    month = __month;
                }

                String day;
                String __day = parsedDATA[2];
                if (__day.length() == 1) {
                    day = "0" + __day;
                } else {
                    day = __day;
                }



                Intent intent = new Intent(Visit_Friend.this, activity_visit_friend_details.class);
                intent.putExtra("id", userId);
                intent.putExtra("friendId", friendId);

                intent.putExtra("selected_year", year);
                intent.putExtra("selected_month", month);
                intent.putExtra("selected_day", day);
                Log.v("캘린더 클릭 이벤트 네번째 부분 성공", " 성공");
                startActivity(intent); //날짜 데이터 담아서 넘겨줌
            }
        });


    }


    }
