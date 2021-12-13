package com.example.chatr.repository.database;

import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbRepoUser implements Repo<Integer, User> {
    private String url;
    private String username;
    private String password;

    /**
     * Constructor for DbRepoUser
     *
     * @param url      the url of the server where the database runs
     * @param username the username used to log into the database
     * @param password the password used to log into the database
     */
    public DbRepoUser(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Adds a user into the database, into users table
     *
     * @param user the user that needs to be added
     * @throws Exception if the operation fails
     */
    @Override
    public void add(User user) throws Exception {
        String sql = "insert into users (first_name, last_name) values (?,?)";
        Collection<User> all_users = find_all();
        for (User us : all_users) {
            if (us.equals(user)) {
                throw new RepoException("User already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a user in the database, in users table
     *
     * @param user the new user after modifications
     * @throws Exception if the operation fails
     */
    @Override
    public void update(User user) throws Exception {
        String sql = "update users set first_name=?, last_name=? where id=?";
        Collection<User> all_users = find_all();
        for (User us : all_users) {
            if (us.equals(user)) {
                throw new RepoException("User already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a user with a given id from the database, from users table
     *
     * @param integer the id of the user
     * @return the deleted user
     * @throws RepoException if the user doesn't exist
     */
    @Override
    public User delete(Integer integer) throws RepoException {
        String sql = "delete from users where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            User user = find_by_id(integer);
            ps.setInt(1, integer);

            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("User doesn't exist!\n");
    }

    /**
     * Finds a user by id in the users table from the database
     *
     * @param integer the id of the user
     * @return the found user
     * @throws RepoException if the user doesn't exist
     */
    @Override
    public User find_by_id(Integer integer) throws RepoException {
        String sql = "select * from users where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, integer);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    User user = new User(firstName, lastName);
                    user.setId(id);
                    return user;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("User doesn't exist!\n");
    }

    /**
     * Gets all the users from the database, in the users table
     *
     * @return an iterable collection with all the users
     */
    @Override
    public Collection<User> find_all() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User user = new User(firstName, lastName);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
