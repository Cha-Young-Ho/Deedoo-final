package com.example.deedo.HOME_ETC;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.Friend.Modify_Friend;
import com.example.deedo.Friend.Search_Somebody;
import com.example.deedo.R;
import com.example.deedo.area.Inquiry_Lotate;
import com.example.deedo.background_service.BackgroundService;
import com.example.deedo.callback.Create_Chart_view_daily;
import com.example.deedo.daily.Inquiry_daily_Activity;
import com.example.deedo.daily.daily_data;
import com.example.deedo.inquiry_plan.Plan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class Home_activity extends AppCompatActivity {
    public static Object mcontext;
    Handler mHandler;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private TextView main_id;
    ImageButton map_button, plan_button, inquiry_button, friend_setting_button;
    String userId;
    ArrayList<String> chart_d = new ArrayList<>();
    ArrayList<Integer> earning = new ArrayList<>();
    AnyChartView home_chart_view;
    private Intent serviceIntent;
    Intent foregroundServiceIntent = null;
    DBHelperFirebase firebase;
    ArrayList<daily_data> daily_data_list;


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
        Intent intent = getIntent();
        userId = intent.getStringExtra("id");
        firebase = new DBHelperFirebase();
        mcontext = Home_activity.this;
        Log.v("홈 액티비티 시작", "Home_Activity Start");

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                startService();
            }
        } else {
            startService();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());


        firebase.get_daily_info(new Create_Chart_view_daily() {
            @Override
            public void create_Chart_view_daily(ArrayList<daily_data> daily_data_list) {

                for (int i = 0; i < daily_data_list.size(); i++) {
                   chart_d.add(daily_data_list.get(i).getArea_tag());

                }
                for (int i = 0; i < daily_data_list.size(); i++) {
                    earning.add(Integer.parseInt(daily_data_list.get(i).getSecond()));
                }
                /*
             Chart View 생성
                  */
                home_chart_view = findViewById(R.id.plan_chart_view);
                Pie pie = AnyChart.pie();
                List<DataEntry> dataEntries = new ArrayList<>();

                for (int i = 0; i < chart_d.size(); i++) {
                    dataEntries.add(new ValueDataEntry(chart_d.get(i), earning.get(i)));
                }

                pie.data(dataEntries);
                home_chart_view.setChart(pie);
            }
        }, userId, 1, cal);


         




    /*
        //백그라운드 서비스 인텐트가 이전에 생성되어있는 상태에서 어플을 실행하면 기존의 intent를 종료하고 새로 서비스를 시작
        if (BackgroundService.serviceIntent==null) {
            serviceIntent = new Intent(this, BackgroundService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = BackgroundService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }





        startService(new Intent(getApplicationContext(), BackgroundService.class));

     */



        //id값 확인


        //여기까지

        userId = intent.getStringExtra("id"); // 로그인한 id값 (primary key)
        map_button = findViewById(R.id.map_Button);
        plan_button = findViewById(R.id.plan_Button);
        inquiry_button = findViewById(R.id.inquiry_Button);
        friend_setting_button = findViewById(R.id.friend_setting_Button);
        main_id = findViewById(R.id.main_id);
        main_id.setText(userId + "님 어서오세요!");

        //main_id.setText(now_latitude + " --" + now_longitude);


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

    public void Setup_Pie_Chart() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 어플 종료 시 서비스를 중단시킨다.
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }

    }


    void startService() {

        LocationBroadcastReceiver receiver = new LocationBroadcastReceiver();
        IntentFilter filter = new IntentFilter("ACT_LOC");
        registerReceiver(receiver, filter);

            /*
         intent가 생성되지 않은 상태 (이미 백그라운드가 실행중이 아닐 때 home_activity가 실행되면) intent를 새로 만들어라
             */
        if (null == BackgroundService.serviceIntent) {
            foregroundServiceIntent = new Intent(this, BackgroundService.class);
            foregroundServiceIntent.putExtra("id", userId);
            startService(foregroundServiceIntent);
            Toast.makeText(getApplicationContext(), "start service", Toast.LENGTH_LONG).show();
        } else {
            /*
         이미 intent가 생성된 상태 (이미 백그라운드가 실행중일 때 home_activity가 실행되면) 해당 intent를 그대로 사용하라
             */
            foregroundServiceIntent = BackgroundService.serviceIntent;
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

    }


    // 서비스에서 메인스레드로 보내기
    public class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACT_LOC")) {

                //받아온 정보
                double latitude = intent.getDoubleExtra("latitude", 0);
                double longiude = intent.getDoubleExtra("longitude", 0);


            }
        }
    }


}