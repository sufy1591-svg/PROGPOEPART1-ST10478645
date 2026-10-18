package com.mycompany.registrationloginpoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Sufyaan Bahadur
 *
 * Unit tests for the Message class, using the test data from the Part 2 brief.
 */
public class MessageTest {

    private Message message1;
    private Message message2;

    //Runs before every test so each test starts with fresh test data
    @BeforeEach
    public void setUp() {
        Message.clearMessages();

        //Test data for message 1 (the user selects Send)
        message1 = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");

        //Test data for message 2 (the user selects Discard)
        message2 = new Message("0812345678", 1, "08575975889", "Hi Keegan, did you receive the payment?");
    }

    //===== Message length tests =====

    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testMessageLength_Failure() {
        //a message that is 260 characters long, which is 10 over the limit
        String longText = "a".repeat(260);
        Message longMessage = new Message("0012345678", 0, "+27718693002", longText);

        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", longMessage.checkMessageLength());
    }

    //===== Recipient cell number tests =====

    @Test
    public void testRecipientCell_Success() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_Failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", message2.checkRecipientCell());
    }

    //===== Message hash tests =====

    @Test
    public void testMessageHash_TestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void testMessageHash_AllMessagesInLoop() {
        String[] ids = {"0012345678", "0812345678"};
        int[] numbers = {0, 1};
        String[] messages = {"Hi Mike, can you join us for dinner tonight?", "Hi Keegan, did you receive the payment?"};
        String[] expectedHashes = {"00:0:HITONIGHT", "08:1:HIPAYMENT"};

        //test the hash of every message in a loop
        for (int i = 0; i < messages.length; i++) {
            Message msg = new Message(ids[i], numbers[i], "+27718693002", messages[i]);
            assertEquals(expectedHashes[i], msg.createMessageHash());
        }
    }

    //===== Message ID tests =====

    @Test
    public void testMessageID_Created() {
        Message msg = new Message(0);

        assertEquals("Message ID generated: " + msg.getMessageID(), msg.displayMessageID());
        assertEquals(10, msg.getMessageID().length());
    }

    @Test
    public void testCheckMessageID_NotMoreThanTenCharacters() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    public void testCheckMessageID_MoreThanTenCharacters() {
        Message msg = new Message("00123456789", 0, "+27718693002", "Hi there");
        assertFalse(msg.checkMessageID());
    }

    //===== Send / disregard / store tests =====

    @Test
    public void testMessageSent_Send() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    public void testMessageSent_Disregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    public void testMessageSent_Store() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    //===== Total messages and printing tests =====

    @Test
    public void testReturnTotalMessages() {
        message1.sentMessage(1); //sent
        message2.sentMessage(2); //disregarded, so it is not counted

        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages_ContainsSentMessage() {
        message1.sentMessage(1);

        String output = Message.printMessages();

        assertTrue(output.contains("00:0:HITONIGHT"));
        assertTrue(output.contains("+27718693002"));
    }
}