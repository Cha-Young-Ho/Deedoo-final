package com.example.deedo.callback;

import android.content.Context;

import com.example.deedo.inquiry_plan.Plan_details_Data;

import java.util.ArrayList;

public interface Get_Plan_Detail_info {
    void get_plan_details_onCallback(ArrayList<Plan_details_Data> plan_details_data_list, Context con);
}
