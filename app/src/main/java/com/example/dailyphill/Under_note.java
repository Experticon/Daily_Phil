package com.example.dailyphill;

public class Under_note {
        public String id, day, text;

        public Under_note()
        {

        }
        public Under_note(String id, String day, String text) {
            this.id = id;
            this.day = day;
            this.text = text;
        }
        public String getId() {return id;}
        public String getDay() {return day;}
        public String getText() {return text;}
}
