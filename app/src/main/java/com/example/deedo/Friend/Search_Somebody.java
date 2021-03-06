package com.example.deedo.Friend;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;
import com.example.deedo.callback.Get_Search_Somebody_onCallback;

import java.util.ArrayList;

public class Search_Somebody extends AppCompatActivity {

    ArrayList<Search_Friend_Data> Search_Friend_Data_list; //담아온 데이터
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    Button item_search_request_friend_btn; //친구 요청 버튼
    String userId; //로그인된 유저 아이디
    ImageButton ImageView_search_somebody_btn; //검색 요청 버튼
    EditText Search_text; //사용자가 입력한 검색어
    DBHelperFirebase firebase;

    String search_text; // editText에서 받아온 검색어 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_somebody);
        userId = getIntent().getStringExtra("id");
        firebase = new DBHelperFirebase();
        ImageView_search_somebody_btn = findViewById(R.id.ImageView_search_somebody_btn);



        recyclerView = findViewById(R.id.recyclerview_search_somebody); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Search_Friend_Data_list != null) {
            Search_Friend_Data_list.clear();
            Search_Friend_Data_list = new ArrayList<>();
        } else {
            Search_Friend_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }


        InitializeData(search_text, userId);


    }





    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_search_somebody);
        ImageView_search_somebody_btn = findViewById(R.id.ImageView_search_somebody_btn);
        userId = getIntent().getStringExtra("id");
        /*
        검색어 입력후 상단 검색 버튼 클릭 시 이벤트
         */
        ImageView_search_somebody_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Search_text = findViewById(R.id.Search_text);

                search_text = Search_text.getText().toString();

               // db.get_Search_Somebody(search_text, userId);


                Log.v("입력된 글자 =", "" + search_text);
                InitializeData(search_text, userId);
            }
        });






    }


    public void First_InitializeData(){
      // Search_Friend_Data_list.add(new Search_Friend_Data("", "검색을 해주세요!"));

    }
    public void InitializeData(String search_text, String userId) {

        //DB에서 데이터리스트에 담아야하는 메서드
        Search_Friend_Data_list.clear();

        recyclerView = findViewById(R.id.recyclerview_search_somebody); //리사이클러 뷰 연결
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Search_Friend_Data_list != null) {
            Search_Friend_Data_list.clear();
            Search_Friend_Data_list = new ArrayList<>();
        } else {
            Search_Friend_Data_list = new ArrayList<>(); //  넘어온 데이터를 담을 그릇 (어댑터로)
        }

        Log.v("search_text =", "" + search_text);
        if(search_text == null || search_text.equals("")){

        }else{

            firebase.get_Search_Somebody(new Get_Search_Somebody_onCallback() {
                @Override
                public void get_Search_Somebody(ArrayList<Search_Friend_Data> Search_Friend_Data_list, Context con) {

                    for (int i = 0; i < Search_Friend_Data_list.size(); i++) {
                        Log.v("search somebody메서드 시작 :", i + "번째 친구 아이디 = " + Search_Friend_Data_list.get(i).getSearch_Friend_id() + " \n"+ i + "번째 친구 이름 = " + Search_Friend_Data_list.get(i).getSearch_Friend_name());
                    }
                    adapter = new Search_Friend_Adapter(Search_Friend_Data_list, con, userId);
                    recyclerView.setAdapter(adapter); // 리사이클러뷰 연결
                }
            }, search_text, userId, this);

            Log.v("resume", "여기 실행됨");


        }

    }

}