package com.example.chatr.repository.database;


import com.example.chatr.domain.FriendshipRequest;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbRepoFriendshipRequest implements Repo<Integer, FriendshipRequest> {
    private String url;
    private String username;
    private String password;

    /**
     * Constructor for DbRepoFriendshipRequest
     *
     * @param url
     * @param username
     * @param password
     */
    public DbRepoFriendshipRequest(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * added a friendshipRequest in db, in friendship_request table
     *
     * @param friendshipRequest
     * @throws Exception
     */
    @Override
    public void add(FriendshipRequest friendshipRequest) throws Exception {
        String sql = "insert into friendship_request (sender_id, receiver_id, " +
                "status" + ",request_date" + ") values (?,?,?,?)";
        Collection<FriendshipRequest> all_friendshipsReq = findAll();
        for (FriendshipRequest fr : all_friendshipsReq) {
            if (fr.getSender().getId() == friendshipRequest.getSender().getId() &&
                    fr.getReceiver().getId() == friendshipRequest.getReceiver().getId() &&
                    fr.getStatus().equals(friendshipRequest.getStatus()) ) {
                throw new RepoException("Friendship request already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, friendshipRequest.getSender().getId());
            ps.setInt(2, friendshipRequest.getReceiver().getId());
            ps.setString(3, friendshipRequest.getStatus());
            ps.setString(4, friendshipRequest.getDate());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * update friendship request in db
     *
     * @param friendshipRequest
     * @throws Exception
     */
    @Override
    public void update(FriendshipRequest friendshipRequest) throws Exception {
        String sql = "update friendship_request  set sender_id=?, receiver_id=?, " +
                "status=? where id=?";
        Collection<FriendshipRequest> all_friendshipsReq = findAll();
        for (FriendshipRequest fr : all_friendshipsReq) {
            if (fr.equals(friendshipRequest)) {
                throw new RepoException("Friendship request already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, friendshipRequest.getSender().getId());
            ps.setInt(2, friendshipRequest.getReceiver().getId());
            ps.setString(3, friendshipRequest.getStatus());
            ps.setInt(4, friendshipRequest.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a friendship request with a given id from the database, from friendship_request table
     *
     * @param integer the id of the friendshipRequest
     * @return the deleted friendshipRequest
     * @throws RepoException if the friendshipRequest doesn't exist
     */
    @Override
    public FriendshipRequest delete(Integer integer) throws RepoException {
        String sql = "delete from friendship_request where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            FriendshipRequest friendshipReq = findById(integer);
            ps.setInt(1, integer);

            ps.executeUpdate();
            return friendshipReq;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Friendship request doesn't exist!\n");
    }

    /**
     * Finds a friendshipRequest by id in the friendship_request table from the database
     *
     * @param integer the id of the friendshipRequest
     * @return the found friendshipRequest
     * @throws RepoException if the friendshipRequest doesn't exist
     */
    @Override
    public FriendshipRequest findById(Integer integer) throws RepoException {
        String sql = "select * from friendship_request where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, integer);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DbRepoUser all_users = new DbRepoUser(url, username, password);
                    Integer id = resultSet.getInt("id");
                    int sender_id = resultSet.getInt("sender_id");
                    int receiver_id = resultSet.getInt("receiver_id");
                    String status = resultSet.getString("status");
                    User sender = new User(sender_id,
                            all_users.findById(sender_id).getFirstName(),
                            all_users.findById(receiver_id).getLastName());
                    User receiver = new User(receiver_id,
                            all_users.findById(receiver_id).getFirstName(),
                            all_users.findById(receiver_id).getLastName());
                    FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver, status);
                    friendshipReq.setId(id);
                    return friendshipReq;
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
     * Gets all the friendshipsReq from the database, in the friendships_request table
     *
     * @return an iterable collection with all the friendships req
     */
    @Override
    public Collection<FriendshipRequest> findAll() {
        Set<FriendshipRequest> friendshipsReqs = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship_request");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                DbRepoUser all_users = new DbRepoUser(url, username, password);
                Integer id = resultSet.getInt("id");
                int sender_id = resultSet.getInt("sender_id");
                int receiver_id = resultSet.getInt("receiver_id");
                String status = resultSet.getString("status");
                String date = resultSet.getString("request_date");
                User sender = new User(sender_id,
                        all_users.findById(sender_id).getFirstName(),
                        all_users.findById(sender_id).getLastName());
                User receiver = new User(receiver_id,
                        all_users.findById(receiver_id).getFirstName(),
                        all_users.findById(receiver_id).getLastName());
                FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver, status, date);
                friendshipReq.setId(id);
                friendshipsReqs.add(friendshipReq);
            }
            return friendshipsReqs;
        } catch (SQLException | RepoException e) {
            e.printStackTrace();
        }
        return friendshipsReqs;
    }
}
