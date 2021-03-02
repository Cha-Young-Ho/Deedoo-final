package com.example.deedo.area;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deedo.DB.DBHelper;
import com.example.deedo.R;
import com.example.deedo.Friend.Search_Somebody;

import java.util.ArrayList;

public class Inquiry_Lotate extends AppCompatActivity {
    ArrayList<Area_Data> Area_Data_list;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    Button create_lotate_btn;
    String userId;
    DBHelper db;

    /*
     액션바에 돋보기 추가
      */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*
   액션바에 돋보기 이벤트 추가
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_search_btn) {

            Intent intent = new Intent(Inquiry_Lotate.this, Search_Somebody.class);
            intent.putExtra("id", userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_lotate);
        userId = getIntent().getStringExtra("id");


        create_lotate_btn = findViewById(R.id.create_lotate_btn); //구역 추가 버튼
        create_lotate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inquiry_Lotate.this, Create_Lotate.class);
                intent.putExtra("id", userId);
                startActivity(intent);


            }
        });
        recyclerView = findViewById(R.id.recyclerview_lotate); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Area_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        db = new DBHelper(this);


        InitializeData();  //리스트에 데이터 담기


        adapter = new AreaAdapter(Area_Data_list, this, userId);
        recyclerView.setAdapter(adapter); // 리사이클러뷰 연결


    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_inquiry_lotate);
        userId = getIntent().getStringExtra("id");


        create_lotate_btn = findViewById(R.id.create_lotate_btn); //구역 추가 버튼
        create_lotate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inquiry_Lotate.this, Create_Lotate.class);
                intent.putExtra("id", userId);
                startActivity(intent);


            }
        });
        recyclerView = findViewById(R.id.recyclerview_lotate); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Area_Data_list != null) {
            Area_Data_list.clear();
            Area_Data_list = new ArrayList<>();

        } else {
            Area_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }

        db = new DBHelper(this);


        InitializeData();  //리스트에 데이터 담기


        adapter = new AreaAdapter(Area_Data_list, this, userId);
        recyclerView.setAdapter(adapter); // 리사이클러뷰 연결

    }

    public void InitializeData() {

        Area_Data_list = db.get_Area_info(userId);

    }

}