package repository.database;


import domain.FriendshipRequest;
import domain.User;
import exceptions.RepoException;
import repository.Repo;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbRepoFriendshipRequest implements Repo<Integer, FriendshipRequest> {
    private String url;
    private String username;
    private String password;

    public DbRepoFriendshipRequest(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(FriendshipRequest friendshipRequest) throws Exception {
        String sql = "insert into friendship_request (sender_id, receiver_id, " +
                "status) values (?,?,?)";
        Collection<FriendshipRequest> all_friendshipsReq = find_all();
        for(FriendshipRequest fr: all_friendshipsReq){
            if(fr.getSender().getId()==friendshipRequest.getSender().getId()&&
            fr.getReceiver().getId()==friendshipRequest.getReceiver().getId()){
                throw new RepoException("Friendship request already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1,friendshipRequest.getSender().getId());
            ps.setInt(2, friendshipRequest.getReceiver().getId());
            ps.setString(3,friendshipRequest.getStatus());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }



    @Override
    public void update(FriendshipRequest friendshipRequest) throws Exception {
        String sql = "update friendship_request set sender_id=?, receiver_id=?, " +
                "status=? where id=?";
        Collection<FriendshipRequest> all_friendshipsReq = find_all();
        for(FriendshipRequest fr: all_friendshipsReq){
            if(fr.equals(friendshipRequest)){
                throw new RepoException("Friendship already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1,friendshipRequest.getSender().getId());
            ps.setInt(2,friendshipRequest.getReceiver().getId());
            ps.setString(3,friendshipRequest.getStatus());
            ps.setInt(4,friendshipRequest.getId());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Deletes a friendship with a given id from the database, from friendships table
     * @param integer the id of the friendship
     * @return the deleted friendship
     * @throws RepoException if the friendship doesn't exist
     */
    @Override
    public FriendshipRequest delete(Integer integer) throws RepoException {
        String sql = "delete from friendship_request where id=?";

        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            FriendshipRequest friendshipReq = find_by_id(integer);
            ps.setInt(1,integer);

            ps.executeUpdate();
            return friendshipReq;
        } catch (SQLException e){
            e.printStackTrace();
        }
        throw new RepoException("Friendship request doesn't exist!\n");
    }

    /**
     * Finds a friendship by id in the friendships table from the database
     * @param integer the id of the friendship
     * @return the found friendship
     * @throws RepoException if the friendship doesn't exist
     */
    @Override
    public FriendshipRequest find_by_id(Integer integer) throws RepoException {
        String sql = "select * from friendship_request where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1,integer);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()) {
                    DbRepoUser all_users = new DbRepoUser(url,username,password);
                    Integer id = resultSet.getInt("id");
                    int sender_id = resultSet.getInt("sender_id");
                    int receiver_id = resultSet.getInt("receiver_id");
                    String status = resultSet.getString("status");
                    User sender = new User(sender_id,
                            all_users.find_by_id(sender_id).getFirstName(),
                            all_users.find_by_id(receiver_id).getLastName());
                    User receiver = new User(receiver_id,
                            all_users.find_by_id(receiver_id).getFirstName(),
                            all_users.find_by_id(receiver_id).getLastName());
                    FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver,status);
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
     * Gets all the friendships from the database, in the friendships table
     * @return an iterable collection with all the friendships
     */
    @Override
    public Collection<FriendshipRequest> find_all() {
        Set<FriendshipRequest> friendshipsReqs = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship_request");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                DbRepoUser all_users = new DbRepoUser(url,username,password);
                Integer id = resultSet.getInt("id");
                int sender_id = resultSet.getInt("sender_id");
                int receiver_id= resultSet.getInt("receiver_id");
                String status = resultSet.getString("status");
                User sender = new User(sender_id,
                        all_users.find_by_id(sender_id).getFirstName(),
                        all_users.find_by_id(receiver_id).getLastName());
                User receiver = new User(receiver_id,
                        all_users.find_by_id(receiver_id).getFirstName(),
                        all_users.find_by_id(receiver_id).getLastName());
                FriendshipRequest friendshipReq = new FriendshipRequest(sender,receiver,status);
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
