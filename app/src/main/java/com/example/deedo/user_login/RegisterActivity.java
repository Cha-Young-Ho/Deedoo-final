package com.example.deedo.user_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;
import com.example.deedo.callback.Register_Call_back;

public class RegisterActivity extends AppCompatActivity {

    EditText register_id, register_password, register_name;
    Button register_btn;
    DBHelperFirebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.firebase = new DBHelperFirebase();

        register_id = findViewById(R.id.register_id);
        register_password = findViewById(R.id.register_password);
        register_name = findViewById(R.id.register_name);

        register_btn = findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _id = register_id.getText().toString();
                String _password = register_password.getText().toString();
                String _name = register_name.getText().toString();
                 firebase.SignUp(new Register_Call_back() {
                     @Override
                     public void Register_onCallback(String id) {

                         Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                         intent.putExtra("id", id);
                         startActivity(intent);
                     }
                 },_id, _password, _name);




            }
        });
        /*

        // 액티비티 작업하려면 콜백함수를 계속만들어야함. 현재 상황

        1. 로그인 작업 완료
        2. Register 작업 -> 액티비티 넘겨주기
        3. 조회 -> 리사이클 뷰 해야함
        4. 삭제 -> 리사이클 뷰 완료
        5. 수정 및 기타 다이얼로그 -> 콜백 작업
        6. 모든 작업 콜백 해아함
        7. 현재위치 저장 로직 완성
        8. 비교로직 완성


         */
    }
}