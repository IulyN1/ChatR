package com.example.chatr.repository.database;

import com.example.chatr.domain.Account;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbRepoAccount implements Repo<Integer, Account> {
    private String url;
    private String username;
    private String password;

    public DbRepoAccount(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Account account) throws Exception {
        String sql = "insert into accounts (username, password, " +
                "user_id) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setInt(3, account.getUser_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Account account) throws Exception {
        String sql = "update accounts set username=?, password=?, " +
                "user_id=? where id=?";
        Collection<Account> accounts = findAll();
        for (Account ac : accounts) {
            if (ac.equals(accounts)) {
                throw new RepoException("Account already exists!\n");
            }
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setInt(3, account.getUser_id());
            ps.setInt(4, account.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account delete(Integer integer) throws RepoException {
        String sql = "delete from accounts where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Account account = findById(integer);
            ps.setInt(1, integer);
            ps.executeUpdate();
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Account doesn't exist!\n");
    }

    @Override
    public Account findById(Integer integer) throws RepoException {
        String sql = "select * from account where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, integer);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DbRepoUser all_users = new DbRepoUser(url, username, password);
                    Integer id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    int user_id = resultSet.getInt("user_id");

                    Account account = new Account(id, username, password, user_id);
                    return account;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RepoException("Account doesn't exist!\n");
    }

    /**
     * Gets all the friendshipsReq from the database, in the friendships_request table
     *
     * @return an iterable collection with all the friendships req
     */
    @Override
    public Collection<Account> findAll() {
        Set<Account> accounts = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from accounts");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int user_id = resultSet.getInt("user_id");

                Account account = new Account(id, username, password, user_id);
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
}
