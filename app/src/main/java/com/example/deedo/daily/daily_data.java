package com.example.deedo.daily;

public class daily_data {
    String daily_name;
    String second;
    String area_tag;

    public daily_data(String daily_name, String second, String area_tag) {
        this.daily_name = daily_name;
        this.second = second;
        this.area_tag = area_tag;
    }
    public daily_data(String area_tag, String second){
        this.area_tag = area_tag;
        this.second = second;
    }

    public String getArea_tag() {
        return area_tag;
    }

    public void setArea_tag(String area_tag) {
        this.area_tag = area_tag;
    }

    public String getDaily_name() {
        return daily_name;
    }

    public void setDaily_name(String daily_name) {
        this.daily_name = daily_name;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}
