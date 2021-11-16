package repository.database;

import domain.Message;
import domain.User;
import exceptions.RepoException;
import repository.Repo;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DbRepoMessage implements Repo<Integer, Message> {
    private String url;
    private String username;
    private String password;
    private DbRepoUser repoUser;

    public DbRepoMessage(String url, String username, String password, DbRepoUser repoUser) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.repoUser = repoUser;
    }

    @Override
    public void add(Message message) throws Exception {
        String sql = "insert into messages (id,fromUserId,toUserIds,message,date) values (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1,message.getId());
            ps.setInt(2,message.getFrom().getId());
            ps.setString(3,message.getFrom().getFirstName());
            ps.setString(4,message.getFrom().getLastName());

            List<User> toUsers = message.getTo();
            String toIds = getToIds(toUsers);
            ps.setString(5,toIds);

            ps.setString(6,message.getMessage());
            ps.setDate(7, Date.valueOf(message.getDate()));

            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String getToIds(List<User> toUsers){
        String toIds = "";
        int i = 0;
        for(User user: toUsers){
            toIds += user.getId();
            i++;
            if(i< toUsers.size()){
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

    @Override
    public Message find_by_id(Integer integer) throws RepoException {
        return null;
    }

    @Override
    public Collection<Message> find_all() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer fromId = resultSet.getInt("fromUserId");
                String fromFirstName = resultSet.getString("fromUserFirstName");
                String fromLastName = resultSet.getString("fromUserLastName");
                String to = resultSet.getString("toUserIds");
                String message = resultSet.getString("message");

                User userFrom = new User(fromId,fromFirstName,fromLastName);
                List<User> toUsers = getToUsers(to);
                Message messageSent = new Message(id,userFrom,toUsers,message);

                messages.add(messageSent);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<User> getToUsers(String to){
        String[] array = to.split(" ");
        int[] toUsersIds = new int[array.length];
        for(int i=0;i< array.length;i++){
            toUsersIds[i] = Integer.parseInt(array[i]);
        }
        List<User> toUsers = new ArrayList<>();
        for(int idUser: toUsersIds){
            try {
                User user = repoUser.find_by_id(idUser);
                toUsers.add(user);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return toUsers;
    }
}
