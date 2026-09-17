/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationloginpoe;

import java.util.Scanner;

/**
 *
 * @author Sufyaan Bahadur
 *
 * Console-driven application for Part 1 - Registration and login feature.
 * No GUI / JOptionPane is used, as required by the brief.
 */
public class Main {

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
        Login user = new Login(firstName, lastName, userName, password, cellPhoneNumber);

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

            user.loginUser(loginUserName, loginPassword);
            System.out.println(user.returnLoginStatus());
        } else {
            System.out.println("\nRegistration was not successful, so login cannot continue.");
        }

        input.close();
    }
}