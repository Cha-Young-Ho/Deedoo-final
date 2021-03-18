package com.example.deedo.inquiry_plan;

public class Plan_details_Data {

   private String plan_name;
   private int executing_hour;
   private int executing_minute;
   private String plan_tag;

    public Plan_details_Data(String plan_name, int executing_hour, int executing_minute, String plan_tag) {
        this.plan_name = plan_name;
        this.executing_hour = executing_hour;
        this.executing_minute = executing_minute;
        this.plan_tag = plan_tag;
    }

    public String getPlan_tag() {
        return plan_tag;
    }

    public void setPlan_tag(String plan_tag) {
        this.plan_tag = plan_tag;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public int getExecuting_hour() {
        return executing_hour;
    }

    public void setExecuting_hour(int executing_hour) {
        this.executing_hour = executing_hour;
    }

    public int getExecuting_minute() {
        return executing_minute;
    }

    public void setExecuting_minute(int executing_minute) {
        this.executing_minute = executing_minute;
    }
}
