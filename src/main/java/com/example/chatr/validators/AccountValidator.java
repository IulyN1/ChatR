package com.example.chatr.validators;

import com.example.chatr.domain.Account;
import com.example.chatr.exceptions.AccountException;

public class AccountValidator implements StrategyValidator<Account> {
    private final static AccountValidator accountValidator = new AccountValidator();

    private AccountValidator() {
    }

    public static AccountValidator getInstance() {
        return accountValidator;
    }

    @Override
    public void validate(Account account) throws Exception {
        String err = "";
        if (account.getUsername() == null)
            err = err + "Empty username!\n";
        if (account.getPassword() == null)
            err = err + "Empty  password!\n";
        if (account.getId() < 0)
            err = err + "Invalid id!\n";
        if (account.getUser_id() < 0)
            err = err + "Invalid user id!\n";
        if (!err.equals(""))
            throw new AccountException(err);
    }
}
