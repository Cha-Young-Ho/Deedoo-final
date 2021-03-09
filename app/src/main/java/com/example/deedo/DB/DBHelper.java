package com.example.deedo.DB;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.deedo.callback.MyCallback;
import com.example.deedo.inquiry_plan.Plan_details_Data;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    String match_id = "";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "example8.db";
    DBHelperFirebase dbHelperFirebase = new DBHelperFirebase();
    MyCallback myCallback;
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
    public ArrayList<Plan_details_Data> get_plan_details_info(String _userId, String[] DATE) {

        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Plan_details_Data> plan_details_Data_list;


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



}

