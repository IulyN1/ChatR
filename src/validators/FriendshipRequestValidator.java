package validators;


import domain.FriendshipRequest;

public class FriendshipRequestValidator implements StrategyValidator<FriendshipRequest>{
    private final static FriendshipRequestValidator friendshipRequestValidator=new FriendshipRequestValidator();
    private FriendshipRequestValidator(){}

    public static FriendshipRequestValidator getInstance(){return friendshipRequestValidator;}

    @Override
    public void validate(FriendshipRequest friendshipRequest){

    }
}
