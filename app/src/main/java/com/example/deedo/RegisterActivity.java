package com.example.deedo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    DBHelper db;
    EditText register_id, register_password, register_name;
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);

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
                boolean id_check = db.Insert_User_Data(_id, _password, _name);

                if(id_check){

                    Toast.makeText(getApplicationContext(), "회원가입이 성공!", Toast.LENGTH_SHORT).show();

                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "중복된 ID!", Toast.LENGTH_SHORT).show();
                }



            }
        });




    }
}