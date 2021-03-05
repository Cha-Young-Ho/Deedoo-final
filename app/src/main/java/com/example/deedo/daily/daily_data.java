package com.example.deedo.daily;

public class daily_data {
    String daily_name;
    int hour;
    int minute;

    public daily_data(String daily_name, int hour, int minute) {
        this.daily_name = daily_name;
        this.hour = hour;
        this.minute = minute;
    }

    public String getDaily_name() {
        return daily_name;
    }

    public void setDaily_name(String daily_name) {
        this.daily_name = daily_name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
