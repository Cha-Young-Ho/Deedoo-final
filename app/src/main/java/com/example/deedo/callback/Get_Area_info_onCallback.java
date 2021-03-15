package com.example.deedo.callback;

import android.content.Context;

import com.example.deedo.area.Area_Data;

import java.util.ArrayList;

public interface Get_Area_info_onCallback {
    void get_Area_info_onCallback(ArrayList<Area_Data> Area_Data_list, Context con);
    void get_Area_info_onCallback(ArrayList<Area_Data> Area_Data_list);

}


