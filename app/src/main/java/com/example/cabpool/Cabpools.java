package com.example.cabpool;

import java.text.SimpleDateFormat;

public class Cabpools{
    private String from,to,date,time;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");


    public Cabpools(String from, String to, String date, String time) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
