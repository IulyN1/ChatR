package com.example.chatr.controllers;

import javafx.scene.control.Button;

public class UserTable {
    private String c1;
    private String c2;
    private String date;
    private Button button1;
    private Button button2;

    public UserTable(String c1, String c2,Button button1) {
        this.c1 = c1;
        this.c2 = c2;
        this.button1=button1;
    }

    public UserTable(String c1, String c2, String Date,Button button1) {
        this.c1 = c1;
        this.c2 = c2;
        this.date = Date;
        this.button1=button1;
    }

    public UserTable(String c1, String c2, String Date, Button button1,Button button2){
        this.c1=c1;
        this.c2=c2;
        this.date=Date;
        this.button1=button1;
        this.button2=button2;
    }

    public UserTable(String c1, String c2, Button button1,Button button2){
        this.c1=c1;
        this.c2=c2;
        this.button1=button1;
        this.button2=button2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Button getButton1() {
        return button1;
    }

    public void setButton1(Button button1) {
        this.button1 = button1;
    }

    public void setButtonText(String text){
        this.button1.setText(text);
    }

    public Button getButton2() {
        return button2;
    }

    public void setButton2(Button button2) {
        this.button2 = button2;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                ", c1='" + c1 + '\'' +
                ", c2='" + c2 + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}