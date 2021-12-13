package com.example.chatr.domain;

import java.util.Objects;

public class Account extends Entity<Integer> {
    private String username;
    private String password;
    private int user_id;

    public Account(Integer id, String username, String password, int user_id) {
        super(id);
        this.username = username;
        this.password = password;
        this.user_id = user_id;
    }

    public Account(String username, String password, int user_id) {
        super(0);
        this.username = username;
        this.password = password;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return user_id == account.user_id && username.equals(account.username) && password.equals(account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, user_id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", user_id=" + user_id +
                ", id=" + id +
                '}';
    }

    @Override
    public void modify(Entity<Integer> entity) {
        Account account = (Account) entity;
        setUsername(account.getUsername());
        setPassword(account.getPassword());
        setUser_id(account.getUser_id());
    }
}
