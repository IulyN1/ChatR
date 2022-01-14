package com.example.chatr.repository.fileRepo;

import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.validators.UserValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileRepoUser extends SuperclassFileRepo<User> {
    /**
     * Constructor for FileRepoUser
     *
     * @param fileName the name of the file where the users are located
     * @throws Exception if the operation fails
     */
    public FileRepoUser(String fileName) throws Exception {
        super(fileName);
    }

    /**
     * Loads the users from the file
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
                User user = new User(separate[0], separate[1]);
                UserValidator.getInstance().validate(user);
                super.addNoSave(user);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RepoException("The file doesn't exist!\n");
        }
    }

    /**
     * Saves the users to the file
     */
    @Override
    protected void save() {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            for (User user : super.findAll()) {
                fileWriter.write(user.getFirstName() + ";" + user.getLastName() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
