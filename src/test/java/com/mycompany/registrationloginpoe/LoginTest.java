/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.registrationloginpoe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author Sufyaan Bahadur
 *
 * Unit tests for the Login class, using the test data specified in the PoE brief.
 */
public class LoginTest {

    //===== Username tests =====

    @Test
    public void testCheckUserName_CorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkUserName());
    }

    @Test
    public void testCheckUserName_IncorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.checkUserName());
    }

    @Test
    public void testRegisterUser_UsernameCorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username successfully captured.\nPassword successfully captured.", user.registerUser());
    }

    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", user.registerUser());
    }

    //===== Password tests =====

    @Test
    public void testCheckPasswordComplexity_MeetsRequirements() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexity_DoesNotMeetRequirements() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "password", "+27838968976");
        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testRegisterUser_PasswordMeetsRequirements() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username successfully captured.\nPassword successfully captured.", user.registerUser());
    }

    @Test
    public void testRegisterUser_PasswordDoesNotMeetRequirements() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", user.registerUser());
    }

    //===== Cell phone number tests =====

    @Test
    public void testCheckCellPhoneNumber_CorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumber_IncorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(user.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberMessage_CorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Cell phone number successfully added.", user.checkCellPhoneNumberMessage());
    }

    @Test
    public void testCellPhoneNumberMessage_IncorrectlyFormatted() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", user.checkCellPhoneNumberMessage());
    }

    //===== Login tests =====

    @Test
    public void testLoginUser_Successful() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginUser_Failed() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.loginUser("kyl_1", "wrongPassword"));
    }

    @Test
    public void testReturnLoginStatus_Successful() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        user.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Doe it is great to see you again.", user.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatus_Failed() {
        Login user = new Login("Kyle", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        user.loginUser("kyl_1", "wrongPassword");
        assertEquals("Username or password incorrect, please try again.", user.returnLoginStatus());
    }
}