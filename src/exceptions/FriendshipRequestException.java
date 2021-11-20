package exceptions;

import domain.FriendshipRequest;

public class FriendshipRequestException extends Exception{
    public FriendshipRequestException(String err){
        super("Friendship Request Exception: "+err);
    }
}
