package com.example.deedo.compare;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;

public class Compare_Mydaily_Friend_plan extends AppCompatActivity {
    DBHelperFirebase firebase;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_mydaily_friend);
        firebase = new DBHelperFirebase();

    }
}