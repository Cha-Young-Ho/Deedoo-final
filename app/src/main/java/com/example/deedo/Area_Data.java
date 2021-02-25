package com.example.deedo;

public class Area_Data {

    private String textView_id;
    private String textView_latitude;
    private String textView_longitude;

    public Area_Data(String textView_id, String textView_latitude, String textView_longitude) {
        this.textView_id = textView_id;
        this.textView_latitude = textView_latitude;
        this.textView_longitude = textView_longitude;
    }

    public String getTextView_id() {
        return textView_id;
    }

    public String getTextView_latitude() {
        return textView_latitude;
    }

    public String getTextView_longitude() {
        return textView_longitude;
    }

    public void setTextView_id(String textView_id) {
        this.textView_id = textView_id;
    }

    public void setTextView_latitude(String textView_latitude) {
        this.textView_latitude = textView_latitude;
    }

    public void setTextView_longitude(String textView_longitude) {
        this.textView_longitude = textView_longitude;
    }
}
