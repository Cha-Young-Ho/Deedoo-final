package com.example.deedo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    DBHelper db;
    private EditText login_id, login_password;
    private Button login_ok_btn, login_register_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);
        login_ok_btn = findViewById(R.id.login_btn);
        login_register_btn = findViewById(R.id.login_register_btn);

        login_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_ok_btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = new DBHelper(LoginActivity.this);
                String _id = login_id.getText().toString();
                String _password = login_password.getText().toString();

                String str = db.login(_id, _password);

                if(str.equals("")){
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다. str = " + str, Toast.LENGTH_SHORT).show();;
                }else{
                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다. 로그인 ID = " + str, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, Home_activity.class);
                    intent.putExtra("id", str);
                    startActivity(intent);
                }


            }
        }));




    }
}