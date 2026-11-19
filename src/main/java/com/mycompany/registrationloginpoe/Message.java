package com.mycompany.registrationloginpoe;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 *
 * @author Sufy
 */
public class Message {

    //Attributes that describe one message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String sender;
    private String flag;

    //These lists are shared by every Message object, so they keep track of
    //everything sent, disregarded and stored while the program is running
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();

    //The person who is logged in, and the JSON file the stored messages live in
    private static String currentSender = "Unknown sender";
    private static String storedFilePath = "storedMessages.json";

    //Constructor used by the app - the message ID is generated automatically
    public Message(int messageNumber) {
        String newID = generateMessageID();
        while (messageIDs.contains(newID)) {
            newID = generateMessageID();
        }
        this.messageID = newID;
        this.messageNumber = messageNumber;
        this.recipient = "";
        this.messageText = "";
        this.sender = currentSender;
        this.flag = "";
    }

    //Constructor used by the unit tests - lets us supply our own message ID
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.sender = currentSender;
        this.flag = "";
    }

    //Method: generates a random ten-digit message ID
    public static String generateMessageID() {
        Random rand = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + rand.nextInt(10);
        }
        return id;
    }

    //Method: the message ID must be no more than ten characters long
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    //Method: the recipient must start with + (international code) and be a valid length
    public boolean isRecipientValid() {
        return recipient != null && recipient.matches("^\\+\\d{10,12}$");
    }

    public String checkRecipientCell() {
        if (isRecipientValid()) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        }
    }

    //Method: a message may be 250 characters or less
    public boolean isMessageLengthValid() {
        return messageText != null && messageText.length() <= 250;
    }

    public String checkMessageLength() {
        if (isMessageLengthValid()) {
            return "Message ready to send.";
        } else {
            int tooLong = messageText.length() - 250;
            return "Message exceeds 250 characters by " + tooLong + ", please reduce size.";
        }
    }

    public String displayMessageID() {
        return "Message ID generated: " + messageID;
    }

    //Method: first two digits of the ID : message number : first word + last word (capitals)
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String firstTwo = messageID.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    //Method: sends, disregards or stores the message depending on the user's choice
    public String sentMessage(int choice) {
        if (choice == 1) {
            addMessage(this, "Sent");
            return "Message successfully sent.";
        } else if (choice == 2) {
            addMessage(this, "Disregard");
            return "Press 0 to delete message.";
        } else if (choice == 3) {
            addMessage(this, "Stored");
            saveStoredMessages();
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
        }
    }

    public String getMessageDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + createMessageHash() + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    //Method: shows everything that was sent
    public static String printMessages() {
        return displayReport(sentMessages);
    }

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    //Getters and setters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSender() {
        return sender;
    }

    public String getFlag() {
        return flag;
    }

    public static void setCurrentSender(String name) {
        currentSender = name;
    }

    public static void setStoredFilePath(String path) {
        storedFilePath = path;
    }

    public static ArrayList<Message> getSentMessages() {
        return sentMessages;
    }

    public static ArrayList<Message> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static ArrayList<Message> getStoredMessages() {
        return storedMessages;
    }

    public static ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    public static ArrayList<String> getMessageIDs() {
        return messageIDs;
    }

   
    //PART 3 - STORE DATA AND DISPLAY
    

    //Method: puts a message into the correct array and records its hash and ID
    public static void addMessage(Message msg, String flag) {
        if (flag.equalsIgnoreCase("Sent")) {
            msg.flag = "Sent";
            sentMessages.add(msg);
        } else if (flag.equalsIgnoreCase("Disregard")) {
            msg.flag = "Disregard";
            disregardedMessages.add(msg);
        } else if (flag.equalsIgnoreCase("Stored")) {
            msg.flag = "Stored";
            storedMessages.add(msg);
        } else {
            return;
        }
        if (!messageHashes.contains(msg.createMessageHash())) {
            messageHashes.add(msg.createMessageHash());
        }
        if (!messageIDs.contains(msg.messageID)) {
            messageIDs.add(msg.messageID);
        }
    }

    //Method: empties every array (used by the unit tests so each test starts fresh)
    public static void clearAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }

    //Method: 2a - the sender and recipient of every stored message
    public static String displaySenderAndRecipient() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        String output = "";
        for (Message msg : storedMessages) {
            output = output + "Sender: " + msg.sender + " | Recipient: " + msg.recipient + "\n";
        }
        return output;
    }

    //Method: 2b - the longest stored message
    public static Message findLongestMessage() {
        Message longest = null;
        for (Message msg : storedMessages) {
            if (longest == null || msg.messageText.length() > longest.messageText.length()) {
                longest = msg;
            }
        }
        return longest;
    }

    //Method: 2c - finds a message by its ID (sent or stored), returns null if it is not there
    public static Message findByMessageID(String id) {
        for (Message msg : sentMessages) {
            if (msg.messageID.equals(id)) {
                return msg;
            }
        }
        for (Message msg : storedMessages) {
            if (msg.messageID.equals(id)) {
                return msg;
            }
        }
        return null;
    }

    //Method: 2d - every message (sent or stored) for one recipient
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> results = new ArrayList<>();
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(recipient)) {
                results.add(msg.messageText);
            }
        }
        for (Message msg : storedMessages) {
            if (msg.recipient.equals(recipient)) {
                results.add(msg.messageText);
            }
        }
        return results;
    }

    //Method: 2e - deletes a stored message using its hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message msg = storedMessages.get(i);
            if (msg.createMessageHash().equalsIgnoreCase(hash)) {
                storedMessages.remove(i);
                messageHashes.remove(msg.createMessageHash());
                messageIDs.remove(msg.messageID);
                saveStoredMessages();
                return "Message: \"" + msg.messageText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    //Method: 2f - a report listing the full details of every message in the list given
    public static String displayReport(ArrayList<Message> messages) {
        String report = "===== MESSAGE REPORT =====\n";
        if (messages.isEmpty()) {
            return report + "No messages to display.\n";
        }
        for (Message msg : messages) {
            report = report + "Message ID   : " + msg.messageID + "\n"
                    + "Message Hash : " + msg.createMessageHash() + "\n"
                    + "Sender       : " + msg.sender + "\n"
                    + "Recipient    : " + msg.recipient + "\n"
                    + "Message      : " + msg.messageText + "\n"
                    + "--------------------\n";
        }
        return report;
    }

   
    //JSON - writing the stored messages to a file and reading them back
    

    //Method: writes the stored messages array to the JSON file
    public static void saveStoredMessages() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message msg = storedMessages.get(i);
            json.append("  {\n");
            json.append("    \"messageID\": \"" + escapeJson(msg.messageID) + "\",\n");
            json.append("    \"messageNumber\": " + msg.messageNumber + ",\n");
            json.append("    \"sender\": \"" + escapeJson(msg.sender) + "\",\n");
            json.append("    \"recipient\": \"" + escapeJson(msg.recipient) + "\",\n");
            json.append("    \"message\": \"" + escapeJson(msg.messageText) + "\",\n");
            json.append("    \"flag\": \"Stored\"\n");
            json.append("  }");
            if (i < storedMessages.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");
        try {
            Files.write(Paths.get(storedFilePath), json.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Could not write the JSON file: " + e.getMessage());
        }
    }

    //Method: reads the JSON file into the stored messages array
    public static void loadStoredMessages() {
        storedMessages.clear();
        File file = new File(storedFilePath);
        if (!file.exists()) {
            return;
        }
        try {
            String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            ArrayList<String[]> tokens = tokenise(json);

            HashMap<String, String> current = null;
            for (int i = 0; i < tokens.size(); i++) {
                String type = tokens.get(i)[0];
                String value = tokens.get(i)[1];

                if (type.equals("symbol") && value.equals("{")) {
                    current = new HashMap<>();
                } else if (type.equals("symbol") && value.equals("}") && current != null) {
                    Message msg = new Message(
                            current.getOrDefault("messageID", generateMessageID()),
                            Integer.parseInt(current.getOrDefault("messageNumber", "0")),
                            current.getOrDefault("recipient", ""),
                            current.getOrDefault("message", ""));
                    msg.sender = current.getOrDefault("sender", currentSender);
                    addMessage(msg, "Stored");
                    current = null;
                } else if (type.equals("string") && current != null && i + 2 < tokens.size()) {
                    //A key is always followed by a colon and then its value
                    current.put(value, tokens.get(i + 2)[1]);
                    i = i + 2;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not read the JSON file: " + e.getMessage());
        }
    }

    //Method: escapes the characters JSON does not allow inside a string
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    //Method: breaks JSON text into tokens (strings, numbers and the symbols { } [ ] : ,)
    private static ArrayList<String[]> tokenise(String json) {
        ArrayList<String[]> tokens = new ArrayList<>();
        int i = 0;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '"') {
                StringBuilder text = new StringBuilder();
                i++;
                while (i < json.length() && json.charAt(i) != '"') {
                    char ch = json.charAt(i);
                    if (ch == '\\' && i + 1 < json.length()) {
                        i++;
                        char next = json.charAt(i);
                        if (next == 'n') {
                            text.append('\n');
                        } else if (next == 'r') {
                            text.append('\r');
                        } else if (next == 't') {
                            text.append('\t');
                        } else if (next == 'u' && i + 4 < json.length()) {
                            text.append((char) Integer.parseInt(json.substring(i + 1, i + 5), 16));
                            i = i + 4;
                        } else {
                            text.append(next);
                        }
                    } else {
                        text.append(ch);
                    }
                    i++;
                }
                tokens.add(new String[]{"string", text.toString()});
                i++;
            } else if (c == '{' || c == '}' || c == '[' || c == ']' || c == ':' || c == ',') {
                tokens.add(new String[]{"symbol", String.valueOf(c)});
                i++;
            } else if (Character.isDigit(c) || c == '-') {
                int start = i;
                while (i < json.length() && (Character.isDigit(json.charAt(i)) || json.charAt(i) == '-' || json.charAt(i) == '.')) {
                    i++;
                }
                tokens.add(new String[]{"number", json.substring(start, i)});
            } else {
                i++;
            }
        }
        return tokens;
    }
}
