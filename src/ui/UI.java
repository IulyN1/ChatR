package ui;

import domain.*;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUserFriendship;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class UI {
     private final ServiceUserFriendship serviceUserFriendship;
     private final ServiceMessage serviceMessage;
     private final ServiceFriendshipRequest serviceFriendshipRequest;
    /**
     * Constructor for UI
     * @param serviceUserFriendship the service to which the UI transmits the values about users and friendships
     * @param serviceMessage the service to which the UI transmits the values about messages between users
     */
     public UI(ServiceUserFriendship serviceUserFriendship, ServiceMessage serviceMessage,ServiceFriendshipRequest serviceFriendshipRequest){
         this.serviceUserFriendship = serviceUserFriendship;
         this.serviceMessage = serviceMessage;
         this.serviceFriendshipRequest=serviceFriendshipRequest;
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
                 System.out.println("4. Message menu");
                 System.out.println("0. Exit\n");
                 int c = scanner.nextInt();
                 switch (c) {
                     case 1 -> users_menu(scanner);
                     case 2 -> friendships_menu(scanner);
                     case 3 -> special_menu(scanner);
                     case 4 -> message_menu(scanner);
                     case 0 -> ok = false;
                     default -> System.out.println("Invalid option!\n");
                 }
             }
             scanner.close();
         } catch (Exception e){
             System.out.println("Invalid menu! Press '1', '2', '3', '4' or '0' next time!");
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
                            serviceUserFriendship.add_user(firstName, lastName);
                            System.out.println("\nSuccess!\n");
                            break;
                        case 2:
                            System.out.println("ID: ");
                            int id = scanner.nextInt();
                            serviceUserFriendship.delete_user(id);
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
                            serviceUserFriendship.update_user(id, firstName, lastName);
                            System.out.println("\nSuccess!\n");
                            break;
                        case 4:
                            System.out.println("ID: ");
                            id = scanner.nextInt();
                            User user = serviceUserFriendship.find_user_by_id(id);
                            System.out.println("\n" + user.toString() + "\n");
                            break;
                        case 5:
                            System.out.println();
                            for (User us : serviceUserFriendship.get_all_users())
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
            System.out.println("6. User's friendships ");
            System.out.println("7. User's friendships made in a certain month of the year");
            System.out.println("8. Friendship requests");
            System.out.println("0. Exit\n");
            try{
                int c = scanner.nextInt();
                switch (c){
                    case 1:
                        System.out.println("First user ID: ");
                        int id1 = scanner.nextInt();
                        System.out.println("Second user ID: ");
                        int id2 = scanner.nextInt();
                        serviceUserFriendship.add_friendship(id1,id2);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 2:
                        System.out.println("ID: ");
                        int id = scanner.nextInt();
                        System.out.println("ID of user 1: ");
                        int idUser1 = scanner.nextInt();
                        System.out.println("ID of user 2: ");
                        int idUser2 = scanner.nextInt();
                        serviceUserFriendship.update_friendship(id,idUser1,idUser2);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 3:
                        System.out.println("ID: ");
                        id = scanner.nextInt();
                        serviceUserFriendship.delete_friendship(id);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 4:
                        System.out.println("ID: ");
                        id = scanner.nextInt();
                        Friendship friendship = serviceUserFriendship.find_friendship_by_id(id);
                        System.out.println("\n" + friendship.toString() + "\n");
                        break;
                    case 5:
                        System.out.println();
                        for(Friendship fr: serviceUserFriendship.get_all_friendships())
                            System.out.println(fr);
                        System.out.println();
                        break;
                    case 6:
                        System.out.println("ID: ");
                        int ID = scanner.nextInt();
                        User user = serviceUserFriendship.find_user_by_id(ID);//check user's existence
                        System.out.println(user.getFirstName()+" "+user.getLastName()+"'s friendships are:");
                        serviceUserFriendship.get_all_friendships().stream()
                                .filter(
                                        myKey->myKey.getUser1().getId()==ID
                                )
                                .forEach(
                                        myKey->System.out.println(myKey.getUser2().getLastName()
                                                +"|"
                                                +myKey.getUser2().getFirstName()
                                                +"|"
                                                +myKey.getFriendshipDate()

                                        )
                                );
                        serviceUserFriendship.get_all_friendships().stream()
                                .filter(
                                        myKey->myKey.getUser2().getId()==ID
                                )
                                .forEach(
                                        myKey->System.out.println(myKey.getUser1().getLastName()
                                                +"|"
                                                +myKey.getUser1().getFirstName()
                                                +"|"
                                                +myKey.getFriendshipDate()
                                        )
                                );
                        break;
                    case 7:
                        System.out.println("ID: ");
                        id = scanner.nextInt();
                        User user1 = serviceUserFriendship.find_user_by_id(id);//check user's existence
                        System.out.println("MONTH: ");
                        int month = scanner.nextInt();
                        System.out.println("Friendship made in "+
                                new DateFormatSymbols().getMonths()[month-1]+
                                " for "+user1.getFirstName()+
                                " "+
                                user1.getLastName()+
                                " are:");
                        serviceUserFriendship.get_all_friendships().stream()
                                .filter(
                                        myKey->Integer.parseInt((myKey.getFriendshipDate().split("-")[1]))==month
                                                && myKey.getUser1().getId()==id
                                )
                                .forEach(
                                        myKey->System.out.println(myKey.getUser2().getLastName()
                                                +"|"
                                                +myKey.getUser2().getFirstName()
                                                +"|"
                                                +myKey.getFriendshipDate()
                                        )
                                );
                        serviceUserFriendship.get_all_friendships().stream()
                                .filter(
                                        myKey->Integer.parseInt((myKey.getFriendshipDate().split("-")[1]))==month
                                                && myKey.getUser2().getId()==id
                                )
                                .forEach(
                                        myKey->System.out.println(myKey.getUser1().getLastName()
                                                +"|"
                                                +myKey.getUser1().getFirstName()
                                                +"|"
                                                +myKey.getFriendshipDate()
                                        )
                                );
                        break;
                    case 8:
                        System.out.println();
                        for(FriendshipRequest fr: serviceFriendshipRequest.getAllRequests())
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
                        int nr = serviceUserFriendship.nr_related_friendships();
                        System.out.println(nr + "\n");
                        break;
                    case 2:
                        for (String name : serviceUserFriendship.longest_related_friendship())
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

    /**
     * Process the messages menu
     * @param scanner used for scanning the input
     */
    private void message_menu(Scanner scanner) {
        while (true) {
            System.out.println("1. Send message");
            System.out.println("2. All messages");
            System.out.println("3. Reply to message");
            System.out.println("4. Chat between 2 users");
            System.out.println("0. Exit\n");
            try {
                int c = scanner.nextInt();
                switch (c) {
                    case 1:
                        System.out.println("From user ID:");
                        int fromId = scanner.nextInt();
                        System.out.println("To users IDs:");
                        System.out.println("Separate them with ,");
                        scanner.nextLine();
                        String ids = scanner.nextLine();
                        System.out.println("Message:");
                        String message = scanner.nextLine();
                        List<Integer> toIds = serviceMessage.getIdsFromString(ids);
                        serviceMessage.sendMessage(fromId,toIds,message);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 2:
                        System.out.println();
                        for(Message msg: serviceMessage.get_all_messages())
                            System.out.println(msg);
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Message id to reply to:");
                        int idMessage = scanner.nextInt();
                        System.out.println("Id of user who replies to this message:");
                        int idReplier = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Message:");
                        message = scanner.nextLine();
                        serviceMessage.sendReplyMessage(idMessage,idReplier,message);
                        System.out.println("\nSuccess!\n");
                        break;
                    case 4:
                        System.out.println("Id user 1:");
                        int idUser1 = scanner.nextInt();
                        System.out.println("Id user 2:");
                        int idUser2 = scanner.nextInt();
                        System.out.println();
                        Collection<Message> messages = serviceMessage.get_chat(idUser1,idUser2);
                        for(Message msg: messages){
                            MessageDTO dto = new MessageDTO(msg.getId(), msg.getFrom(),msg.getTo(),msg.getMessage());
                            System.out.println(dto);
                        }
                        System.out.println();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}
