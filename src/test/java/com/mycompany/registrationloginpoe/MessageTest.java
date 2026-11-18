package com.mycompany.registrationloginpoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;





/**
 *
 * @author Sufy
 */
public class MessageTest {

    //The test data from the brief - entered by the developer
    private static final String[] IDS = {"1000000001", "1000000002", "1000000003", "1000000004", "1000000005"};
    private static final String[] RECIPIENTS = {"+27834557896", "+27838884567", "+27834484567", "0838884567", "+27838884567"};
    private static final String[] TEXTS = {
        "Did you get the cake?",
        "Where are you? You are late! I have asked you to be on time.",
        "Yohoooo, I am at your gate.",
        "It is dinner time!",
        "Ok, I am leaving without you."};
    private static final String[] FLAGS = {"Sent", "Stored", "Disregard", "Sent", "Stored"};

    //Runs before every test so each test starts with empty arrays and its own JSON file
    @BeforeEach
    public void setUp() throws IOException {
        Message.clearAll();
        Message.setCurrentSender("Test Sender");
        File tempFile = File.createTempFile("storedMessagesTest", ".json");
        tempFile.deleteOnExit();
        Message.setStoredFilePath(tempFile.getAbsolutePath());
    }

    //Method: loads the first few test messages into the arrays
    private void loadTestData(int howMany) {
        for (int i = 0; i < howMany; i++) {
            Message msg = new Message(IDS[i], i + 1, RECIPIENTS[i], TEXTS[i]);
            Message.addMessage(msg, FLAGS[i]);
        }
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        loadTestData(4);
        assertEquals(2,Message.getSentMessages().size());
        assertEquals("Did you get the cake?", Message.getSentMessages().get(0).getMessageText());
        assertEquals("It is dinner time!", Message.getSentMessages().get(1).getMessageText());
    }

    @Test
    public void testDisplayLongestMessage() {
        loadTestData(4);
        Message longest = Message.findLongestMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest.getMessageText());
    }

    @Test
    public void testSearchForMessageID() {
        loadTestData(4);
        Message found = Message.findByMessageID("1000000004");
        assertEquals("0838884567", found.getRecipient());
        assertEquals("It is dinner time!", found.getMessageText());
    }

    @Test
    public void testSearchMessagesForRecipient() {
        loadTestData(5);
        ArrayList<String> results = Message.searchByRecipient("+27838884567");
        assertEquals(2, results.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", results.get(0));
        assertEquals("Ok, I am leaving without you.", results.get(1));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        loadTestData(5);
        String hash = Message.getStoredMessages().get(0).createMessageHash();
        assertEquals("10:2:WHERETIME", hash);

        String expected = "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.";
        assertEquals(expected, Message.deleteByHash(hash));

        //Only test message 5 is left in the stored array
        assertEquals(1, Message.getStoredMessages().size());
        assertEquals("Ok, I am leaving without you.", Message.getStoredMessages().get(0).getMessageText());
    }

    @Test
    public void testDeleteMessageHashNotFound() {
        loadTestData(5);
        assertEquals("Message hash not found.", Message.deleteByHash("00:0:NOTHERE"));
        assertEquals(2, Message.getStoredMessages().size());
    }

    @Test
    public void testDisplayReport() {
        loadTestData(4);
        String expected = "===== MESSAGE REPORT =====\n"
                + "Message ID   : 1000000001\n"
                + "Message Hash : 10:1:DIDCAKE\n"
                + "Sender       : Test Sender\n"
                + "Recipient    : +27834557896\n"
                + "Message      : Did you get the cake?\n"
                + "--------------------\n"
                + "Message ID   : 1000000004\n"
                + "Message Hash : 10:4:ITTIME\n"
                + "Sender       : Test Sender\n"
                + "Recipient    : 0838884567\n"
                + "Message      : It is dinner time!\n"
                + "--------------------\n";
        assertEquals(expected, Message.displayReport(Message.getSentMessages()));
    }

    @Test
    public void testDisplaySenderAndRecipient() {
        loadTestData(5);
        String expected = "Sender: Test Sender | Recipient: +27838884567\n"
                + "Sender: Test Sender | Recipient: +27838884567\n";
        assertEquals(expected, Message.displaySenderAndRecipient());
    }

    @Test
    public void testDisregardedMessagesArrayAndIDsAndHashes() {
        loadTestData(5);
        assertEquals(1, Message.getDisregardedMessages().size());
        assertEquals("Yohoooo, I am at your gate.", Message.getDisregardedMessages().get(0).getMessageText());
        assertEquals(5, Message.getMessageIDs().size());
        assertEquals(5, Message.getMessageHashes().size());
    }

    @Test
    public void testStoredMessagesReadFromJsonFile() {
        loadTestData(5);
        Message.saveStoredMessages();

        //Empty the arrays, then read the JSON file back into the stored array
        Message.clearAll();
        Message.loadStoredMessages();

        assertEquals(2, Message.getStoredMessages().size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.getStoredMessages().get(0).getMessageText());
        assertEquals("Ok, I am leaving without you.", Message.getStoredMessages().get(1).getMessageText());
        assertEquals("+27838884567", Message.getStoredMessages().get(1).getRecipient());
    }

    @Test
    public void testJsonKeepsQuotesAndSpecialCharacters() {
        Message msg = new Message("1000000009", 9, "+27831234567", "She said \"hello\" and {left}\\");
        Message.addMessage(msg, "Stored");
        Message.saveStoredMessages();

        Message.clearAll();
        Message.loadStoredMessages();

        assertEquals("She said \"hello\" and {left}\\", Message.getStoredMessages().get(0).getMessageText());
    }
}
