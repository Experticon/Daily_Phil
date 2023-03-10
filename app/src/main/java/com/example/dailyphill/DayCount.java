package com.example.dailyphill;

public class DayCount {
    public String id, day, text, showText;

    public DayCount( ) {

    }
    public DayCount(String id, String day, String text, String showText) {
        this.id = id;
        this.day = day;
        this.text = text;
        this.showText = showText;
    }
    //public getText(String text) {
    //    this.text = text;
    //}
    public String getId() { return id; }
    public String getDay() {return day;}
    public String getText() {return text;}
    public String getShowText() {return showText;}
}
