package com.example.deedo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Administrator on 2017-08-07.
 */

public class Dialog_Plan_details_modify {

    private Context context;
    DBHelper db;
    String userId, before_plan_name;
    int before_plan_executing_hour, before_plan_executing_minute;

    int year;
    int month;
    int day;

    public Dialog_Plan_details_modify(Context context, String _userId, String[] _DATE, String before_plan_name, int before_plan_executing_hour, int before_plan_executing_minute) {
        this.context = context;
        this.userId = _userId;
        this.db = new DBHelper(this.context);
        this.year = Integer.parseInt(_DATE[0]);
        this.month = Integer.parseInt(_DATE[1])+1;
        this.day = Integer.parseInt(_DATE[2]);
        this.before_plan_name = before_plan_name;
        this.before_plan_executing_hour = before_plan_executing_hour;
        this.before_plan_executing_minute = before_plan_executing_minute;
    }

    // 호출할 다이얼로그 함수를 정의
    public void callFunction(final TextView main_label) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성
        final Dialog modify_plan_dialog = new Dialog(context);

        // 액티비티의 타이틀바 숨기기
        modify_plan_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정
        modify_plan_dialog.setContentView(R.layout.activity_plan_details_modify);

        // 커스텀 다이얼로그를 노출
        modify_plan_dialog.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의
        final TextView modify_plan_dialog_title = modify_plan_dialog.findViewById(R.id.modify_plan_dialog_title);
        final Button ok_btn = (Button) modify_plan_dialog.findViewById(R.id.modify_plan_ok_btn);
        final Button cancel_btn = (Button) modify_plan_dialog.findViewById(R.id.modify_plan_cancel_btn);
        final EditText edittext_modify_plan_name = modify_plan_dialog.findViewById(R.id.edittext_modify_plan_name);
        //number picker hour
        final NumberPicker modify_plan_hourpicker = modify_plan_dialog.findViewById(R.id.modify_plan_hourpicker);
        //number picker minute
        final NumberPicker modify_plan_minutepicker = modify_plan_dialog.findViewById(R.id.modify_plan_minutepicker);

        modify_plan_dialog_title.setText(year + "년 " + month + "월 " + day + " 일 Plan 입니다.");

       // edittext_modify_plan_name.setText(db.get_Plan_details_name);


        //number picker 최소 최대 범위
        modify_plan_hourpicker.setMinValue(0);
        modify_plan_hourpicker.setMaxValue(23);
        modify_plan_minutepicker.setMinValue(0);
        modify_plan_minutepicker.setMinValue(59);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int executing_time_hour = modify_plan_hourpicker.getValue();
                int executing_time_minute = modify_plan_minutepicker.getValue();
                String modify_plan_name = edittext_modify_plan_name.getText().toString();


                //시간, 분이 0일 때 입력안되게 하기위한 구문
                if(executing_time_hour == 0 && executing_time_minute == 0){
                    Toast.makeText(Dialog_Plan_details_modify.this.context,"0시간 0분으로 설정되었습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
                }else if(modify_plan_dialog.equals("") || modify_plan_dialog.equals(" ") || modify_plan_dialog == null){
                    Toast.makeText(Dialog_Plan_details_modify.this.context,"이름이 입력되지 않았습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
                } else{
                    //입력한 값 db에 저장
                    db.Modify_plan_detail(userId, year, month, day, modify_plan_name ,executing_time_hour, executing_time_minute,
                            before_plan_name, before_plan_executing_hour, before_plan_executing_minute);
                    Toast.makeText(Dialog_Plan_details_modify.this.context,"성공적으로 입력되었습니다.", Toast.LENGTH_SHORT).show();
                    // 커스텀 다이얼로그를 종료한다.
                    modify_plan_dialog.dismiss();
                }


            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                modify_plan_dialog.dismiss();
            }
        });
    }
}


