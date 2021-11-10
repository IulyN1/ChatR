import domain.Friendship;
import domain.User;
import repository.database.DbRepoFriendship;
import repository.database.DbRepoUser;
import repository.fileRepo.FileRepoFriendship;
import repository.fileRepo.FileRepoUser;
import repository.Repo;
import service.Service;
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

        Service service = new Service(userRepo, friendshipRepo,
                UserValidator.getInstance(), FriendshipValidator.getInstance());

        UI ui = new UI(service);
        ui.run();
        //Commentariu Test
    }
}

//test branch