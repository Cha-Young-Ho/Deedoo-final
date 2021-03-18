package com.example.deedo.daily;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;


/**
 * Created by Administrator on 2017-08-07.
 */

public class Activity_daily_compare extends Dialog {
    DBHelperFirebase firebase = new DBHelperFirebase();
    private Context context;
    String userId;
    int year;
    int month;
    int day;


    public Activity_daily_compare(@NonNull Context context, String _userId, String[] _DATE) {
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
        this.setContentView(R.layout.activity_daily_compare);



        // 커스텀 다이얼로그의 각 위젯들을 정의
        final TextView create_daily_dialog_title = this.findViewById(R.id.create_daily_dialog_title);
        final Button create_daily_cancel_btn = (Button) this.findViewById(R.id.create_daily_cancel_btn);
        create_daily_dialog_title.setText(year + "년 " + month + "월 " + day + " 일 Plan 입니다.");
        create_daily_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dismiss();
            }
        });


    }


}


