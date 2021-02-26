package com.example.deedo;

public class Search_Friend_Data {

    private String Search_Friend_name;
    private String Search_Friend_id;

    public Search_Friend_Data(String search_Friend_id, String search_Friend_name) {
        Search_Friend_name = search_Friend_name;
        Search_Friend_id = search_Friend_id;
    }

    public String getSearch_Friend_name() {
        return Search_Friend_name;
    }

    public void setSearch_Friend_name(String search_Friend_name) {
        Search_Friend_name = search_Friend_name;
    }

    public String getSearch_Friend_id() {
        return Search_Friend_id;
    }

    public void setSearch_Friend_id(String search_Friend_id) {
        Search_Friend_id = search_Friend_id;
    }
}
