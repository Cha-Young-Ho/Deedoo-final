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

public class Dialog_Plan_details_create {

    private Context context;
    DBHelper db;
    String userId;

    int year;
    int month;
    int day;
    public Dialog_Plan_details_create(Context context, String _userId, String[] _DATE) {
        this.context = context;
        this.userId = _userId;
        this.db = new DBHelper(this.context);
        this.year = Integer.parseInt(_DATE[0]);
        this.month = Integer.parseInt(_DATE[1])+1;
        this.day = Integer.parseInt(_DATE[2]);
    }

    // 호출할 다이얼로그 함수를 정의
    public void callFunction(final TextView main_label) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성
        final Dialog create_plan_dialog = new Dialog(context);

        // 액티비티의 타이틀바 숨기기
        create_plan_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정
        create_plan_dialog.setContentView(R.layout.activity_plan_details_create);

        // 커스텀 다이얼로그를 노출
        create_plan_dialog.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의
        final TextView create_plan_dialog_title = create_plan_dialog.findViewById(R.id.create_plan_dialog_title);
        final Button create_plan_ok_btn = (Button) create_plan_dialog.findViewById(R.id.create_plan_ok_btn);
        final Button create_plan_cancel_btn = (Button) create_plan_dialog.findViewById(R.id.create_plan_cancel_btn);
        final EditText edittext_create_plan_name = create_plan_dialog.findViewById(R.id.edittext_create_plan_name);
        //number picker hour
        final NumberPicker create_plan_hourpicker = create_plan_dialog.findViewById(R.id.create_plan_hourpicker);
        //number picker minute
        final NumberPicker create_plan_minutepicker = create_plan_dialog.findViewById(R.id.create_plan_minutepicker);

        create_plan_dialog_title.setText(year + "년 " + month + "월 " + day + " 일 Plan 입니다.");

       // edittext_modify_plan_name.setText(db.get_Plan_details_name);



        //number picker 최소 최대 범위
        create_plan_hourpicker.setMinValue(0);
        create_plan_hourpicker.setMaxValue(23);
        create_plan_minutepicker.setMinValue(0);
        create_plan_minutepicker.setMinValue(59);

        create_plan_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int executing_time_hour = create_plan_hourpicker.getValue();
                int executing_time_minute = create_plan_minutepicker.getValue();
                String create_plan_name = edittext_create_plan_name.getText().toString();


                //시간, 분이 0일 때 입력안되게 하기위한 구문
                if(executing_time_hour == 0 && executing_time_minute == 0){
                    Toast.makeText(Dialog_Plan_details_create.this.context,"0시간 0분으로 설정되었습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
                }else if(create_plan_dialog.equals("") || create_plan_dialog.equals(" ") || create_plan_dialog == null){
                    Toast.makeText(Dialog_Plan_details_create.this.context,"이름이 입력되지 않았습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
                } else{
                    //입력한 값 db에 저장
                    db.create_plan_detail(userId, year, month, day, create_plan_name ,executing_time_hour, executing_time_minute);
                    Toast.makeText(Dialog_Plan_details_create.this.context,"성공적으로 입력되었습니다.", Toast.LENGTH_SHORT).show();
                    // 커스텀 다이얼로그를 종료한다.
                    create_plan_dialog.dismiss();
                }


            }
        });
        create_plan_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                create_plan_dialog.dismiss();
            }
        });
    }
}


