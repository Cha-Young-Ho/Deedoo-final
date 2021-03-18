package com.example.deedo.inquiry_plan;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;
import com.example.deedo.callback.Create_Plan_Callback;


/**
 * Created by Administrator on 2017-08-07.
 */

public class Dialog_Plan_details_create extends Dialog {
    DBHelperFirebase firebase = new DBHelperFirebase();
    private Context context;
    String userId;
    int year;
    int month;
    int day;
    Spinner create_plan_spinner;
    String plan_Tag = "기타";

    public Dialog_Plan_details_create(@NonNull Context context, String _userId, String[] _DATE) {
        super(context);
        this.context = context;
        this.userId = _userId;
        this.year = Integer.parseInt(_DATE[0]);
        this.month = Integer.parseInt(_DATE[1]) + 1;
        this.day = Integer.parseInt(_DATE[2]);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성


        // 액티비티의 타이틀바 숨기기
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정
        this.setContentView(R.layout.activity_plan_details_create);



        // 커스텀 다이얼로그의 각 위젯들을 정의
        final TextView create_plan_dialog_title = this.findViewById(R.id.create_plan_dialog_title);
        final Button create_plan_ok_btn = (Button) this.findViewById(R.id.create_plan_ok_btn);
        final Button create_plan_cancel_btn = (Button) this.findViewById(R.id.create_plan_cancel_btn);
        final EditText edittext_create_plan_name = this.findViewById(R.id.edittext_create_plan_name);
        //number picker time
        final TimePicker create_plan_timepicker = this.findViewById(R.id.create_plan_timepicker);
        create_plan_timepicker.setIs24HourView(true);




        create_plan_dialog_title.setText(year + "년 " + month + "월 " + day + " 일 Plan 입니다.");

        // edittext_modify_plan_name.setText(db.get_Plan_details_name);




        create_plan_ok_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {



                final int executing_time_hour = create_plan_timepicker.getHour();
                final int executing_time_minute = create_plan_timepicker.getMinute();

                String create_plan_name = edittext_create_plan_name.getText().toString();


                //시간, 분이 0일 때 입력안되게 하기위한 구문
                if (executing_time_hour == 0 && executing_time_minute == 0) {
                    Toast.makeText(Dialog_Plan_details_create.this.context, "0시간 0분으로 설정되었습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
                } else if (this.equals("") || this.equals(" ") || this == null) {
                    Toast.makeText(Dialog_Plan_details_create.this.context, "이름이 입력되지 않았습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    //입력한 값 db에 저장

                  firebase.create_plan_detail(new Create_Plan_Callback() {
                      @Override
                      public void create_Plan_Callback() {
                          Toast.makeText(Dialog_Plan_details_create.this.context, "성공적으로 입력되었습니다.", Toast.LENGTH_SHORT).show();
                          // 커스텀 다이얼로그를 종료한다.
                          dismiss();
                      }
                  },userId, year, month, day, create_plan_name, executing_time_hour, executing_time_minute, plan_Tag);





                }




            }
        });
        create_plan_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dismiss();
            }
        });
        String[] str = getContext().getResources().getStringArray(R.array.spinner_array_area);
        //드롭박스 스피너 생성
        create_plan_spinner = (Spinner) findViewById(R.id.create_plan_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, R.layout.spinner_layout, str);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        create_plan_spinner.setAdapter(adapter);

        //spinner 이벤트 리스너

        create_plan_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plan_Tag = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}


