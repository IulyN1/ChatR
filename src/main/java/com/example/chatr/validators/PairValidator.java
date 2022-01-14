package com.example.chatr.validators;

import com.example.chatr.domain.Friendship;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;
import com.example.chatr.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class PairValidator implements StrategyValidator<Pair<Repo<Integer, User>, Repo<Integer, Friendship>>> {
    private static final PairValidator pairValidator = new PairValidator();

    /**
     * Private constructor for PairValidator
     */
    private PairValidator() {
    }

    /**
     * Gets the instance of PairValidator
     *
     * @return the instance of PairValidator
     */
    public static PairValidator getInstance() {
        return pairValidator;
    }

    /**
     * Validates the pair of repositories for valid user-friendship combination
     *
     * @param repos the pair of the two repos of Users and Friendships
     * @throws Exception if the operation fails
     */
    @Override
    public void validate(Pair<Repo<Integer, User>, Repo<Integer, Friendship>> repos) throws Exception {
        Repo<Integer, User> userRepo = repos.getFirst();
        Repo<Integer, Friendship> friendshipRepo = repos.getSecond();
        String err = "";
        Map<Pair<String, String>, User> map = new HashMap<>();
        for (User user : userRepo.findAll()) {
            map.put(new Pair<>(user.getFirstName(), user.getLastName()), user);
        }

        for (Friendship friendship : friendshipRepo.findAll()) {
            User user1 = friendship.getUser1();
            if (!map.containsKey(new Pair<>(user1.getFirstName(), user1.getLastName()))) {
                err += "User" + user1.getFirstName() + " " + user1.getLastName() + " doesn't exist!\n";
            }
            User user2 = friendship.getUser2();
            if (!map.containsKey(new Pair<>(user2.getFirstName(), user2.getLastName()))) {
                err += "User" + user2.getFirstName() + " " + user2.getLastName() + " doesn't exist!\n";
            }
        }
        if (!err.equals(""))
            throw new RepoException(err);
    }
}
