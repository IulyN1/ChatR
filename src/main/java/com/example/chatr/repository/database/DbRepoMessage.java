package com.example.chatr.repository.database;

import com.example.chatr.domain.Message;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;

import java.sql.*;
import java.util.*;

public class DbRepoMessage implements Repo<Integer, Message> {
    private String url;
    private String username;
    private String password;
    private Repo<Integer, User> repoUser;

    /**
     * Constructor for Message repo
     *
     * @param url      the url of the server where the database runs
     * @param username the username used to log into the database
     * @param password the password used to log into the database
     * @param repoUser the user repo
     */
    public DbRepoMessage(String url, String username, String password, Repo<Integer, User> repoUser) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.repoUser = repoUser;
    }

    /**
     * Adds a message in the repo
     *
     * @param message message Object
     * @throws Exception if the operation fails
     */
    @Override
    public void add(Message message) throws Exception {
        String sql = "insert into messages (from_user_id,to_user_ids,message,date,reply) values (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, message.getFrom().getId());

            List<User> toUsers = message.getTo();
            String toIds = getToIds(toUsers);
            ps.setString(2, toIds);

            ps.setString(3, message.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(message.getDate()));

            if (message.getReply() == null)
                ps.setNull(5, Types.INTEGER);
            else
                ps.setInt(5, message.getReply().getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a string of ids from a list of users
     *
     * @param toUsers the list of the users
     * @return the string with ids
     */
    public String getToIds(List<User> toUsers) {
        String toIds = "";
        int i = 0;
        for (User user : toUsers) {
            toIds += user.getId();
            i++;
            if (i < toUsers.size()) {
                toIds += " ";
            }
        }
        return toIds;
    }

    @Override
    public void update(Message message) throws Exception {

    }

    @Override
    public Message delete(Integer integer) throws RepoException {
        return null;
    }

    /**
     * Finds a message by its id
     *
     * @param integer the id of the message
     * @return the found message
     * @throws RepoException if the message doesn't exist
     */
    @Override
    public Message findById(Integer integer) throws RepoException {
        String sql = "select * from messages where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, integer);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    Integer fromId = resultSet.getInt("from_user_id");
                    String to = resultSet.getString("to_user_ids");
                    String message = resultSet.getString("message");
                    Timestamp date = resultSet.getTimestamp("date");
                    int replyId = resultSet.getInt("reply");

                    User userFrom = repoUser.findById(fromId);
                    List<User> toUsers = getToUsers(to);
                    Message messageSent = new Message(userFrom, toUsers, message);
                    messageSent.setId(id);
                    messageSent.setDate(date.toLocalDateTime());
                    if (replyId != 0) {
                        messageSent.setReply(findById(replyId));
                    }
                    return messageSent;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Message doesn't exist!\n");
    }

    /**
     * Gets all the messages
     *
     * @return a collection with all the messages
     */
    @Override
    public Collection<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer fromId = resultSet.getInt("from_user_id");
                String to = resultSet.getString("to_user_ids");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int replyId = resultSet.getInt("reply");

                User userFrom = repoUser.findById(fromId);
                List<User> toUsers = getToUsers(to);
                Message messageSent = new Message(userFrom, toUsers, message);
                messageSent.setId(id);
                messageSent.setDate(date.toLocalDateTime());
                if (replyId != 0) {
                    messageSent.setReply(findById(replyId));
                }
                messages.add(messageSent);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
        return messages;
    }

    /**
     * Gets the list of users from a string of ids
     *
     * @param to the string with user ids
     * @return the user list
     */
    public List<User> getToUsers(String to) {
        String[] array = to.split(" ");
        int[] toUsersIds = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            toUsersIds[i] = Integer.parseInt(array[i]);
        }
        List<User> toUsers = new ArrayList<>();
        for (int idUser : toUsersIds) {
            try {
                User user = repoUser.findById(idUser);
                toUsers.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toUsers;
    }
}
