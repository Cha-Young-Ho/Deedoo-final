package com.example.deedo.DB;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.deedo.Friend.Modify_Friend_Data;
import com.example.deedo.Friend.Search_Friend_Data;
import com.example.deedo.area.Area_Data;
import com.example.deedo.callback.Get_Area_info_onCallback;
import com.example.deedo.callback.Get_Friend_onCallback;
import com.example.deedo.callback.Get_Plan_Detail_info;
import com.example.deedo.callback.Get_Search_Somebody_onCallback;
import com.example.deedo.callback.MyCallback;
import com.example.deedo.inquiry_plan.Plan_details_Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelperFirebase{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String match_id = "";
    final String TAG = "1";
    public void SignUp(String _id, String _password, String name){
        Map<String, Object> user = new HashMap<>();
        user.put("userId", _id);
        user.put("userPassword", _password);
        user.put("userName", name);

        Log.v("파이어베이스 시작", " 시작시작시작");
// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Log.v("파이어베이스 시작", " 실패실패");
                    }
                });
    }
    public void insert_create_Area(String _id, String _name, String _latitude, String _longitude) {
        Map<String, Object> Area = new HashMap<>();
        Area.put("userId", _id);
        Area.put("AreaName", _name);
        Area.put("AreaLatitude", _latitude);
        Area.put("AreaLongitude", _longitude);

        Log.v("파이어베이스 로테이트 인서트 시작", " id = " + _id+" AreaName = " + _name+" AreaLatitude = " + _latitude+" AreaLongitude " + _longitude);
        // Add a new document with a generated ID
        db.collection("Area")
                .add(Area)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Area 인서트 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Log.d(TAG, "Area 인서트 실패");
                    }
                });
    }
    public void create_plan_detail(String _userId, int _year, int _month, int _day, String _create_plan_name, int executing_hour, int executing_minute) {
        Map<String, Object> Plan = new HashMap<>();
        Plan.put("userId", _userId);
        Plan.put("Plan_Year", _year);
        Plan.put("Plan_Month", _month);
        Plan.put("Plan_Day", _day);
        Plan.put("Plan_Name", _create_plan_name);
        Plan.put("Executing_Hour", executing_hour);
        Plan.put("Executing_Minute", executing_minute);

        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId+" _year = " + _year+" _create_plan_name = " + _create_plan_name+" executing_minute " + executing_minute);
        // Add a new document with a generated ID
        db.collection("Plan")
                .add(Plan)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Plan 인서트 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Log.d(TAG, "Plan 인서트 실패");
                    }
                });
    }

/*

데일리 구현해야함
    public void create_plan_daily(String _userId, int _year, int _month, int _day, String _create_plan_name, int executing_hour, int executing_minute) {
        Map<String, Object> Plan = new HashMap<>();
        Plan.put("userId", _userId);
        Plan.put("Plan_Year", _year);
        Plan.put("Plan_Month", _month);
        Plan.put("Plan_Day", _day);
        Plan.put("Plan_Name", _create_plan_name);
        Plan.put("Executing_Hour", executing_hour);
        Plan.put("Executing_Minute", executing_minute);

        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId+" _year = " + _year+" _create_plan_name = " + _create_plan_name+" executing_minute " + executing_minute);
        // Add a new document with a generated ID
        db.collection("Plan")
                .add(Plan)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Plan 인서트 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Log.d(TAG, "Plan 인서트 실패");
                    }
                });
    }
    
    */

    public void Create_Friend(String _userId, String Friend_id, String Friend_Name){
         /*
        친구 관계 테이블
             User1  User2
        ---------------------
             A    -   B
             B    -   A

             A    -   C
             C    -   A
        ---------------------

        1:1 매칭되도록 쿼리문 2개 작성

         */

        //유저 A
        Map<String, Object> User1_Friend = new HashMap<>();
        User1_Friend.put("userId", _userId);
        User1_Friend.put("Friend_Id", Friend_id);




        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId+" Friend_Id = " + Friend_id+" Friend_Name = " + Friend_Name);
        // Add a new document with a generated ID
        db.collection("User1_Friend")
                .add(User1_Friend)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User1_Friend 인서트 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Log.d(TAG, "User1_Friend 인서트 실패");
                    }
                });
        // 유저 B
        Map<String, Object> User2_Friend = new HashMap<>();
        User2_Friend.put("userId", Friend_id);
        User2_Friend.put("Friend_Id", _userId);



        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId+" Friend_Id = " + Friend_id+" Friend_Name = " + Friend_Name);
        // Add a new document with a generated ID
        db.collection("User2_Friend")
                .add(User2_Friend)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User2_Friend 인서트 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Log.d(TAG, "User2_Friend 인서트 실패");
                    }
                });

    }
 

    public void login(MyCallback myCallback){

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("userId").equals("ckdudgh") && document.getData().get("userPassword").equals("123")) {


                                    match_id = "ckdudgh";
                                    Log.v("11111111111", "파이어베이스 로그인 성공!. 로그인 아이디 =" + match_id);
                                }
                            }
                            Log.v("1.5555555", "파이어베이스 로그인 성공!. 로그인 아이디 =" + match_id);
                            myCallback.login_onCallback(match_id);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        Log.v("2222222222", "파이어베이스 로그인 성공!. 로그인 아이디 =" + match_id);


                    }


                });
        Log.v("33333333333", "파이어베이스 로그인 성공!. 로그인 아이디 =" + match_id);



    }

    public void get_Area_info(Get_Area_info_onCallback get_area_info_onCallback, String _id, Context con) {
        ArrayList<Area_Data> Area_Data_list = new ArrayList<>();

        db.collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if(document.getData().get("userId").equals(_id)){

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_Area_info 조회 성공!!. 로그인 아이디 =" + _id);
                                    String AreaName = document.getData().get("AreaName").toString();
                                    String AreaLatitude = document.getData().get("AreaLatitude").toString();
                                    String AreaLongitude = document.getData().get("AreaLongitude").toString();
                                    Area_Data_list.add(new Area_Data(AreaName, AreaLatitude, AreaLongitude));
                                }

                            }
                            get_area_info_onCallback.get_Area_info_onCallback(Area_Data_list, con);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

    }

    public void get_Search_Somebody(Get_Search_Somebody_onCallback get_search_somebody_onCallback, String insert_text, String userId, Context con) {
        ArrayList<Search_Friend_Data> Search_Friend_Data_list = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                QueryDocumentSnapshot temp = (QueryDocumentSnapshot) document.getData();
                                if((temp.get("userId").toString().contains(insert_text) || temp.get("userName").toString().contains(insert_text)) && !(temp.get("userId").toString().contains(userId))){

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_Area_info 조회 성공!!. 로그인 아이디 =" + userId);
                                    String FriendName = document.getData().get("userId").toString();
                                    String FriendId = document.getData().get("userName").toString();
                                    Search_Friend_Data_list.add(new Search_Friend_Data(FriendId, FriendName));
                                }

                            }
                            get_search_somebody_onCallback.get_Search_Somebody(Search_Friend_Data_list, con);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    public void Get_friend_info(Get_Friend_onCallback get_friend_onCallback, String userId, Context con){
        ArrayList<Modify_Friend_Data> modify_friend_data_list = new ArrayList<>();

        db.collection("User1_Friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                QueryDocumentSnapshot temp = (QueryDocumentSnapshot) document.getData();
                                if((temp.get("userId").equals(userId))){

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_Area_info 조회 성공!!. 로그인 아이디 =" + userId);

                                    String FriendId = document.getData().get("userId").toString();
                                    modify_friend_data_list.add(new Modify_Friend_Data(FriendId));
                                }

                            }
                            get_friend_onCallback.get_Friend_onCallback(modify_friend_data_list, con);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

    }

    public void Get_plan_details_info(Get_Plan_Detail_info get_plan_detail_info, String _userId, String[] DATE, Context con) {
        ArrayList<Plan_details_Data> plan_details_data_list = new ArrayList<>();
        int year = Integer.parseInt(DATE[0]);
        int month = Integer.parseInt(DATE[1]) + 1;
        int day = Integer.parseInt(DATE[2]);

        db.collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                QueryDocumentSnapshot temp = (QueryDocumentSnapshot) document.getData();
                                if(temp.get("userId").equals(_userId) && temp.get("Plan_Year").equals(year) && temp.get("Plan_Month").equals(month)&& temp.get("Plan_Day").equals(day)){

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_plan_details_info 조회 성공!!");

                                    String plan_name = document.getData().get("Plan_Name").toString();
                                    int planExecuting_hour =Integer.parseInt(document.getData().get("Executing_Hour").toString());
                                    int planExecuting_minute =Integer.parseInt(document.getData().get("Executing_Minute").toString());
                                    plan_details_data_list.add(new Plan_details_Data(plan_name, planExecuting_hour, planExecuting_minute));
                                }

                            }

                            get_plan_detail_info.get_plan_details_onCallback(plan_details_data_list, con);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }


}


