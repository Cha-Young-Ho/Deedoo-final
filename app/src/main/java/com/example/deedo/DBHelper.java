package com.example.deedo;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "example3.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //데이터 베이스 생성될 때 호출


        db.execSQL("CREATE TABLE IF NOT EXISTS User (userId VARCHAR(20) PRIMARY KEY NOT NULL, userPassword varchar(20) NOT NULL, userName varchar(10) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Area (userId VARCHAR(20) NOT NULL,  AreaName VARCHAR(30) NOT NULL, AreaLatitude VARCHAR(15) NOT NULL, AreaLongitude VARCHAR(15) NOT NULL) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS Friend (User1 VARCHAR(20), User2 VARCHAR(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS Request_Friend (userId VARCHAR(20),Request_UserId VARCHAR(20), Request BOOLEAN)");

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

    public void Delete_Area(String _name, String _latitude, String _longitude) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.execSQL("DELETE FROM Area WHERE AreaName = '" + _name + "' AND AreaLatitude = '" + _latitude + "' AND AreaLongitude = '" + _longitude + "'");
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
            Log.v("친구 딜리트성공", "성공!");
        } catch (Exception e) {
            Log.v("친구 딜리트실패", "실패!");
        }
    }

    public ArrayList<Search_Friend_Data> get_Search_Somebody(String insert_text, String userId) {
        ArrayList<Search_Friend_Data> Search_Friend_Data_list = new ArrayList<>();


        SQLiteDatabase db = getWritableDatabase();

        //여기서 검색어를 id, name이랑 비교하도록 해야함---------------------------------
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE userId = '" + insert_text + "' OR userName = '" + insert_text + "' NOT userId IN ('"+ userId+"')" , null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String FriendName = cursor.getString(cursor.getColumnIndex("userId"));
                String FriendId = cursor.getString(cursor.getColumnIndex("userName"));


                Search_Friend_Data_list.add(new Search_Friend_Data(FriendId, FriendName));
            }

        } else {
            Log.v("실패", "cursor.getCount = 0");
        }

        cursor.close();
        return Search_Friend_Data_list;
    }


}

