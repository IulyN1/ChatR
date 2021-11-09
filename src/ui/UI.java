package ui;

import domain.Friendship;
import domain.User;
import service.Service;

import java.util.Scanner;

public class UI {
     private Service service;

    /**
     * Constructor for UI
     * @param service the service that the UI transmits the values to
     */
     public UI(Service service){
         this.service = service;
     }

    /**
     * Runs the program
     */
    public void run(){
         System.out.println("Hello!");
         boolean ok = true;
         try {
             Scanner scanner = new Scanner(System.in);
             while (ok) {
                 System.out.println("1. Users menu");
                 System.out.println("2. Friendships menu");
                 System.out.println("3. Special menu");
                 System.out.println("0. Exit\n");
                 int c = scanner.nextInt();
                 switch (c) {
                     case 1 -> users_menu(scanner);
                     case 2 -> friendships_menu(scanner);
                     case 3 -> special_menu(scanner);
                     case 0 -> ok = false;
                     default -> System.out.println("Invalid option!\n");
                 }
             }
             scanner.close();
         } catch (Exception e){
             System.out.println("Invalid menu! Press '1', '2', '3' or '0' next time!");
         }
         System.out.println("Bye!");
     }

    /**
     * Process the users menu
     * @param scanner used for scanning the input
     */
    private void users_menu(Scanner scanner) {
        while(true) {
                System.out.println("1. Add user");
                System.out.println("2. Delete user");
                System.out.println("3. Modify user");
                System.out.println("4. Search user");
                System.out.println("5. All users");
                System.out.println("0. Exit\n");
                try {
                    int c = scanner.nextInt();
                    switch (c) {
                        case 1:
                            scanner.nextLine();
                            System.out.println("First name: ");
                            String firstName = scanner.nextLine();
                            System.out.println("Last name: ");
                            String lastName = scanner.nextLine();
                            service.add_user(firstName, lastName);
                            System.out.println("\nSuccess!\n");
                            break;
                        case 2:
                            System.out.println("ID: ");
                            int id = scanner.nextInt();
                            service.delete_user(id);
                            System.out.println("\nSuccess!\n");
                            break;
                        case 3:
                            System.out.println("ID: ");
                            id = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("First name: ");
                            firstName = scanner.nextLine();
                            System.out.println("Last name: ");
                            lastName = scanner.nextLine();
                            service.update_user(id, firstName, lastName);
                            System.out.println("\nSuccess!\n");
                            break;
                        case 4:
                            System.out.println("ID: ");
                            id = scanner.nextInt();
                            User user = service.find_user_by_id(id);
                            System.out.println("\n" + user.toString() + "\n");
                            break;
                        case 5:
                            System.out.println();
                            for (User us : service.get_all_users())
                                System.out.println(us);
                            System.out.println();
                            break;
                        case 0:
                            return;
                        default:
                            System.out.println("Invalid option!");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                }
        }
    }

    /**
     * Process the friendships menu
     * @param scanner used for scanning the input
     */
    private void friendships_menu(Scanner scanner) {
        while(true){
            System.out.println("1. Add friendship");
            System.out.println("2. Modify friendship");
            System.out.println("3. Delete friendship");
            System.out.println("4. Search friendship");
            System.out.println("5. All friendship");
            System.out.println("0. Exit\n");
            try{
                int c = scanner.nextInt();
                switch (c){
                    case 1:
                        System.out.println("First user ID: ");
                        int id1 = scanner.nextInt();
                        System.out.println("Second user ID: ");
                        int id2 = scanner.nextInt();
                        service.add_friendship(id1,id2);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 2:
                        System.out.println("ID: ");
                        int id = scanner.nextInt();
                        System.out.println("ID of user 1: ");
                        int idUser1 = scanner.nextInt();
                        System.out.println("ID of user 2: ");
                        int idUser2 = scanner.nextInt();
                        service.update_friendship(id,idUser1,idUser2);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 3:
                        System.out.println("ID: ");
                        id = scanner.nextInt();
                        service.delete_friendship(id);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 4:
                        System.out.println("ID: ");
                        id = scanner.nextInt();
                        Friendship friendship = service.find_friendship_by_id(id);
                        System.out.println("\n" + friendship.toString() + "\n");
                        break;
                    case 5:
                        System.out.println();
                        for(Friendship fr: service.get_all_friendships())
                            System.out.println(fr);
                        System.out.println();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }

    /**
     * Process the special social network menu
     * @param scanner used for scanning the input
     */
    private void special_menu(Scanner scanner) {
        while (true) {
            System.out.println("1. Show the number of friendship communities");
            System.out.println("2. Show the most sociable community");
            System.out.println("0. Exit\n");
            try {
                int c = scanner.nextInt();
                switch (c) {
                    case 1:
                        int nr = service.nr_related_friendships();
                        System.out.println(nr + "\n");
                        break;
                    case 2:
                        for (String name : service.longest_related_friendship())
                            System.out.println(name);
                        System.out.println();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }

}
