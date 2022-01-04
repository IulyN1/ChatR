package com.example.chatr;

import com.example.chatr.domain.*;
import com.example.chatr.repository.Repo;
import com.example.chatr.repository.database.*;
import com.example.chatr.service.ServiceEvent;
import com.example.chatr.service.ServiceFriendshipRequest;
import com.example.chatr.service.ServiceMessage;
import com.example.chatr.service.ServiceUserFriendship;
import com.example.chatr.ui.UI;
import com.example.chatr.validators.EventValidator;
import com.example.chatr.validators.FriendshipRequestValidator;
import com.example.chatr.validators.FriendshipValidator;
import com.example.chatr.validators.UserValidator;

public class Main {
    public static void main(String[] args) throws Exception {
        Repo<Integer, User> userRepo = new DbRepoUser("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres");
        Repo<Integer, Friendship> friendshipRepo = new DbRepoFriendship("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres");
        Repo<Integer, FriendshipRequest> friendshipRequestRepo = new DbRepoFriendshipRequest(
                "jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "postgres");
        Repo<Integer, Message> messageRepo = new DbRepoMessage("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres", userRepo);
        Repo<Integer, Event> eventRepo = new DbRepoEvent("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres");

        ServiceUserFriendship serviceUserFriendship = new ServiceUserFriendship(userRepo, friendshipRepo,
                UserValidator.getInstance(), FriendshipValidator.getInstance());
        ServiceMessage serviceMessage = new ServiceMessage(userRepo, messageRepo);
        ServiceEvent serviceEvent=new ServiceEvent(eventRepo, EventValidator.getInstance());
        ServiceFriendshipRequest serviceFriendshipRequest = new ServiceFriendshipRequest(userRepo, friendshipRequestRepo,
                FriendshipRequestValidator.getInstance());
        UI ui = new UI(serviceUserFriendship, serviceMessage, serviceFriendshipRequest);
        ui.run();
    }
}
