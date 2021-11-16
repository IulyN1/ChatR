import domain.Friendship;
import domain.Message;
import domain.User;
import repository.database.DbRepoFriendship;
import repository.database.DbRepoMessage;
import repository.database.DbRepoUser;
import repository.Repo;
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
                "postgres","postgres");
        Repo<Integer, Friendship> friendshipRepo = new DbRepoFriendship("jdbc:postgresql://localhost:5432/network",
                "postgres","postgres");
        Repo<Integer, Message> messageRepo = new DbRepoMessage("jdbc:postgresql://localhost:5432/network",
                "postgres","postgres", userRepo);

        ServiceUserFriendship serviceUserFriendship = new ServiceUserFriendship(userRepo, friendshipRepo,
                UserValidator.getInstance(), FriendshipValidator.getInstance());
        ServiceMessage serviceMessage = new ServiceMessage(userRepo,messageRepo);
        UI ui = new UI(serviceUserFriendship, serviceMessage);
        ui.run();
    }
}

//test branch