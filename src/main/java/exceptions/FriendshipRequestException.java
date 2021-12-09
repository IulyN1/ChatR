package exceptions;

public class FriendshipRequestException extends Exception{
    /**
     * Constructor for FriendshipRequestException
     * @param err
     */
    public FriendshipRequestException(String err){
        super("Friendship Request Exception: "+err);
    }
}
