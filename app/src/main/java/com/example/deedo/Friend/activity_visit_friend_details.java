package com.example.deedo.Friend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deedo.BarChart.BarChart_list_data;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;
import com.example.deedo.callback.Calc_BarChart_data_Callback;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class activity_visit_friend_details extends AppCompatActivity {
    String year;
    String month;
    String day;
    String userId;
    String friendId;
    DBHelperFirebase firebase = new DBHelperFirebase();
    ArrayList<BarChart_list_data> barChart_lists_Data;
    BarChart barChart;
    Button visit_friend_detail_btn;
    TextView textView_visit_friend_detail_title;

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

            Intent intent = new Intent(activity_visit_friend_details.this, Search_Somebody.class);
            getIntent().putExtra("id", userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_friend_details);
        this.barChart = findViewById(R.id.visit_friend_barchart);
        this.userId = getIntent().getStringExtra("id");
        this.friendId = getIntent().getStringExtra("friendId");
        this.year = getIntent().getStringExtra("selected_year");
        this.month = getIntent().getStringExtra("selected_month");
        this.day = getIntent().getStringExtra("selected_day");

        textView_visit_friend_detail_title = findViewById(R.id.textView_visit_friend_detail_title);
        textView_visit_friend_detail_title.setText(friendId + "님의 " + year+"-"+month+"-"+day + " 일과 입니다.");



        visit_friend_detail_btn = findViewById(R.id.visit_friend_detail_btn);
        visit_friend_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Calc_data(userId, friendId, year, month, day);


    }

    public void Calc_data(String userId, String friend_Id, String year, String month, String day) {


        String today_date = year + month + day;

        Log.v("Calc_data 시작", "start");

        firebase.Compare_friend(new Calc_BarChart_data_Callback() {
            @Override
            public void calc_BarChart_data_Callback(ArrayList<BarChart_list_data> barChart_list_data, ArrayList<BarChart_list_data> barChart_lists_friend_Data) {
                for (int i = 0; i < barChart_list_data.size(); i++) {
                    Log.v("태그이름: ", "" + barChart_list_data.get(i).getTag());
                    //Log.v("태그이름: " , "" + barChart_lists_friend_Data.get(i).getTag());
                }
                set_barchart(barChart_list_data, barChart_lists_friend_Data);

            }
        }, today_date, userId, friendId);


    }

    public void set_barchart(ArrayList<BarChart_list_data> barChart_lists_Data, ArrayList<BarChart_list_data> barChart_lists_friend_Data) {
        BarDataSet barDataSet1;
        BarDataSet barDataSet2;
        if (barChart_lists_Data.size() >= barChart_lists_friend_Data.size()) {
            barDataSet1 = new BarDataSet(data1(barChart_lists_Data, barChart_lists_Data.size()), "나의 일과");
            barDataSet2 = new BarDataSet(data2(barChart_lists_friend_Data, barChart_lists_Data.size()), "친구 일과");
        } else {
            barDataSet1 = new BarDataSet(data1(barChart_lists_Data, barChart_lists_Data.size()), "나의 일과");
            barDataSet2 = new BarDataSet(data2(barChart_lists_friend_Data, barChart_lists_Data.size()), "친구 일과");
        }

        barDataSet1.setColor(Color.RED);
        barDataSet2.setColor(Color.CYAN);

        BarData data = new BarData(barDataSet1, barDataSet2);
        barChart.setData(data);

        String[] days = new String[]{"운동", "식사", "근무", "공부", "휴식", "여가활동", "쇼핑", "집", "학교", "유흥", "기타활동"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);
        float groupSpace = 0.11f;
        float barSpace = 0.34f; // x4 DataSet
        float barWidth = 0.1f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        data.setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + (barChart.getBarData().getGroupWidth(groupSpace, barSpace)) * 11);
        barChart.getAxisLeft().setAxisMinimum(0);

        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();

    }

    private ArrayList<BarEntry> data1(ArrayList<BarChart_list_data> barChart_lists_Data, int data_size) {
        ArrayList<BarEntry> data_val = new ArrayList<>();

        Log.v("일과데이터 = ", "꺼내보기");
        for (int i = 0; i < barChart_lists_Data.size(); i++) {
            Log.v("barchart_lists_data", "꺼내보기 " + i + "번째");
            Log.v("tag = ", "" + barChart_lists_Data.get(i).getTag());
            Log.v("second = ", barChart_lists_Data.get(i).getSecond());
        }

        ArrayList<BarChart_list_data> synchronize_chart = Synchronize_Chart(barChart_lists_Data);

        for (int i = 0; i < synchronize_chart.size(); i++) {
            data_val.add(new BarEntry(i, Float.parseFloat(synchronize_chart.get(i).getSecond())));
        }

        return data_val;
    }

    private ArrayList<BarEntry> data2(ArrayList<BarChart_list_data> barChart_lists_friend_Data, int data_size) {
        ArrayList<BarEntry> data_val = new ArrayList<>();

        Log.v("일과데이터 = ", "꺼내보기");
        for (int i = 0; i < barChart_lists_friend_Data.size(); i++) {
            Log.v("bar_friend_Data", "꺼내보기 " + i + "번째");
            Log.v("tag = ", "" + barChart_lists_friend_Data.get(i).getTag());
            Log.v("second = ", barChart_lists_friend_Data.get(i).getSecond());
        }

        ArrayList<BarChart_list_data> synchronize_chart = Synchronize_Chart(barChart_lists_friend_Data);

        for (int i = 0; i < synchronize_chart.size(); i++) {
            data_val.add(new BarEntry(i, Float.parseFloat(synchronize_chart.get(i).getSecond())));
        }

        return data_val;
    }

    public ArrayList<BarChart_list_data> Synchronize_Chart(ArrayList<BarChart_list_data> barChart_lists_Data) {
        ArrayList<BarChart_list_data> Synchronized_list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {

            Synchronized_list.add(i, new BarChart_list_data("a", "" + 0));
        }
        //1운동-2식사-3근무-4공부-5휴식-6여가활동-7쇼핑-8집-9학교-10유흥-11기타활동-12기타
        Log.v("barchart 길이 = ", "" + barChart_lists_Data.size());
        for (int i = 0; i < barChart_lists_Data.size(); i++) {
            Log.v("i = ", "" + i);
            Log.v("tag = ", barChart_lists_Data.get(i).getTag());
            Log.v("second = ", barChart_lists_Data.get(i).getSecond());
            if (barChart_lists_Data.get(i).getTag().equals("운동")) {
                Synchronized_list.set(0, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("식사")) {
                Synchronized_list.set(1, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("근무")) {
                Synchronized_list.set(2, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("공부")) {
                Synchronized_list.set(3, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("휴식")) {
                Synchronized_list.set(4, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("여가활동")) {
                Synchronized_list.set(5, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("쇼핑")) {
                Synchronized_list.set(6, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("집")) {
                Synchronized_list.set(7, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("학교")) {
                Synchronized_list.set(8, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("유흥")) {
                Synchronized_list.set(9, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("기타활동")) {
                Synchronized_list.set(10, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            } else if (barChart_lists_Data.get(i).getTag().equals("기타")) {
                Synchronized_list.set(11, new BarChart_list_data(barChart_lists_Data.get(i).getTag(), barChart_lists_Data.get(i).getSecond()));
            }
        }

        for (int i = 0; i < 12; i++) {


            if (Synchronized_list.get(i).getTag().equals("a")) {
                //1운동-2식사-3근무-4공부-5휴식-6여가활동-7쇼핑-8집-9학교-10유흥-11기타활동-12기타
                if (i == 0) {
                    Synchronized_list.set(0, new BarChart_list_data("운동", "0"));
                } else if (i == 1) {
                    Synchronized_list.set(1, new BarChart_list_data("식사", "0"));
                } else if (i == 2) {
                    Synchronized_list.set(2, new BarChart_list_data("근무", "0"));
                } else if (i == 3) {
                    Synchronized_list.set(3, new BarChart_list_data("공부", "0"));
                } else if (i == 4) {
                    Synchronized_list.set(4, new BarChart_list_data("휴식", "0"));
                } else if (i == 5) {
                    Synchronized_list.set(5, new BarChart_list_data("여가활동", "0"));
                } else if (i == 6) {
                    Synchronized_list.set(6, new BarChart_list_data("쇼핑", "0"));
                } else if (i == 7) {
                    Synchronized_list.set(7, new BarChart_list_data("집", "0"));
                } else if (i == 8) {
                    Synchronized_list.set(8, new BarChart_list_data("학교", "0"));
                } else if (i == 9) {
                    Synchronized_list.set(9, new BarChart_list_data("유흥", "0"));
                } else if (i == 10) {
                    Synchronized_list.set(10, new BarChart_list_data("기타활동", "0"));
                } else if (i == 11) {
                    Synchronized_list.set(11, new BarChart_list_data("기타", "0"));
                }

            }


        }

        for (int i = 0; i < Synchronized_list.size(); i++) {
            Log.v("Synchronized_list =", "" + Synchronized_list.get(i).getTag());
        }
        return Synchronized_list;

    }
}