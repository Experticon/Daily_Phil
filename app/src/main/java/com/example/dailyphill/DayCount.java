package com.example.dailyphill;

public class DayCount {
    public String id, day, text, showText, image;

    public DayCount()
    {

    }
    public DayCount(String id, String day, String text, String showText, String image) {
        this.id = id;
        this.day = day;
        this.text = text;
        this.showText = showText;
        this.image = image;
    }
    public String getId() { return id; }
    public String getDay() {return day;}
    public String getText() {return text;}
    public String getShowText() {return showText;}
    public String getImage() {return image;}
}
