package com.example.deedo.callback;

import android.content.Context;

import com.example.deedo.daily.daily_data;

import java.util.ArrayList;

public interface Get_Daily_Detail_info {
    void get_Daily_details_onCallback(ArrayList<daily_data> daily_data_list, Context con);
}
