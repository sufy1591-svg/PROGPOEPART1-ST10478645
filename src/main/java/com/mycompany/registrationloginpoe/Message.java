package com.mycompany.registrationloginpoe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Sufyaan Bahadur
 *
 * The Message class holds the details of a single QuickChat message and the
 * methods used to validate it, create its hash, and send, store or disregard it.
 *
 * Reference (JSON storage):
 * The JSON structure and string escaping used in storeMessage() follow the
 * syntax rules in:
 * Bray, T. (ed.) 2017. The JavaScript Object Notation (JSON) Data Interchange
 * Format (RFC 8259). Internet Engineering Task Force. Available at:
 * https://www.rfc-editor.org/rfc/rfc8259 [Accessed 18 October 2026].
 */
public class Message {

    //Attributes that describe one message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;

    //These lists are shared by every Message object, so they keep track of
    //everything sent and stored while the program is running
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    //Constructor used by the app - the message ID is generated automatically
    public Message(int messageNumber) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = "";
        this.messageText = "";
    }

    //Constructor used by the unit tests - lets us supply our own message ID
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    //Method: generates a random ten-digit message ID
    public static String generateMessageID() {
        Random rand = new Random();
        String id = "";

        for (int i = 1; i <= 10; i++) {
            id = id + rand.nextInt(10);
        }

        return id;
    }

    //Method: returns the message that says which ID was generated
    public String displayMessageID() {
        return "Message ID generated: " + messageID;
    }

    //Method: checks that the message ID is not more than ten characters long
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    //Method: checks the recipient cell number - it must start with the
    //international code (+27) followed by no more than ten digits
    public boolean isRecipientValid() {
        return recipient != null && recipient.matches("^\\+27[0-9]{1,10}$");
    }

    //Method: returns the message for the recipient cell number check
    public String checkRecipientCell() {
        if (isRecipientValid()) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    //Method: checks that the message is not more than 250 characters
    public boolean isMessageLengthValid() {
        return messageText.length() <= 250;
    }

    //Method: returns the message for the message length check
    public String checkMessageLength() {
        if (isMessageLengthValid()) {
            return "Message ready to send.";
        } else {
            int extra = messageText.length() - 250;
            return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
        }
    }

    //Method: creates the message hash - first two digits of the message ID,
    //the message number, then the first and last words of the message, in capitals
    public String createMessageHash() {
        String firstTwo = messageID;
        if (messageID.length() > 2) {
            firstTwo = messageID.substring(0, 2);
        }

        String[] words = messageText.trim().split("\\s+");

        //remove punctuation such as commas and question marks from the words
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    //Method: lets the user send, disregard or store the message
    //1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";
        } else {
            return "Invalid option selected.";
        }
    }

    //Method: returns the full details of this message in the required order
    public String getMessageDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + createMessageHash() + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    //Method: returns all the messages sent while the program is running
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        String output = "";

        for (int i = 0; i < sentMessages.size(); i++) {
            output = output + sentMessages.get(i).getMessageDetails() + "\n--------------------\n";
        }

        return output;
    }

    //Method: returns the total number of messages sent
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    //Method: stores the message in a JSON file so it can be sent later
    public void storeMessage() {
        storedMessages.add(this);

        try {
            FileWriter writer = new FileWriter("StoredMessages.json");

            writer.write("[\n");

            for (int i = 0; i < storedMessages.size(); i++) {
                Message m = storedMessages.get(i);

                writer.write("  {\n");
                writer.write("    \"messageID\": \"" + escapeJson(m.messageID) + "\",\n");
                writer.write("    \"messageHash\": \"" + escapeJson(m.createMessageHash()) + "\",\n");
                writer.write("    \"recipient\": \"" + escapeJson(m.recipient) + "\",\n");
                writer.write("    \"message\": \"" + escapeJson(m.messageText) + "\"\n");

                //no comma after the last item in the array
                if (i < storedMessages.size() - 1) {
                    writer.write("  },\n");
                } else {
                    writer.write("  }\n");
                }
            }

            writer.write("]\n");
            writer.close();

        } catch (IOException e) {
            System.out.println("The message could not be stored: " + e.getMessage());
        }
    }

    //Method: escapes characters that are not allowed raw inside a JSON string
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    //Method: clears the sent and stored lists (used by the unit tests)
    public static void clearMessages() {
        sentMessages.clear();
        storedMessages.clear();
    }

    //Setters - the app fills these in after validating the user's input
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    //Getter
    public String getMessageID() {
        return messageID;
    }
}