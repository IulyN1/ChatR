package com.example.chatr.controllers;

public class UserTable {
    private int id;
    private String c1;
    private String c2;
    private String date;

    public UserTable(int id, String c1, String c2) {
        this.id = id;
        this.c1 = c1;
        this.c2 = c2;
    }

    public UserTable(int id, String c1, String c2, String Date) {
        this.id = id;
        this.c1 = c1;
        this.c2 = c2;
        this.date = Date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", c1='" + c1 + '\'' +
                ", c2='" + c2 + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}