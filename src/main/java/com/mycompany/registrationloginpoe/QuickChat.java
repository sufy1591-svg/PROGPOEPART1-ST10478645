package com.mycompany.registrationloginpoe;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.ArrayList;
import java.util.Scanner;



  /**
 *
 * @author Sufy
 */

public class QuickChat {

    
    //Scanner is declared here so it can be reused by all methods
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        //Step 1: Capture registration details
        System.out.println("===== Registration =====");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter a username: ");
        String userName = input.nextLine();

        System.out.print("Enter a password: ");
        String password = input.nextLine();

        System.out.print("Enter your South African cell phone number (e.g. +27838968976): ");
        String cellPhoneNumber = input.nextLine();

        //Create the Login object with the captured details
        Login user;
        user = new Login(firstName, lastName, userName, password, cellPhoneNumber);

        //Step 2: Register - checks username and password
        System.out.println(user.registerUser());

        //Step 3: Check the cell phone number separately
        System.out.println(user.checkCellPhoneNumberMessage());

        //Only continue to login if everything captured successfully
        boolean registrationSuccessful = user.checkUserName() && user.checkPasswordComplexity() && user.checkCellPhoneNumber();

        if (registrationSuccessful) {
            //Step 4: Login using the same username and password
            System.out.println("\n===== Login =====");
            System.out.print("Enter your username: ");
            String loginUserName = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            boolean loggedIn = user.loginUser(loginUserName, loginPassword);
            System.out.println(user.returnLoginStatus());

            //Step 5: Messages can only be sent if the user logged in successfully
            if (loggedIn) {
                //Every message created from now on records who sent it
                Message.setCurrentSender(user.getFirstName() + " " + user.getLastName());
                quickChat();
            } else {
                System.out.println("\nLogin was not successful, so you cannot send messages.");
            }
        } else {
            System.out.println("\nRegistration was not successful, so login cannot continue.");
        }

        input.close();
    }

    //Method: runs the QuickChat menu until the user chooses to quit
    public static void quickChat() {

        System.out.println("\nWelcome to QuickChat.");

        //Read any messages that were stored in an earlier run into the stored messages array
        Message.loadStoredMessages();

        //The user decides how many messages they want to enter
        System.out.print("How many messages do you want to enter? ");
        int numMessages = input.nextInt();
        input.nextLine(); //clear the leftover newline

        while (numMessages < 1) {
            System.out.print("Please enter a number greater than 0: ");
            numMessages = input.nextInt();
            input.nextLine();
        }

        boolean messagesEntered = false;
        int choice;

        do {
            System.out.println("\n========== QUICKCHAT MENU ==========");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.println("====================================");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                if (messagesEntered) {
                    System.out.println("\nYou have already entered the " + numMessages + " messages you set at the start.");
                } else {
                    sendMessages(numMessages);
                    messagesEntered = true;
                }
            } else if (choice == 2) {
                System.out.println("\n" + Message.printMessages());
            } else if (choice == 3) {
                System.out.println("\nThank you for using QuickChat.");
            } else if (choice == 4) {
                storedMessagesMenu();
            } else {
                System.out.println("\nInvalid menu option.");
            }

        } while (choice != 3);
    }

    //Method: lets the user enter the set number of messages, one at a time
    public static void sendMessages(int numMessages) {

        for (int i = 0; i < numMessages; i++) {
            System.out.println("\nMessage " + (i + 1));
            System.out.println("--------------------");

            //The message ID is generated when the Message object is created
            Message msg = new Message(i);

            //Recipient - keep asking until the number is correctly formatted
            do {
                System.out.print("Enter the recipient cell number (e.g. +27718693002): ");
                msg.setRecipient(input.nextLine());
                System.out.println(msg.checkRecipientCell());
            } while (!msg.isRecipientValid());

            //Message - keep asking until it is 250 characters or less
            do {
                System.out.print("Enter your message (maximum 250 characters): ");
                msg.setMessageText(input.nextLine());
                System.out.println(msg.checkMessageLength());
            } while (!msg.isMessageLengthValid());

            System.out.println(msg.displayMessageID());
            System.out.println("Message Hash: " + msg.createMessageHash());

            //Send, disregard or store the message
            System.out.println("\nWhat would you like to do with this message?");
            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Enter your choice: ");
            int sendChoice = input.nextInt();
            input.nextLine();

            System.out.println(msg.sentMessage(sendChoice));

            if (sendChoice == 1) {
                //Full details are shown once the message has been sent
                System.out.println("\n" + msg.getMessageDetails());
            } else if (sendChoice == 2) {
                //The message is only deleted once the user presses 0
                String delete = input.nextLine();
                while (!delete.equals("0")) {
                    System.out.print("Press 0 to delete the message: ");
                    delete = input.nextLine();
                }
                System.out.println("Message deleted.");
            }
        }

        //Once all the messages have been entered, show what was sent and the total
        System.out.println("\n===== Messages Sent =====");
        System.out.println(Message.printMessages());
        System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
    }

    //Method: PART 3 - the Stored Messages menu (main menu option 4)
    public static void storedMessagesMenu() {

        //Read the JSON file into the stored messages array
        Message.loadStoredMessages();

        int storedChoice;

        do {
            System.out.println("\n========== STORED MESSAGES ==========");
            System.out.println("1. Display the sender and recipient of all stored messages");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search for a message ID");
            System.out.println("4. Search for all messages stored for a recipient");
            System.out.println("5. Delete a message using its message hash");
            System.out.println("6. Display a report of all stored messages");
            System.out.println("7. Back to the main menu");
            System.out.println("=====================================");
            System.out.print("Enter your choice: ");
            storedChoice = input.nextInt();
            input.nextLine();

            if (storedChoice == 1) {
                System.out.println("\n" + Message.displaySenderAndRecipient());
            } else if (storedChoice == 2) {
                Message longest = Message.findLongestMessage();
                if (longest == null) {
                    System.out.println("\nThere are no stored messages.");
                } else {
                    System.out.println("\nLongest stored message: " + longest.getMessageText());
                }
            } else if (storedChoice == 3) {
                System.out.print("Enter the message ID to search for: ");
                String id = input.nextLine();
                Message found = Message.findByMessageID(id);
                if (found == null) {
                    System.out.println("\nMessage ID not found.");
                } else {
                    System.out.println("\nRecipient: " + found.getRecipient());
                    System.out.println("Message: " + found.getMessageText());
                }
            } else if (storedChoice == 4) {
                System.out.print("Enter the recipient cell number to search for: ");
                String recipient = input.nextLine();
                ArrayList<String> results = Message.searchByRecipient(recipient);
                if (results.isEmpty()) {
                    System.out.println("\nNo messages found for that recipient.");
                } else {
                    System.out.println("\nMessages for " + recipient + ":");
                    for (int i = 0; i < results.size(); i++) {
                        System.out.println((i + 1) + ". " + results.get(i));
                    }
                }
            } else if (storedChoice == 5) {
                System.out.print("Enter the message hash to delete: ");
                String hash = input.nextLine();
                System.out.println("\n" + Message.deleteByHash(hash));
            } else if (storedChoice == 6) {
                System.out.println("\n" + Message.displayReport(Message.getStoredMessages()));
            } else if (storedChoice == 7) {
                System.out.println("\nReturning to the main menu.");
            } else {
                System.out.println("\nInvalid menu option.");
            }

        } while (storedChoice != 7);
    }
}
