package com.example.deedo;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Deedoo.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //데이터 베이스 생성될 때 호출


        db.execSQL("CREATE TABLE IF NOT EXISTS User (userId VARCHAR(20) PRIMARY KEY NOT NULL, userPassword varchar(20) NOT NULL, userName varchar(10) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Area (userId VARCHAR(20) PRIMARY KEY NOT NULL,  AreaName VARCHAR(30) NOT NULL, AreaLatitude VARCHAR(15) NOT NULL, AreaLogitude VARCHAR(15) NOT NULL, FOREIGN KEY (userId) REFERENCES User (userId)) ");

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

    public String insert_create_lotate(String _id, String _name, String _latitude, String _longitude){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO User (userId, userPassword, userName) VALUES('" + _id + "','" + _name + "','" + _latitude + "', '"+_longitude+"');");



        return "구역 입력 성공!";
    }


}

