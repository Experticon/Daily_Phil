package com.example.dailyphill;

public class User_day {
    public String id = "", key = "", day = "", showText = "";

    public User_day(String id, String key, String day, String showText) {
        this.id = id;
        this.key = key;
        this.day = day;
        this.showText = showText;
    }
    public String getId() { return id; }
    public String getKey() { return key; }
    public String getDay() {return day;}
    public String getShowText() {return showText;}
}
