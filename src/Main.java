import domain.Friendship;
import domain.FriendshipRequest;
import domain.Message;
import domain.User;
import repository.database.DbRepoFriendship;
import repository.database.DbRepoFriendshipRequest;
import repository.database.DbRepoMessage;
import repository.database.DbRepoUser;
import repository.Repo;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUserFriendship;
import ui.UI;
import validators.FriendshipValidator;
import validators.UserValidator;

public class Main {
    public static void main(String[] args) throws Exception {
        //Repo<Integer, User> userRepo = new FileRepoUser("src/users.csv");
        //Repo<Integer, Friendship> friendshipRepo = new FileRepoFriendship("src/friendships.csv");
        Repo<Integer, User> userRepo = new DbRepoUser("jdbc:postgresql://localhost:5432/network",
                "postgres","Catinei18");
        Repo<Integer, Friendship> friendshipRepo = new DbRepoFriendship("jdbc:postgresql://localhost:5432/network",
                "postgres","Catinei18");
        Repo<Integer, FriendshipRequest> friendshipRequestRepo = new DbRepoFriendshipRequest("jdbc:postgresql://localhost:5432/network",
                "postgres","Catinei18");
        Repo<Integer, Message> messageRepo = new DbRepoMessage("jdbc:postgresql://localhost:5432/network",
                "postgres","Catinei18", userRepo);

        ServiceUserFriendship serviceUserFriendship = new ServiceUserFriendship(userRepo, friendshipRepo,
                UserValidator.getInstance(), FriendshipValidator.getInstance());
        ServiceMessage serviceMessage = new ServiceMessage(userRepo,messageRepo);
        ServiceFriendshipRequest serviceFriendshipRequest=new ServiceFriendshipRequest(userRepo,friendshipRequestRepo,
                UserValidator.getInstance());
        UI ui = new UI(serviceUserFriendship, serviceMessage,serviceFriendshipRequest);
        ui.run();
        //Commentariu Test
    }
}
