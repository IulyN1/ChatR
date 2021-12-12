package service;

import domain.Account;
import domain.User;
import exceptions.AccountException;
import exceptions.RepoException;
import repository.Repo;
import validators.StrategyValidator;

import java.util.Collection;

public class ServiceAccount {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, Account> accountRepo;
    private final StrategyValidator<Account> accountStrategyValidator;

    public ServiceAccount(Repo<Integer, User> userRepo, Repo<Integer, Account> accountRepo, StrategyValidator<Account> accountStrategyValidator) {
        this.userRepo = userRepo;
        this.accountRepo=accountRepo;
        this.accountStrategyValidator=accountStrategyValidator;
    }

    public void addAccount(Account account) throws Exception{
        accountStrategyValidator.validate(account);
        accountRepo.add(account);
    }


    public void updateAccount(Account account) throws Exception{
        accountStrategyValidator.validate(account);
        accountRepo.update(account);
    }



    public Account deleteAccount(int id) throws RepoException{
        return accountRepo.delete(id);
    }


    public Account findAccountById(int id) throws RepoException{
        return accountRepo.find_by_id(id);
    }

    public Collection<Account> getAllAccounts(){
        return accountRepo.find_all();
    }

    public Account verifyAccount(String username,String password) throws Exception {
        Collection<Account>accounts=getAllAccounts();
        boolean valid=false;
        for (Account ac : accounts)
            if (ac.getUsername().equals(username) && ac.getPassword().equals(password)){
                valid=true;
                return ac;
            }
        if (valid == false) {
            throw new AccountException("Invalid username or pasword!");
        }
        return null;
    }
}
