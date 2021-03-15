package com.example.deedo.daily;

public class daily_data {
    String daily_name;
    String second;

    public daily_data(String daily_name, String second) {
        this.daily_name = daily_name;
        this.second = second;
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
