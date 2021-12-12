package exceptions;

public class AccountException extends Exception{
    public AccountException(String err){
        super("Account Exception: "+err);
    }
}
