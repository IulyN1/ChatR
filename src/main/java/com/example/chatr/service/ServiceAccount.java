package com.example.chatr.service;

import com.example.chatr.domain.Account;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.AccountException;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;
import com.example.chatr.validators.StrategyValidator;

import java.util.Collection;

public class ServiceAccount {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, Account> accountRepo;
    private final StrategyValidator<Account> accountStrategyValidator;

    public ServiceAccount(Repo<Integer, User> userRepo, Repo<Integer, Account> accountRepo, StrategyValidator<Account> accountStrategyValidator) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.accountStrategyValidator = accountStrategyValidator;
    }

    public void addAccount(Account account) throws Exception {
        accountStrategyValidator.validate(account);
        accountRepo.add(account);
    }

    public void updateAccount(Account account) throws Exception {
        accountStrategyValidator.validate(account);
        accountRepo.update(account);
    }

    public Account deleteAccount(int id) throws RepoException {
        return accountRepo.delete(id);
    }

    public Account findAccountById(int id) throws RepoException {
        return accountRepo.find_by_id(id);
    }

    public Collection<Account> getAllAccounts() {
        return accountRepo.find_all();
    }

    public Account verifyAccount(String username, String password) throws Exception {
        Collection<Account> accounts = getAllAccounts();
        for (Account ac : accounts)
            if (ac.getUsername().equals(username) && ac.getPassword().equals(password)) {
                return ac;
            }
        throw new AccountException("Invalid username or password!");
    }

    public void verifyUniqueUsername(String username) throws Exception {
        if (username.equals("")) {
            throw new AccountException("Username must not be empty!");
        }
        Collection<Account> accounts = getAllAccounts();
        for (Account ac : accounts) {
            if (ac.getUsername().equals(username)) {
                throw new AccountException("Username already in use!");
            }
        }
    }
}
