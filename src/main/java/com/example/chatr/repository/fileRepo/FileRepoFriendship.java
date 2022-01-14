package com.example.chatr.repository.fileRepo;

import com.example.chatr.domain.Friendship;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.validators.FriendshipValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class FileRepoFriendship extends SuperclassFileRepo<Friendship> {
    /**
     * Constructor for FileRepoFriendship
     *
     * @param fileName the file name where the friendships is located
     * @throws Exception if the operation fails
     */
    public FileRepoFriendship(String fileName) throws Exception {
        super(fileName);
    }

    /**
     * Loads the friendships from the file
     *
     * @throws Exception if the operation fails
     */
    @Override
    protected void load() throws Exception {
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] separate = line.split(";");
                if (separate.length != 4)
                    throw new RepoException("Row \"" + line + "\" is invalid!\n");
                User user1 = new User(separate[0], separate[1]);
                User user2 = new User(separate[2], separate[3]);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String date = sdf.format(new Date());
                Friendship friendship = new Friendship(user1, user2, date);
                FriendshipValidator.getInstance().validate(friendship);
                super.addNoSave(friendship);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RepoException("The file doesn't exist!\n");
        }
    }

    /**
     * Saves the friendships to the file
     */
    @Override
    protected void save() {
        Collection<Friendship> collection = super.findAll();
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            for (Friendship friendship : collection.stream().toList()) {
                User user1 = friendship.getUser1();
                User user2 = friendship.getUser2();
                fileWriter.write(user1.getFirstName() + ";" +
                        user1.getLastName() + ";" + user2.getFirstName() +
                        ";" + user2.getLastName() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
