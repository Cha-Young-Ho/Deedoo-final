package com.example.deedo.Friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deedo.DB.DBHelper;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;
import com.example.deedo.callback.Get_Friend_onCallback;

import java.util.ArrayList;

public class Modify_Friend extends AppCompatActivity {
    ArrayList<Modify_Friend_Data> Modify_Friend_Data_list; //담아온 데이터
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    DBHelper db;
    DBHelperFirebase firebase;

    String userId; //로그인된 유저 아이디



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

            Intent intent = new Intent(Modify_Friend.this, Search_Somebody.class);
            intent.putExtra("id",userId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_friend);
        userId = getIntent().getStringExtra("id");
        firebase = new DBHelperFirebase();



        recyclerView = findViewById(R.id.Modify_Friend_recyclerview); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Modify_Friend_Data_list != null) {
            Modify_Friend_Data_list.clear();
            Modify_Friend_Data_list = new ArrayList<>();
        } else {
            Modify_Friend_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }


        First_InitializeData();  //첫 리사이클 뷰는 검색어가 입력되지 않아서, First_InitializeData 메서드 호출 - 리스트에 데이터 담기
        Log.v("resume", "여기 실행됨2");
        adapter = new Modify_Friend_Adapter(Modify_Friend_Data_list, this, userId);
        recyclerView.setAdapter(adapter); // 리사이클러뷰 연결


    }





    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_modify_friend);

        userId = getIntent().getStringExtra("id");


        InitializeData(userId);


    }


    public void First_InitializeData(){


    }
    public void InitializeData(String userId) {

        //DB에서 데이터리스트에 담아야하는 메서드
        Modify_Friend_Data_list.clear();

        recyclerView = findViewById(R.id.Modify_Friend_recyclerview); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Modify_Friend_Data_list != null) {
            Modify_Friend_Data_list.clear();
            Modify_Friend_Data_list = new ArrayList<>();
        } else {
            Modify_Friend_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }
        db = new DBHelper(this);





        firebase.Get_friend_info(new Get_Friend_onCallback() {
            @Override
            public void get_Friend_onCallback(ArrayList<Modify_Friend_Data> modify_Friend_Data_list, Context con) {
                Modify_Friend_Data_list = modify_Friend_Data_list;
                Log.v("resume", "여기 실행됨");
                adapter = new Modify_Friend_Adapter(Modify_Friend_Data_list, con, userId);
                recyclerView.setAdapter(adapter); // 리사이클러뷰 연결
            }
        }, userId, this);





    }



}