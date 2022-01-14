package com.example.chatr.repository.database;

import com.example.chatr.domain.Friendship;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DbRepoFriendship implements Repo<Integer, Friendship> {
    private String url;
    private String username;
    private String password;

    /**
     * Constructor for DbRepoFriendship
     *
     * @param url      the url of the server where the database runs
     * @param username the username used to log into the database
     * @param password the password used to log into the database
     */
    public DbRepoFriendship(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Adds a friendship into the database, into friendships table
     *
     * @param friendship the friendship that needs to be added
     * @throws Exception if the operation fails
     */
    @Override
    public void add(Friendship friendship) throws Exception {
        String sql = "insert into friendships (first_name_user1, last_name_user1, " +
                "first_name_user2, last_name_user2, uid1, uid2,friendship_date) values (?,?,?,?,?,?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = sdf.format(new Date());
        Collection<Friendship> all_friendships = findAll();
        for (Friendship fr : all_friendships) {
            if (fr.equals(friendship)) {
                throw new RepoException("Friendship already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, friendship.getUser1().getFirstName());
            ps.setString(2, friendship.getUser1().getLastName());
            ps.setString(3, friendship.getUser2().getFirstName());
            ps.setString(4, friendship.getUser2().getLastName());
            ps.setInt(5, friendship.getUser1().getId());
            ps.setInt(6, friendship.getUser2().getId());
            ps.setString(7, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a friendship in the database, in friendships table
     *
     * @param friendship the new friendship after modifications
     * @throws Exception if the operation fails
     */
    @Override
    public void update(Friendship friendship) throws Exception {
        String sql = "update friendships set first_name_user1=?, last_name_user1=?, " +
                "first_name_user2=?, last_name_user2=?, uid1=?, uid2=? where id=?";
        Collection<Friendship> all_friendships = findAll();
        for (Friendship fr : all_friendships) {
            if (fr.equals(friendship)) {
                throw new RepoException("Friendship already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, friendship.getUser1().getFirstName());
            ps.setString(2, friendship.getUser1().getLastName());
            ps.setString(3, friendship.getUser2().getFirstName());
            ps.setString(4, friendship.getUser2().getLastName());
            ps.setInt(5, friendship.getUser1().getId());
            ps.setInt(6, friendship.getUser2().getId());
            ps.setInt(7, friendship.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a friendship with a given id from the database, from friendships table
     *
     * @param integer the id of the friendship
     * @return the deleted friendship
     * @throws RepoException if the friendship doesn't exist
     */
    @Override
    public Friendship delete(Integer integer) throws RepoException {
        String sql = "delete from friendships where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            Friendship friendship = findById(integer);
            ps.setInt(1, integer);

            ps.executeUpdate();
            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Friendship doesn't exist!\n");
    }

    /**
     * Finds a friendship by id in the friendships table from the database
     *
     * @param integer the id of the friendship
     * @return the found friendship
     * @throws RepoException if the friendship doesn't exist
     */
    @Override
    public Friendship findById(Integer integer) throws RepoException {
        String sql = "select * from friendships where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, integer);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String firstNameUser1 = resultSet.getString("first_name_user1");
                    String lastNameUser1 = resultSet.getString("last_name_user1");
                    String firstNameUser2 = resultSet.getString("first_name_user2");
                    String lastNameUser2 = resultSet.getString("last_name_user2");
                    int userId1 = resultSet.getInt("uid1");
                    int userId2 = resultSet.getInt("uid2");
                    String friendshipDate = resultSet.getString("friendship_date");

                    User user1 = new User(userId1, firstNameUser1, lastNameUser1);
                    User user2 = new User(userId2, firstNameUser2, lastNameUser2);
                    Friendship friendship = new Friendship(user1, user2, friendshipDate);
                    friendship.setId(id);
                    return friendship;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Friendship doesn't exist!\n");
    }

    /**
     * Gets all the friendships from the database, in the friendships table
     *
     * @return an iterable collection with all the friendships
     */
    @Override
    public Collection<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstNameUser1 = resultSet.getString("first_name_user1");
                String lastNameUser1 = resultSet.getString("last_name_user1");
                String firstNameUser2 = resultSet.getString("first_name_user2");
                String lastNameUser2 = resultSet.getString("last_name_user2");
                int userId1 = resultSet.getInt("uid1");
                int userId2 = resultSet.getInt("uid2");
                String friendshipDate = resultSet.getString("friendship_date");
                User user1 = new User(userId1, firstNameUser1, lastNameUser1);
                User user2 = new User(userId2, firstNameUser2, lastNameUser2);
                Friendship friendship = new Friendship(user1, user2, friendshipDate);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }
}
