package com.example.chatr.domain;

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
