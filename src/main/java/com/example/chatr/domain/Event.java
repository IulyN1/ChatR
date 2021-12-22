package com.example.chatr.domain;

import javafx.scene.control.Alert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Event extends Entity<Integer>  {
    String name;
    String date;
    String subscribers;

    public Event(Integer integer, String name, String date, String subscribers) {
        super(integer);
        this.name = name;
        this.date = date;
        this.subscribers = subscribers;
    }
    public Event(String name, String date, String subscribers) {
        super(0);
        this.name = name;
        this.date = date;
        this.subscribers = subscribers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public void update(){
        try {
            SimpleDateFormat formatter5=new SimpleDateFormat("dd MMM yyyy");
            Date date5=formatter5.parse(date);
            Date currentDate = new Date();
            if(currentDate.getDay()==date5.getDay()&&
                    currentDate.getMonth()==date5.getMonth()&&
                    currentDate.getYear()==date5.getYear()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event notification");
                alert.setContentText("Event "+name+ " is today!");
                alert.showAndWait();
            }
            else if(currentDate.getDay()==date5.getDay()-1&&
                    currentDate.getMonth()==date5.getMonth()&&
                    currentDate.getYear()==date5.getYear()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event notification");
                alert.setContentText("Event "+name+ " is tomorrow!");
                alert.showAndWait();
            }
            else if(currentDate.getDay()==date5.getDay()-3&&
                    currentDate.getMonth()==date5.getMonth()&&
                    currentDate.getYear()==date5.getYear()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event notification");
                alert.setContentText("Event "+name+ " is in 3 days!");
                alert.showAndWait();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(date, event.date) && Objects.equals(subscribers, event.subscribers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, subscribers);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", subscribers='" + subscribers + '\'' +
                '}';
    }

    @Override
    public void modify(Entity<Integer> entity) {
        Event event = (Event) entity;
        setName(event.getName());
        setSubscribers(event.getSubscribers());
        setDate(event.getDate());
    }
}
