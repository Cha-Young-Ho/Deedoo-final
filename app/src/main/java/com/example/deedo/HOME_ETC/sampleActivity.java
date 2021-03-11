package com.example.deedo.HOME_ETC;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deedo.R;

public class sampleActivity extends AppCompatActivity {

    EditText EditText_id, EditText_name, EditText_longi, EditText_lati;
    Button delete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        EditText_id = findViewById(R.id.EditText_id_);
        EditText_name = findViewById(R.id.EditText_name_);
        EditText_lati = findViewById(R.id.EditText_lati_);
        EditText_longi = findViewById(R.id.EditText_longi_);


    }
}