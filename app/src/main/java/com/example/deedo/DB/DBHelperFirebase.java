package com.example.deedo.DB;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.deedo.Friend.Modify_Friend_Data;
import com.example.deedo.Friend.Search_Friend_Data;
import com.example.deedo.area.Area_Data;
import com.example.deedo.callback.Create_Area_Callback;
import com.example.deedo.callback.Create_Chart_view_daily;
import com.example.deedo.callback.Create_Friends_Callback;
import com.example.deedo.callback.Create_Plan_Callback;
import com.example.deedo.callback.Delete_Area_Callback;
import com.example.deedo.callback.Delete_Friend_Callback;
import com.example.deedo.callback.Delete_Plan_Callback;
import com.example.deedo.callback.Get_Area_info_onCallback;
import com.example.deedo.callback.Get_Daily_Detail_info;
import com.example.deedo.callback.Get_Friend_onCallback;
import com.example.deedo.callback.Get_Plan_Detail_info;
import com.example.deedo.callback.Get_Search_Somebody_onCallback;
import com.example.deedo.callback.Modify_Plan_Callback;
import com.example.deedo.callback.MyCallback;
import com.example.deedo.callback.Register_Call_back;
import com.example.deedo.daily.daily_data;
import com.example.deedo.inquiry_plan.Plan_details_Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelperFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String match_id = "";
    final String TAG = "1";

    public void SignUp(Register_Call_back register_call_back, String _id, String _password, String name) {


        Log.v("파이어베이스 시작", " 시작시작시작");
// Add a new document with a generated ID
        Map<String, Object> user = new HashMap<>();
        user.put("userId", _id);
        user.put("userPassword", _password);
        user.put("userName", name);

// Add a new document with a generated ID
        db.collection("User")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        register_call_back.Register_onCallback(_id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public void create_daily(String _userId, String[] DATE, String _daily_name){
        Map<String, Object> Daily = new HashMap<>();
        Daily.put("userId", _userId);
        Daily.put("기타", "0");

        String year = DATE[0];
        String month = Integer.toString(Integer.parseInt(DATE[1]) + 1);
        String day = DATE[2];
        String date = year + month+day;
        DocumentReference docRef = db.collection("Daily").document(""+date);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        DocumentReference washingtonRef = db.collection("Daily").document(""+date);

                        washingtonRef.update(_daily_name, FieldValue.increment(5));
                    } else {
                        Log.d(TAG, "No such document");
                        db.collection("Daily").document(date)
                                .set(Daily)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        DocumentReference washingtonRef = db.collection("Daily").document(""+date);
                                        washingtonRef.update(_daily_name, FieldValue.increment(5));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void insert_create_Area(Create_Area_Callback create_area_callback, String _id, String _name, String _latitude, String _longitude) {
        Map<String, Object> Area = new HashMap<>();
        Area.put("userId", _id);
        Area.put("AreaName", _name);
        Area.put("AreaLatitude", _latitude);
        Area.put("AreaLongitude", _longitude);

        Log.v("파이어베이스 로테이트 인서트 시작", " id = " + _id + " AreaName = " + _name + " AreaLatitude = " + _latitude + " AreaLongitude " + _longitude);
        // Add a new document with a generated ID
        db.collection("Area")
                .add(Area)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        create_area_callback.create_Area_Callback(_name, _latitude, _longitude);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void insert_daily(String _userId, String[] DATE, String _daily_name, String _latitude, String _longitude) {
        Map<String, Object> Daily = new HashMap<>();
        Daily.put("userId", _userId);

        String year = DATE[0];
        String month = Integer.toString(Integer.parseInt(DATE[1]) + 1);
        String day = DATE[2];
        String date = year+month+day;


        db.collection("Daily")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("firebase insert", " daily 실행 중");
                                if(document.getData().get("userId").equals(_userId) && document.getData().get("Daily_Name").equals(_daily_name) &&
                                        document.getData().get("Latitude").equals(_latitude) && document.getData().get("Longitude").equals(_longitude)){
                                    String documentpath = document.getId();
                                    String time = String.valueOf(Integer.parseInt(document.getData().get("Daily_Time").toString()) + 5);

                                    Log.v("document path=", documentpath);
                                    db.collection("Plan").document(documentpath)
                                            .update(
                                                "Daily_Time", time
                                            );


                                }

                            }


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    public void create_plan_detail(Create_Plan_Callback create_plan_callback, String _userId, int _year, int _month, int _day, String _create_plan_name, int executing_hour, int executing_minute) {
        Map<String, Object> Plan = new HashMap<>();
        Plan.put("userId", _userId);
        Plan.put("Plan_Year", _year);
        Plan.put("Plan_Month", _month);
        Plan.put("Plan_Day", _day);
        Plan.put("Plan_Name", _create_plan_name);
        Plan.put("Executing_Hour", executing_hour);
        Plan.put("Executing_Minute", executing_minute);

        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId + " _year = " + _year + " _create_plan_name = " + _create_plan_name + " executing_minute " + executing_minute);
        // Add a new document with a generated ID
        db.collection("Plan")
                .add(Plan)
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
                    }
                });
        create_plan_callback.create_Plan_Callback();
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

    public void Create_Friend(Create_Friends_Callback create_friends_callback, String _userId, String Friend_id, String Friend_Name) {
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


        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId + " Friend_Id = " + Friend_id + " Friend_Name = " + Friend_Name);
        // Add a new document with a generated ID
        db.collection("User1_Friend")
                .add(User1_Friend)
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
                    }
                });
        // 유저 B
        Map<String, Object> User2_Friend = new HashMap<>();
        User2_Friend.put("userId", Friend_id);
        User2_Friend.put("Friend_Id", _userId);


        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId + " Friend_Id = " + Friend_id + " Friend_Name = " + Friend_Name);
        // Add a new document with a generated ID
        db.collection("User2_Friend")
                .add(User2_Friend)
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
                    }
                });

        create_friends_callback.create_Friends_Callback();

    }


    public void login(MyCallback myCallback) {

        db.collection("User")
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

        db.collection("Area")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if (document.getData().get("userId").equals(_id)) {

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
    public void get_Area_info(Get_Area_info_onCallback get_area_info_onCallback, String _id) {
        ArrayList<Area_Data> Area_Data_list = new ArrayList<>();

        db.collection("Area")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if (document.getData().get("userId").equals(_id)) {

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_Area_info 조회 성공!!. 로그인 아이디 =" + _id);
                                    String AreaName = document.getData().get("AreaName").toString();
                                    String AreaLatitude = document.getData().get("AreaLatitude").toString();
                                    String AreaLongitude = document.getData().get("AreaLongitude").toString();
                                    Area_Data_list.add(new Area_Data(AreaName, AreaLatitude, AreaLongitude));
                                }

                            }

                            get_area_info_onCallback.get_Area_info_onCallback(Area_Data_list);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

    }
    public void get_daily_info(Create_Chart_view_daily chart_view_daily, String userId, int numberOfDay, CalendarDay today_date){
        String today = today_date.toString();


        String[] parsedDATA = DATE.split("[{]"); // ex : [0] = Calender || [1] = 2021-02-28}

        parsedDATA = parsedDATA[1].split("[}]"); // ex : [0] = 2021-02-28 || [1] = ""

        parsedDATA = parsedDATA[0].split("-"); // ex : [0] = 2021 || [1] = 02 || [2] = 28


        year = Integer.parseInt(parsedDATA[0]);

        month = Integer.parseInt(parsedDATA[1])+1;

        day = Integer.parseInt(parsedDATA[2]);
        ArrayList<daily_data> chart_view_daily_list = new ArrayList<>();

    }



    public void get_Search_Somebody(Get_Search_Somebody_onCallback get_search_somebody_onCallback, String insert_text, String userId, Context con) {
        ArrayList<Search_Friend_Data> Search_Friend_Data_list = new ArrayList<>();
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.v("입력한 아이디 = ", "id = " + userId );
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                Log.v("파이어------", "" + document.getData().get("userId").toString());
                                if((document.getData().get("userId").toString().contains(insert_text) || document.getData().get("userName").toString().contains(insert_text))){


                                    if(!(document.getData().get("userId").toString().equals(userId))){
                                        Log.v("파이어베이스 성공!", "파이어베이스 Get_Area_info 조회 성공!!. 로그인 아이디 =" + userId);
                                        String FriendId = document.getData().get("userId").toString();
                                        String FriendName = document.getData().get("userName").toString();
                                        Search_Friend_Data_list.add(new Search_Friend_Data(FriendId, FriendName));
                                    }

                                }

                            }

                            Log.v("여기시작", ":!@#!@#" + Search_Friend_Data_list.size());
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
                                if((document.getData().get("userId").equals(userId))){

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_Area_info 조회 성공!!. 로그인 아이디 =" + userId);

                                    String FriendId = document.getData().get("Friend_Id").toString();
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
        String year = DATE[0];
        String month = Integer.toString(Integer.parseInt(DATE[1]) + 1);
        String day = DATE[2];


        db.collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.v("파이어asdf", "" + document.getData());
                                Log.v("사용자가 입력한 날짜 = ", "userId=" + _userId+"\n Plan_Year="+year+"\nPlan_month=" + month + "\nPlan_day="+day);

                                if(document.getData().get("userId").equals(_userId) && document.getData().get("Plan_Year").toString().equals(year) &&
                                        document.getData().get("Plan_Month").toString().equals(month)&& document.getData().get("Plan_Day").toString().equals(day)){

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


    public void Get_daily_details_info(Get_Daily_Detail_info get_daily_detail_info, String _userId, String[] DATE, Context con){


        get_daily_detail_info.get_Daily_details_onCallback(con);
    }
    public void Delete_Area(Delete_Area_Callback delete_area_callback, String _name, String _latitude, String _longitude, String userId){
        db.collection("Area")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if(document.getData().get("userId").equals(userId) && document.getData().get("AreaLatitude").equals(_latitude) && document.getData().get("AreaLongitude").equals(_longitude)&& document.getData().get("AreaName").equals(_name)){
                                    String documentpath = document.getId();

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_plan_details_info 조회 성공!!");
                                    db.collection("Area").document(documentpath)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });


                                }

                            }

                            delete_area_callback.delete_Area_Callback(_name,  _latitude, _longitude);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    public void Delete_Friend(Delete_Friend_Callback delete_friend_callback, String userId, String Friend_id) {
        db.collection("User1_Friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if(document.getData().get("userId").equals(userId) && document.getData().get("Friend_Id").equals(Friend_id)){
                                    String documentpath = document.getId();

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_plan_details_info 조회 성공!!");
                                    db.collection("User1_Friend").document(documentpath)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });


                                }

                            }


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

        //친구 B목록 삭제
        db.collection("User2_Friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if(document.getData().get("userId").equals(Friend_id) && document.getData().get("Friend_Id").equals(userId)){
                                    String documentpath = document.getId();

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_plan_details_info 조회 성공!!");
                                    db.collection("User2_Friend").document(documentpath)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });


                                }

                            }
                            delete_friend_callback.delete_Friend_Callback();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

    }

    public void Delete_Plan_Details(Delete_Plan_Callback delete_plan_callback, String _userId, String Plan_Details_name) {
        db.collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if(document.getData().get("userId").equals(_userId) && document.getData().get("Plan_Name").equals(Plan_Details_name)){
                                    String documentpath = document.getId();

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_plan_details_info 조회 성공!!");
                                    db.collection("Plan").document(documentpath)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });


                                }

                            }
                            delete_plan_callback.delete_Plan_Callback();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    public void Modify_plan_detail(Modify_Plan_Callback modify_plan_callback,String _userId, int _year, int _month, int _day, String _modify_plan_name, int executing_hour, int executing_minute, String before_planName) {
        db.collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("modify plan", "실행 중");
                                if(document.getData().get("userId").equals(_userId) && document.getData().get("Plan_Name").equals(before_planName)){
                                    String documentpath = document.getId();
                                    Log.v("document path=", documentpath);
                                    db.collection("Plan").document(documentpath)
                                            .update(

                                                    "Plan_Year", _year,
                                                    "Plan_Month", _month,
                                                    "Plan_Day", _day,
                                                    "Executing_Hour", executing_hour,
                                                    "Executing_Minute", executing_minute,
                                                    "Plan_Name", _modify_plan_name
                                            );


                                }

                            }


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
        modify_plan_callback.modifyh_Plan_Callback();
    }

    /*

     */
}


