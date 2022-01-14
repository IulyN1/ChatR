package com.example.chatr.repository.database;

import com.example.chatr.domain.Event;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbRepoEvent implements Repo<Integer, Event> {
    private String url;
    private String username;
    private String password;

    public DbRepoEvent (String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Event event) throws Exception {
        String sql = "insert into events (name, date, " +
                "subscribers) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, event.getName());
            ps.setString(2, event.getDate());
            ps.setString(3, event.getSubscribers());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Event event) throws Exception {
        String sql = "update events set name=?, date=?, " +
                "subscribers=? where id=?";
        Collection<Event> events = findAll();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, event.getName());
            ps.setString(2, event.getDate());
            ps.setString(3, event.getSubscribers());
            ps.setInt(4, event.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Event delete(Integer integer) throws RepoException {
        String sql = "delete from events where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Event event = findById(integer);
            ps.setInt(1, integer);
            ps.executeUpdate();
            return event;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Event doesn't exist!\n");
    }

    @Override
    public Event findById(Integer integer) throws RepoException {
        String sql = "select * from events where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, integer);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String date = resultSet.getString("date");
                    String subscribers = resultSet.getString("subscribers");

                    Event event = new Event(id, name, date, subscribers);
                    return event;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Event doesn't exist!\n");
    }

    @Override
    public Collection<Event> findAll() {
        Set<Event> events = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from events");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String subscribers = resultSet.getString("subscribers");

                Event event = new Event(id, name, date, subscribers);
                events.add(event);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
