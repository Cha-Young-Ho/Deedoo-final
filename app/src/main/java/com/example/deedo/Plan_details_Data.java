package com.example.deedo;

public class Plan_details_Data {

   private String plan_name;
   private int executing_time;

    public Plan_details_Data(String plan_name, int executing_time) {
        this.plan_name = plan_name;
        this.executing_time = executing_time;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public int getExecuting_time() {
        return executing_time;
    }

    public void setExecuting_time(int executing_time) {
        this.executing_time = executing_time;
    }
}
