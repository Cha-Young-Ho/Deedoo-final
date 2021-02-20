package com.example.deedo;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

    ImageView icon ;
    TextView textView_name ;
    TextView textView_information;
    Button btn_inquiry;
    Button btn_cancel;

    ViewHolder(View itemView) {


        // 뷰 객체에 대한 참조. (hold strong reference)
        textView_name = itemView.findViewById(R.id.textView_name) ;
        textView_information = itemView.findViewById(R.id.textView_information) ;
        btn_cancel = itemView.findViewById(R.id.button_cancel);
        btn_inquiry = itemView.findViewById(R.id.button_inquiry);
    }
}

