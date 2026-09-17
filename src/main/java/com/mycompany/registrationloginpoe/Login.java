/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.mycompany.registrationloginpoe;

/**
 *
 * @author Sufyaan Bahadur
 *
 * This class handles user registration and login validation.
 * It checks that the username, password, and cell phone number
 * entered by the user meet the required formatting rules, and
 * verifies login credentials against the registered details.
 */
public class Login {

    //Attributes to store the details captured during registration
    private String userName;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    //Keeps track of the outcome of the most recent login attempt,
    //so returnLoginStatus() knows which message to give back
    private boolean loginSuccessful;

    //Constructor - captures all the details entered at registration
    public Login(String firstName, String lastName, String userName, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    //Method: checks that the username contains an underscore and
    //is no more than five characters long
    public boolean checkUserName() {
        return (userName != null && userName.contains("_") && userName.length() <= 5);
    }

    //Method: checks that the password meets the complexity rules -
    //at least eight characters, a capital letter, a number, and a special character
    public boolean checkPasswordComplexity() {
        if (password == null) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        return password.matches(regex);
    }

    //Method: checks that the cell phone number contains the international
    //country code (+27) followed by a number no more than ten characters long
    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "^\\+27[0-9]{1,10}$";
        return cellPhoneNumber.matches(regex);
    }

    //Method: registers the user, checking the username and password
    //rules, and returns the appropriate message
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        } else if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        } else {
            return "Username successfully captured.\nPassword successfully captured.";
        }
    }

    //Method: returns the cell phone number message on its own,
    //since the brief gives it a separate condition/message table
    public String checkCellPhoneNumberMessage() {
        if (checkCellPhoneNumber()) {
            return "Cell phone number successfully added.";
        } else {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
    }

    //Method: verifies that the entered login details match the
    //details captured at registration
    public boolean loginUser(String enteredUserName, String enteredPassword) {
        loginSuccessful = userName.equals(enteredUserName) && password.equals(enteredPassword);
        return loginSuccessful;
    }

    //Method: returns the message for the most recent login attempt
    public String returnLoginStatus() {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    //Getters - used by the console app and by the unit tests
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }
}