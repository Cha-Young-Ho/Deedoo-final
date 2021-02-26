package com.example.deedo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Search_Somebody extends AppCompatActivity {

    ArrayList<Search_Friend_Data> Search_Friend_Data_list; //담아온 데이터
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    DBHelper db;
    Button item_search_request_friend_btn; //친구 요청 버튼
    String userId; //로그인된 유저 아이디
    ImageButton ImageView_search_somebody_btn; //검색 요청 버튼
    EditText Search_text; //사용자가 입력한 검색어

    String SET_TEXT = "s";
    String search_text = SET_TEXT; // editText에서 받아온 검색어 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_somebody);
        userId = getIntent().getStringExtra("id");

        ImageView_search_somebody_btn = findViewById(R.id.ImageView_search_somebody_btn);
        ImageView_search_somebody_btn.setOnClickListener(new View.OnClickListener() {

            /*
        검색어 입력후 상단 검색 버튼 클릭 시 이벤트
         */
            @Override
            public void onClick(View v) {

                Search_text = findViewById(R.id.Search_text);

                search_text = Search_text.getText().toString();

                db.get_Search_Somebody(search_text, userId);

                Search_text.setText(search_text);
                Log.v("입력된 글자 =", "" + Search_text);
            }
        });



        recyclerView = findViewById(R.id.recyclerview_search_somebody); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Search_Friend_Data_list != null) {
            Search_Friend_Data_list.clear();
            Search_Friend_Data_list = new ArrayList<>();
        } else {
            Search_Friend_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }
        db = new DBHelper(this);

        InitializeData(search_text, userId);  //리스트에 데이터 담기
        adapter = new Search_Friend_Adapter(Search_Friend_Data_list, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰 연결


    }





    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_search_somebody);
        ImageView_search_somebody_btn = findViewById(R.id.ImageView_search_somebody_btn);
        ImageView_search_somebody_btn.setOnClickListener(new View.OnClickListener() {

            /*
        검색어 입력후 상단 검색 버튼 클릭 시 이벤트
         */
            @Override
            public void onClick(View v) {
                String search_text; // editText에서 받아온 검색어 저장
                Search_text = findViewById(R.id.Search_text);

                search_text = Search_text.getText().toString();

                db.get_Search_Somebody(search_text, userId);

                Search_text.setText(search_text);
                Log.v("입력된 글자 =", "" + Search_text);
            }
        });
        userId = getIntent().getStringExtra("id");


        recyclerView = findViewById(R.id.recyclerview_search_somebody); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Search_Friend_Data_list != null) {
            Search_Friend_Data_list.clear();
            Search_Friend_Data_list = new ArrayList<>();
        } else {
            Search_Friend_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }
        db = new DBHelper(this);

        InitializeData(search_text, userId);  //리스트에 데이터 담기
        adapter = new Search_Friend_Adapter(Search_Friend_Data_list, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰 연결


    }



    public void InitializeData(String search_text, String userId) {

        //DB에서 데이터리스트에 담아야하는 메서드
        Search_Friend_Data_list = db.get_Search_Somebody(search_text, userId);
    }



}