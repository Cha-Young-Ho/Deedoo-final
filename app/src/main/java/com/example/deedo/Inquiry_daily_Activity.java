package com.example.deedo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class Inquiry_daily_Activity extends AppCompatActivity {


    DBHelper db;
    String userId;
    String[] chart_d = {"first", "second", "third"};
    int [] earning = {500, 800, 1000};
    AnyChartView inquiry_daily_chart_view;

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

        //드롭박스 스피너 생성
        Spinner spinner = (Spinner) findViewById(R.id.inquiry_spinner);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.spinner_layout,str);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        //spinner 이벤트 리스너

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {



            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItemPosition() > 0){

                    //선택된 항목

                    Log.v("알림",spinner.getSelectedItem().toString()+ "is selected");

                }
            }

            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        /*
        Chart View 생성
         */
        inquiry_daily_chart_view = findViewById(R.id.plan_chart_view);
        Setup_Pie_Chart();



    }
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