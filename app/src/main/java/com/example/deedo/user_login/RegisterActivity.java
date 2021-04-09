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

    }
}