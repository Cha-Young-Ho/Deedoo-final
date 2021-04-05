package com.example.deedo.DB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.deedo.BarChart.BarChart_list_data;
import com.example.deedo.Friend.Modify_Friend_Data;
import com.example.deedo.Friend.Search_Friend_Data;
import com.example.deedo.area.Area_Data;
import com.example.deedo.callback.Calc_BarChart_data_Callback;
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
import com.example.deedo.callback.Get_period_list_Callback;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    public void create_daily(String _userId, Calendar today_date, String _daily_name, String _area_tag) {
        Map<String, Object> Daily = new HashMap<>();

        Daily.put("userId", _userId);
        Daily.put("기타", "0");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(today_date.getTime());


        DocumentReference docRef = db.collection("Daily").document("" + date + _userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        DocumentReference washingtonRef = db.collection("Daily").document("" + date + _userId);

                        washingtonRef.update(_area_tag, FieldValue.increment(10));
                    } else {
                        Log.d(TAG, "No such document");
                        db.collection("Daily").document(date + _userId)
                                .set(Daily)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        DocumentReference washingtonRef = db.collection("Daily").document("" + date + _userId);
                                        washingtonRef.update(_area_tag, FieldValue.increment(10));
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

    public void insert_create_Area(Create_Area_Callback create_area_callback, String _id, String _name, String _latitude, String _longitude, String _area_tag) {
        Map<String, Object> Area = new HashMap<>();
        Area.put("userId", _id);
        Area.put("AreaName", _name);
        Area.put("AreaLatitude", _latitude);
        Area.put("AreaLongitude", _longitude);
        Area.put("AreaTag", _area_tag);

        Log.v("파이어베이스 로테이트 인서트 시작", " id = " + _id + " AreaName = " + _name + " AreaLatitude = " + _latitude + " AreaLongitude " + _longitude);
        // Add a new document with a generated ID
        db.collection("Area")
                .add(Area)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        create_area_callback.create_Area_Callback(_name, _latitude, _longitude, _area_tag);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    public void create_plan_detail(Create_Plan_Callback create_plan_callback, String _userId, int _year,
                                   int _month, int _day, String _create_plan_name, int executing_hour, int executing_minute, String _plan_Tag) {
        String month;
        String day;
        Map<String, Object> Plan = new HashMap<>();
        Plan.put("userId", _userId);
        if (String.valueOf(_month).length() == 1) {
            month = "0" + _month;
        } else {
            month = "" + _month;
        }
        if (String.valueOf(_day).length() == 1) {
            day = "0" + _day;
        } else {
            day = "" + _day;
        }
        Plan.put("Plan_Year", _year);
        Plan.put("Plan_Month", month);
        Plan.put("Plan_Day", day);
        Plan.put("Plan_Name", _create_plan_name);
        Plan.put("Executing_Hour", executing_hour);
        Plan.put("Executing_Minute", executing_minute);
        Plan.put("Plan_Tag", _plan_Tag);

        Log.v("파이어베이스 플랜 인서트 시작", " id = " + _userId + " _year = " + _year + " _create_plan_name = " + _create_plan_name + " executing_minute " + executing_minute +
                "plan_tag = " + _plan_Tag);
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


    public void login(MyCallback myCallback, String userId, String userPassword) {

        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("userId").equals(userId) && document.getData().get("userPassword").equals(userPassword)) {


                                    match_id = userId;
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
                                    try {
                                        String Area_Tag = document.getData().get("AreaTag").toString();
                                        Area_Data_list.add(new Area_Data(AreaName, AreaLatitude, AreaLongitude, Area_Tag));
                                    } catch (Exception e) {
                                        String Area_Tag = document.getData().get("AreaTag").toString();
                                        Area_Data_list.add(new Area_Data(AreaName, AreaLatitude, AreaLongitude, "기타"));
                                    }

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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("@@파이어@@", "" + document.getData());

                                if (document.getData().get("userId").equals(_id)) {

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_Area_info 조회 성공!!. 로그인 아이디 =" + _id);
                                    String AreaName = document.getData().get("AreaName").toString();
                                    String AreaLatitude = document.getData().get("AreaLatitude").toString();
                                    String AreaLongitude = document.getData().get("AreaLongitude").toString();
                                    String Area_Tag = document.getData().get("AreaTag").toString();
                                    Area_Data_list.add(new Area_Data(AreaName, AreaLatitude, AreaLongitude, Area_Tag));
                                }

                            }

                            get_area_info_onCallback.get_Area_info_onCallback(Area_Data_list);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

    }

    public void get_daily_info(Create_Chart_view_daily chart_view_daily, String userId, int numberOfDay, Calendar today_date) {

        ArrayList<daily_data> chart_view_daily_list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //for (int i = 0; i < numberOfDay; i++) {
        // today_date.add(Calendar.DATE, -i);
        String date = sdf.format(today_date.getTime());
        Log.v("데이트 = ", date);
        DocumentReference docRef = db.collection("Daily").document("" + date + userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.v("짱짱짱", "WNWN");
                        if (document.getData().get("userId").equals(userId)) {
                            Log.v("르르르", "리리리");
                            String[] splitdata = document.getData().toString().replaceAll("\\{", "")
                                    .replaceAll("\\}", "").split("=");
                            for (int i = 0; i < splitdata.length; i++) {
                                Log.v("-1-", splitdata[i]);
                            }
                            String[] splitdata2 = (String.join("-", splitdata)).split(", ");
                            for (int i = 0; i < splitdata2.length; i++) {
                                Log.v("-2-", "" + splitdata2[i]);
                                String[] splitdata3 = splitdata2[i].split("-");
                                if ("운동식사근무공부휴식여가활동쇼핑집학교유흥기타활동".contains(splitdata3[0])) {
                                    Log.v("차트뷰에 추가 성공", "tagname = " + splitdata3[0] + " second = " + splitdata3[1]);
                                    String dailyname = splitdata3[0];
                                    String dailysecond = splitdata3[1];

                                    Log.v("데일리 리스트에 추가 = ", dailyname + dailysecond);
                                    chart_view_daily_list.add(new daily_data(dailyname, dailysecond));
                                }

                            }


                        }

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                chart_view_daily.create_Chart_view_daily(chart_view_daily_list);
            }
        });
        // }


    }


    public void get_Search_Somebody(Get_Search_Somebody_onCallback get_search_somebody_onCallback, String insert_text, String userId, Context con) {
        ArrayList<Search_Friend_Data> Search_Friend_Data_list = new ArrayList<>();
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.v("입력한 아이디 = ", "id = " + userId);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                Log.v("파이어------", "" + document.getData().get("userId").toString());
                                if ((document.getData().get("userId").toString().contains(insert_text) || document.getData().get("userName").toString().contains(insert_text))) {


                                    if (!(document.getData().get("userId").toString().equals(userId))) {
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

    public void Get_friend_info(Get_Friend_onCallback get_friend_onCallback, String userId, Context con) {
        ArrayList<Modify_Friend_Data> modify_friend_data_list = new ArrayList<>();

        db.collection("User1_Friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if ((document.getData().get("userId").equals(userId))) {

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
        String __month = Integer.toString(Integer.parseInt(DATE[1]) + 1);
        String month;

        if (__month.length() == 1) {
            month = "0" + __month;
        } else {
            month = __month;
        }

        String day;
        String __day = DATE[2];
        if (__day.length() == 1) {
            day = "0" + __day;
        } else {
            day = __day;
        }


        db.collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.v("파이어asdf", "" + document.getData());
                                Log.v("사용자가 입력한 날짜 = ", "userId=" + _userId + "\n Plan_Year=" + year + "\nPlan_month=" + month + "\nPlan_day=" + day);

                                Log.v("month", "get month = -" + document.getData().get("Plan_Month") +"- month = -" + month + "-");
                                Log.v("day", "get day = -" + document.getData().get("Plan_Day") +"- day = -" + day + "-");
                                if (document.getData().get("userId").equals(_userId) && document.getData().get("Plan_Year").toString().equals(year) &&
                                        document.getData().get("Plan_Month").toString().equals(month) && document.getData().get("Plan_Day").toString().equals(day)) {

                                    Log.v("파이어베이스 성공!", "파이어베이스 Get_plan_details_info 조회 성공!!");

                                    String plan_name = document.getData().get("Plan_Name").toString();
                                    int planExecuting_hour = Integer.parseInt(document.getData().get("Executing_Hour").toString());
                                    int planExecuting_minute = Integer.parseInt(document.getData().get("Executing_Minute").toString());
                                    String planExecuting_tag = document.getData().get("Plan_Tag").toString();
                                    plan_details_data_list.add(new Plan_details_Data(plan_name, planExecuting_hour, planExecuting_minute, planExecuting_tag));
                                }

                            }

                            get_plan_detail_info.get_plan_details_onCallback(plan_details_data_list, con);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }


    public void Get_daily_details_info(Get_Daily_Detail_info get_daily_detail_info, String _userId, String[] DATE, Context con) {

        ArrayList<daily_data> daily_details_data_list = new ArrayList<>();
        String today_date = null;

        String year = DATE[0];
        String __month = Integer.toString(Integer.parseInt(DATE[1]) + 1);
        String month;

        if (__month.length() == 1) {
            month = "0" + __month;
        } else {
            month = __month;
        }

        String day;
        String __day = DATE[2];
        if (__day.length() == 1) {
            day = "0" + __day;
        } else {
            day = __day;
        }

        for (int i = 0; i < DATE.length; i++) {
            Log.v("DATE", DATE[i]);
        }
        today_date = year + month + day;
        Log.v("get info daily에서 today", today_date);

        DocumentReference docRef = db.collection("Daily").document("" + today_date + _userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getData().get("userId").equals(_userId)) {
                            Log.v("getdailyinfo 진입", "--===============================================--");
                            String[] splitdata = document.getData().toString().replaceAll("\\{", "")
                                    .replaceAll("\\}", "").split("=");
                            for (int i = 0; i < splitdata.length; i++) {
                                Log.v("-1-", splitdata[i]);
                            }
                            String[] splitdata2 = (String.join("-", splitdata)).split(", ");
                            for (int i = 0; i < splitdata2.length; i++) {
                                Log.v("-2-", "" + splitdata2[i]);
                                String[] splitdata3 = splitdata2[i].split("-");
                                if ("운동-식사-근무-공부-휴식-여가활동-쇼핑-집-학교-유흥-기타활동".contains(splitdata3[0])) {
                                    Log.v("차트뷰에 추가 성공", "tagname = " + splitdata3[0] + " second = " + splitdata3[1]);
                                    String dailytag = splitdata3[0];
                                    String dailysecond = splitdata3[1];

                                    Log.v("데일리 리스트에 추가 = ", dailytag + dailysecond);
                                    daily_details_data_list.add(new daily_data(dailytag, dailysecond));
                                }

                            }


                        }

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                Log.v("지금 확인해야하는 리스트 길이= ", "" + daily_details_data_list.size());
                get_daily_detail_info.get_Daily_details_onCallback(daily_details_data_list, con);
            }

        });

    }

    public void Delete_Area(Delete_Area_Callback delete_area_callback, String _name, String _latitude, String _longitude, String userId) {
        db.collection("Area")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("파이어", "" + document.getData());
                                if (document.getData().get("userId").equals(userId) && document.getData().get("AreaLatitude").equals(_latitude) && document.getData().get("AreaLongitude").equals(_longitude) && document.getData().get("AreaName").equals(_name)) {
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

                            delete_area_callback.delete_Area_Callback(_name, _latitude, _longitude);
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
                                if (document.getData().get("userId").equals(userId) && document.getData().get("Friend_Id").equals(Friend_id)) {
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
                                if (document.getData().get("userId").equals(Friend_id) && document.getData().get("Friend_Id").equals(userId)) {
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
                                if (document.getData().get("userId").equals(_userId) && document.getData().get("Plan_Name").equals(Plan_Details_name)) {
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

    public void Modify_plan_detail(Modify_Plan_Callback modify_plan_callback, String _userId, int _year, int _month, int _day, String _modify_plan_name, int executing_hour, int executing_minute, String before_planName) {
        db.collection("Plan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("modify plan", "실행 중");
                                if (document.getData().get("userId").equals(_userId) && document.getData().get("Plan_Name").equals(before_planName)) {
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

    public void Compare_daily(Calc_BarChart_data_Callback calc_barChart_data_callback, String today_date, String userId, String friend_Id, String year, String month, String day) {

        ArrayList<BarChart_list_data> barChart_list_data = new ArrayList<>();
        ArrayList<BarChart_list_data> barChart_list_friend_data = new ArrayList<>();

        DocumentReference docRef = db.collection("Daily").document("" + today_date + userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.v("짱짱짱", "WNWN");
                        if (document.getData().get("userId").equals(userId)) {
                            Log.v("르르르", "리리리");
                            String[] splitdata = document.getData().toString().replaceAll("\\{", "")
                                    .replaceAll("\\}", "").split("=");
                            for (int i = 0; i < splitdata.length; i++) {
                                Log.v("-1-", splitdata[i]);
                            }
                            String[] splitdata2 = (String.join("-", splitdata)).split(", ");
                            for (int i = 0; i < splitdata2.length; i++) {
                                Log.v("-2-", "" + splitdata2[i]);
                                String[] splitdata3 = splitdata2[i].split("-");
                                if ("운동-식사-근무-공부-휴식-여가활동-쇼핑-집-학교-유흥-기타활동".contains(splitdata3[0])) {
                                    Log.v("차트뷰에 추가 성공", "tagname = " + splitdata3[0] + " second = " + splitdata3[1]);
                                    String dailytag = splitdata3[0];
                                    String dailysecond = splitdata3[1];

                                    Log.v("데일리 리스트에 추가 = ", dailytag + dailysecond);
                                    barChart_list_data.add(new BarChart_list_data(dailytag, dailysecond));
                                }

                            }


                        }

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                //plan 담기
                db.collection("Plan")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getData().get("userId").equals(friend_Id) && document.getData().get("Plan_Year").toString().equals(year) &&
                                                document.getData().get("Plan_Month").toString().equals(month) && document.getData().get("Plan_Day").toString().equals(day)) {

                                            String plan_name = document.getData().get("Plan_Name").toString();
                                            int planExecuting_hour = Integer.parseInt(document.getData().get("Executing_Hour").toString());
                                            int planExecuting_minute = Integer.parseInt(document.getData().get("Executing_Minute").toString());
                                            String plan_Executing_time = String.valueOf(((planExecuting_hour * 60) + planExecuting_minute) * 60);
                                            String planExecuting_tag = document.getData().get("Plan_Tag").toString();
                                            barChart_list_friend_data.add(new BarChart_list_data(planExecuting_tag, plan_Executing_time));
                                            Log.v("추가한 내용 = ", "tag = " + planExecuting_tag + " time = " + plan_Executing_time);
                                        }


                                    }

                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }

                                calc_barChart_data_callback.calc_BarChart_data_Callback(barChart_list_data, barChart_list_friend_data);
                            }

                        });
            }
        });
    }

    public void Compare_friend(Calc_BarChart_data_Callback calc_barChart_data_callback, String today_date, String userId, String friendId) {

        ArrayList<BarChart_list_data> barChart_list_data = new ArrayList<>();
        ArrayList<BarChart_list_data> barChart_list_friend_data = new ArrayList<>();
        Log.v("여기서 확인해야함", today_date + userId);
        DocumentReference docRef = db.collection("Daily").document(today_date + userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.v("짱짱짱", "WNWN");
                        if (document.getData().get("userId").equals(userId)) {
                            Log.v("르르르", "리리리");
                            String[] splitdata = document.getData().toString().replaceAll("\\{", "")
                                    .replaceAll("\\}", "").split("=");
                            for (int i = 0; i < splitdata.length; i++) {
                                Log.v("-1-", splitdata[i]);
                            }
                            String[] splitdata2 = (String.join("-", splitdata)).split(", ");
                            for (int i = 0; i < splitdata2.length; i++) {
                                Log.v("-2-", "" + splitdata2[i]);
                                String[] splitdata3 = splitdata2[i].split("-");
                                if ("운동-식사-근무-공부-휴식-여가활동-쇼핑-집-학교-유흥-기타활동".contains(splitdata3[0])) {
                                    Log.v("차트뷰에 추가 성공", "tagname = " + splitdata3[0] + " second = " + splitdata3[1]);
                                    String dailytag = splitdata3[0];
                                    String dailysecond = splitdata3[1];

                                    Log.v("데일리 리스트에 추가 = ", dailytag + dailysecond);
                                    barChart_list_data.add(new BarChart_list_data(dailytag, dailysecond));
                                }

                            }


                        }

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

                ////
                DocumentReference docRef = db.collection("Daily").document("" + today_date + friendId);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.v("짱짱짱", "WNWN");
                                if (document.getData().get("userId").equals(userId)) {
                                    Log.v("르르르", "리리리");
                                    String[] splitdata = document.getData().toString().replaceAll("\\{", "")
                                            .replaceAll("\\}", "").split("=");
                                    for (int i = 0; i < splitdata.length; i++) {
                                        Log.v("-1-", splitdata[i]);
                                    }
                                    String[] splitdata2 = (String.join("-", splitdata)).split(", ");
                                    for (int i = 0; i < splitdata2.length; i++) {
                                        Log.v("-2-", "" + splitdata2[i]);
                                        String[] splitdata3 = splitdata2[i].split("-");
                                        if ("운동-식사-근무-공부-휴식-여가활동-쇼핑-집-학교-유흥-기타활동".contains(splitdata3[0])) {
                                            Log.v("차트뷰에 추가 성공", "tagname = " + splitdata3[0] + " second = " + splitdata3[1]);
                                            String dailytag = splitdata3[0];
                                            String dailysecond = splitdata3[1];

                                            Log.v("데일리 리스트에 추가 = ", dailytag + dailysecond);
                                            barChart_list_friend_data.add(new BarChart_list_data(dailytag, dailysecond));
                                        }

                                    }


                                }

                            } else {
                                Log.d(TAG, "No such document");

                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                        ////


                    }

                });
                        calc_barChart_data_callback.calc_BarChart_data_Callback(barChart_list_data, barChart_list_friend_data);
                    }

                });


    }

    public void Get_Period_Daily(Get_period_list_Callback get_period_list_callback, String userId, int period) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //for (int i = 0; i < numberOfDay; i++) {
        // today_date.add(Calendar.DATE, -i);
        Log.v("period =", "" + period);


        String[] tag_name = {"운동", "식사", "근무", "공부", "휴식", "여가활동", "쇼핑", "집", "학교", "유흥", "기타활동", "기타"};
        ArrayList<daily_data> period_data_list = new ArrayList<>();
        for (int i = 0; i < tag_name.length; i++) {
            period_data_list.add(new daily_data(tag_name[i], "" + 0));
        }


        db.collection("Daily")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v("\n가져온 document id =", document.getId() + "\n");
                                for (int i = 0; i < period; i++) {
                                    Calendar date = Calendar.getInstance();
                                    if (period == 365) {
                                        date.add(Calendar.DATE, -364);

                                    } else if (period == 180) {
                                        date.add(Calendar.DATE, -179);
                                    } else if (period == 90) {
                                        date.add(Calendar.DATE, -89);
                                    } else if (period == 30) {
                                        date.add(Calendar.DATE, -29);
                                    } else if (period == 7) {
                                        date.add(Calendar.DATE, -6);
                                    }

                                    date.add(Calendar.DAY_OF_MONTH, i);
                                    String today_date = sdf.format(date.getTime());
                                    Log.v("비교하는 id = ", today_date + userId+"\n");
                                    if (document.getId().equals(today_date + userId)) {
                                        Log.v("매칭성공", "시작한 이름=" + today_date+userId);
                                        Log.v("가져온 데이터 보자 = ", document.getData().toString());

                                        Log.v("keyvalue=", "" + document.getData().entrySet());
                                        Set<Map.Entry<String, Object>> hashset = document.getData().entrySet();


                                        Log.v("hashset size = ", "" + hashset.size());

                                        Iterator<Map.Entry<String, Object>> entries = hashset.iterator();

                                        while (entries.hasNext()) {

                                            Map.Entry<String, Object> entry = entries.next();

                                            System.out.println("key : " + entry.getKey() + " , value : " + entry.getValue());

                                            if (entry.getKey().equals("운동")) {
                                                period_data_list.set(0, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(0).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue() )))));
                                            } else if (entry.getKey().equals("식사")) {
                                                period_data_list.set(1, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(1).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue() )))));
                                            } else if (entry.getKey().equals("근무")) {
                                                period_data_list.set(2, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(2).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("공부")) {
                                                period_data_list.set(3, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(3).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("휴식")) {
                                                period_data_list.set(4, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(4).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("여가활동")) {
                                                period_data_list.set(5, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(5).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("쇼핑")) {
                                                period_data_list.set(6, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(6).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("집")) {
                                                period_data_list.set(7, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(7).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("학교")) {
                                                period_data_list.set(8, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(8).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("유흥")) {
                                                period_data_list.set(9, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(9).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("기타활동")) {
                                                period_data_list.set(10, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(10).getSecond()) +Integer.valueOf(String.valueOf(entry.getValue())))));
                                            } else if (entry.getKey().equals("기타")) {
                                                period_data_list.set(11, new daily_data(entry.getKey(),
                                                        String.valueOf(Integer.parseInt(period_data_list.get(11).getSecond()) + Integer.valueOf(String.valueOf(entry.getValue())))));
                                            }

                                        }





                                        Log.v("iter이후", "");
                                        for (int j = 0; j < period_data_list.size(); j++) {
                                            Log.v("tag = ", period_data_list.get(j).getArea_tag());
                                            Log.d("second = ", period_data_list.get(j).getSecond());
                                        }
                                    }
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        get_period_list_callback.get_period_list_Callback(period_data_list);
                    }
                });


    }

}




