package com.example.deedo.DB;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.deedo.Friend.Modify_Friend_Data;
import com.example.deedo.Friend.Search_Friend_Data;
import com.example.deedo.area.Area_Data;
import com.example.deedo.daily.daily_data;
import com.example.deedo.inquiry_plan.Plan_details_Data;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "example7.db";
    /*
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    // Read from the database
myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value = dataSnapshot.getValue(String.class);
            Log.d(TAG, "Value is: " + value);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    });

     */
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //데이터 베이스 생성될 때 호출

        db.execSQL("CREATE TABLE IF NOT EXISTS User (userId VARCHAR(20) PRIMARY KEY NOT NULL, userPassword varchar(20) NOT NULL, userName varchar(10) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Area (userId VARCHAR(20) NOT NULL,  AreaName VARCHAR(30) NOT NULL, AreaLatitude VARCHAR(15) NOT NULL, AreaLongitude VARCHAR(15) NOT NULL) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS Friend (User1 VARCHAR(20), User2 VARCHAR(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS Daily (userId VARCHAR(20) NOT NULL,  DailyName Varchar(100) NOT NULL, StayDate_year int, StayDate_month int, StayDate_day int, Stay_Time_hour int, Stay_Time_minute)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Planner (userId VARCHAR(20) NOT NULL,  PlanName varchar(100) NOT NULL, PlanDate_year int, PlanDate_month int, PlanDate_day int, PlanExecuting_hour int, PlanExecuting_minute int)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        onCreate(db);
    }


    //회원가입 insert
    public boolean Insert_User_Data(String _id, String _password, String _name) {
        SQLiteDatabase db = getWritableDatabase();
        //INSERT INTO User (userId, userPassword, userName) VALUES(_id ,_password+,_name);

        try {
            db.execSQL("INSERT INTO User (userId, userPassword, userName) VALUES('" + _id + "','" + _password + "','" + _name + "');");

            return true;
        } catch (Exception e) {


            return false;

        }


    }

    public ArrayList<String> getUserinfo() {
        ArrayList<String> userinfo = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("userId"));
                String password = cursor.getString(cursor.getColumnIndex("userPassword"));
                String name = cursor.getString(cursor.getColumnIndex("userName"));

                userinfo.add(id);
                userinfo.add(password);
                userinfo.add(name);
            }

        } else {
            Log.v("실패", "cursor.getCount = 0");
        }
        cursor.close();
        return userinfo;

    }

    ;

    //로그인 비교
    public String login(String _id, String _password) {
        SQLiteDatabase db = getWritableDatabase();
        String match_id = "";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM User WHERE userId = '" + _id + "'  ", null);
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {

                cursor.close();
                return match_id;

            } else {
                String password = cursor.getString(cursor.getColumnIndex("userPassword"));
                String id = cursor.getString(cursor.getColumnIndex("userId"));
                if (password.equals(_password)) {

                    cursor.close();
                    Log.v("로그인 ID = ", "" + id);
                    return id;
                } else {

                    cursor.close();
                    return match_id;

                }

            }
        } catch (Exception e) {
            e.getStackTrace();

            return match_id;
        }

    }

    public void insert_create_lotate(String _id, String _name, String _latitude, String _longitude) {
        SQLiteDatabase db = getWritableDatabase();

        try {

            db.execSQL("INSERT INTO Area (userId, AreaName, AreaLatitude, AreaLongitude) VALUES('" + _id + "','" + _name + "','" + _latitude + "', '" + _longitude + "');");

            Log.v("인서트 성공", "인서트 성공");
            Log.v("id =", "" + _id);
        } catch (Exception e) {
            Log.v("인서트 실패", "인서트 실패");
        }


    }

    public void delete_table() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE Area");
    }


    public ArrayList<Area_Data> get_Area_info(String userId) {

        ArrayList<Area_Data> Area_Data_list = new ArrayList<>();


        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Area WHERE userId = '" + userId + "'", null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String AreaName = cursor.getString(cursor.getColumnIndex("AreaName"));
                String latitude = cursor.getString(cursor.getColumnIndex("AreaLatitude"));
                String longitude = cursor.getString(cursor.getColumnIndex("AreaLongitude"));

                Area_Data_list.add(new Area_Data(AreaName, latitude, longitude));
            }

        } else {
            Log.v("실패", "cursor.getCount = 0");
        }
        Log.v("userid = ", userId);
        cursor.close();
        return Area_Data_list;

    }

    public void Delete_Area(String _name, String _latitude, String _longitude, String userId) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.execSQL("DELETE FROM Area WHERE AreaName = '" + _name + "' AND AreaLatitude = '" + _latitude + "' AND AreaLongitude = '" + _longitude + "' AND userId = '" + userId + "'");
            Log.v("딜리트성공", "성공!");
        } catch (Exception e) {
            Log.v("딜리트실패", "실패!");
        }


    }


    public void Delete_Friend(String userId, String Friend_id) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.execSQL("DELETE FROM Friend WHERE User1 = '" + userId + "' AND User2 = '" + Friend_id + "'");
            db.execSQL("DELETE FROM Friend WHERE User1 = '" + Friend_id + "' AND User2 = '" + userId + "'");

            Log.v("딜리트 정보 보기 = ", "userId = " + userId + " Friend_id = " + Friend_id);
            Log.v("친구 딜리트성공", "성공!");
        } catch (Exception e) {
            Log.v("친구 딜리트실패", "실패!");
        }
    }

    public ArrayList<Search_Friend_Data> get_Search_Somebody(String insert_text, String userId) {
        ArrayList<Search_Friend_Data> Search_Friend_Data_list = new ArrayList<>();


        SQLiteDatabase db = getWritableDatabase();

        //친구 테이블의 User2에 등록되지 않고(친구 관계가 아닌), 자신의 아이디가 아니고, 이름과 id가 검색어와 일치하는 정보를 가져와라
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE (userId = '" + insert_text + "' OR userName = '" + insert_text + "') AND NOT userId IN ('" + userId + "') ", null);


        Log.v("db-입력한 검색어", "" + insert_text);
        Log.v("db- 입력한 사용자 id", "" + userId);
        Log.v("커서 카운터 수 = ", "" + cursor.getCount());
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String FriendName = cursor.getString(cursor.getColumnIndex("userName"));
                String FriendId = cursor.getString(cursor.getColumnIndex("userId"));


                Search_Friend_Data_list.add(new Search_Friend_Data(FriendId, FriendName));
            }

        } else {
            Log.v("실패", "cursor.getCount = 0");
        }

        cursor.close();
        return Search_Friend_Data_list;
    }

    public void Request_Friend(String _userId, String Friend_id, String Friend_Name) {

        SQLiteDatabase db = getWritableDatabase();
        try {

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
            db.execSQL("INSERT INTO Friend (User1, User2) VALUES('" + _userId + "','" + Friend_id + "');");
            db.execSQL("INSERT INTO Friend (User1, User2) VALUES('" + Friend_id + "','" + _userId + "');");

            Log.v("친구 요청 인서트 성공!", "사용자 아이디 = " + _userId + "- 친구 아이디 = " + Friend_id);
        } catch (Exception e) {
            Log.v("친구 요청 인서트 실패!", "사용자 아이디 = " + _userId + "- 친구 아이디 = " + Friend_id);
        }


    }

    public ArrayList<Modify_Friend_Data> get_friend_info(String userId) {
        ArrayList<Modify_Friend_Data> modify_friend_data_list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        // Cursor cursor = db.rawQuery("SELECT * FROM User WHERE userId = (SELECT User2 FROM Friend WHERE User1 = '" + userId + "')", null);
        Cursor cursor = db.rawQuery("SELECT User2 FROM Friend WHERE User1 = '" + userId + "'", null);
        Log.v("커서 카운터 수 = ", "" + cursor.getCount());


        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {


                String friend_id1 = cursor.getString(cursor.getColumnIndex("User2"));

                Cursor cursor2 = db.rawQuery("SELECT * FROM User WHERE userId = '" + friend_id1 + "'", null);
                Log.v("커서 카운터 수 = ", "" + cursor2.getCount());
                if (cursor2.getCount() != 0) {
                    while (cursor2.moveToNext()) {
                        String friend_Name2 = cursor2.getString(cursor2.getColumnIndex("userName"));
                        String friend_id2 = cursor2.getString(cursor2.getColumnIndex("userId"));

                        modify_friend_data_list.add(new Modify_Friend_Data(friend_id2, friend_Name2));
                    }
                }
            }

        } else {
            Log.v("실패", "cursor.getCount = 0");
        }
        Log.v("userid = ", userId);
        cursor.close();


        return modify_friend_data_list;
    }



    public void Delete_Plan_Details(String _userId, String Plan_Details_name) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.execSQL("DELETE FROM Planner WHERE userId = '" + _userId + "' AND PlanName = '" + Plan_Details_name + "'");


        } catch (Exception e) {
            Log.v("plan 딜리트실패", "실패!");
        }


    }

    public void Modify_plan_detail(String _userId, int _year, int _month, int _day, String _modify_plan_name, int executing_hour, int executing_minute, String before_planName) {
        SQLiteDatabase db = getWritableDatabase();

        String _date = _year + "-" + _month + "-" + _day;

        try {
            db.execSQL("UPDATE Planner " +
                    "Set PlanName = '" + _modify_plan_name + "', " +
                    "PlanExecuting_hour = '" + executing_hour + "', " +
                    "PlanExecuting_minute = '" + executing_minute + "' " +
                    "WHERE PlanName = '" + before_planName + "' " +
                    "AND PlanDate_year = '" + _year + "'" +
                    "AND PlanDate_month = '"+_month+"'" +
                    "AND PlanDate_day = '"+_day+"'" +
                    "AND userId ='"+_userId+"'" );
            Log.v("plan details modify!", "성공");
        } catch (Exception e) {


            Log.v("plan details modify!", "실패");
        }

    }

    public void create_plan_detail(String _userId, int _year, int _month, int _day, String _create_plan_name, int executing_hour, int executing_minute) {
        SQLiteDatabase db = getWritableDatabase();
        String plan_date = _year + "-" + _month + "-" + _day;
        try {

            db.execSQL("INSERT INTO Planner (userId, PlanName, PlanDate_year, PlanDate_month, PlanDate_day, PlanExecuting_hour, PlanExecuting_minute) " +
                    "VALUES('" + _userId + "','" + _create_plan_name + "', '" + _year + "', '"+_month+"', '"+_day+"', '" + executing_hour + "', '" + executing_minute + "');");
            Log.v("create plan insert 성공!", "아이디 = " + _userId + " planname = " + _create_plan_name +
                    " year = " + _year + " month = " + _month + " day = " + _day + " executing_hour =" + executing_hour +
                    " executing_minute = " + executing_minute);

        } catch (Exception e) {
            Log.v("create plan insert 실패!", "아이디 = " + _userId + " planname = " + _create_plan_name +
                    " year = " + _year + " month = " + _month + " day = " + _day + " executing_hour =" + executing_hour +
                    " executing_minute = " + executing_minute);
        }
    }


    public ArrayList<Plan_details_Data> get_plan_details_info(String _userId, String[] DATE) {

        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Plan_details_Data> plan_details_data_list = new ArrayList<>();
        int year = Integer.parseInt(DATE[0]);
        int month = Integer.parseInt(DATE[1]) + 1;
        int day = Integer.parseInt(DATE[2]);

        Cursor cursor = db.rawQuery("SELECT * FROM Planner WHERE userId = '" + _userId + "' AND PlanDate_year = '"+year+"' AND PlanDate_month = '"+month+"' AND PlanDate_day = '"+day+"'", null);



        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String plan_name = cursor.getString(cursor.getColumnIndex("PlanName"));
                int planExecuting_hour = cursor.getInt(cursor.getColumnIndex("PlanExecuting_hour"));
                int planExecuting_minute = cursor.getInt(cursor.getColumnIndex("PlanExecuting_minute"));

                plan_details_data_list.add(new Plan_details_Data(plan_name, planExecuting_hour, planExecuting_minute));

            }

        } else {
            Log.v("실패", "cursor.getCount = 0");
        }
        Log.v("userid = ", _userId);


        cursor.close();

         
        return plan_details_data_list;
    }

    public ArrayList<daily_data> get_daily_info(int date_amount, int year, int month, int day){
        ArrayList<daily_data> daily_data_list = null;

        // 날짜 사이로
        return daily_data_list;
    }

}

