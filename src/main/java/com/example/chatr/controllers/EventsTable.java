package com.example.chatr.controllers;

import javafx.scene.control.Button;

import java.util.Objects;

public class EventsTable {
    private String c1;
    private String c2;
    private String c3;
    private Button button1;
    /*
    public EventsTable(String c1, String c2, String c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

     */
    public EventsTable(String c1, String c2) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }
    public EventsTable(String c1, String c2,Button button1) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.button1=button1;
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

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public Button getButton1() {
        return button1;
    }

    public void setButton1(Button button1) {
        this.button1 = button1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventsTable)) return false;
        EventsTable that = (EventsTable) o;
        return Objects.equals(c1, that.c1) && Objects.equals(c2, that.c2) && Objects.equals(c3, that.c3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c1, c2, c3);
    }

    @Override
    public String toString() {
        return "EventsTable{" +
                "c1='" + c1 + '\'' +
                ", c2='" + c2 + '\'' +
                ", c3='" + c3 + '\'' +
                '}';
    }
}
