package ui;

import model.UserData;
import net.ClientCommunicator;
import request.LoginRequest;

import java.io.PrintStream;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private static String currUser = null;
    private static String authToken = null;

    public static void main(String[] args){
        ClientCommunicator clientCommunicator = new ClientCommunicator("http://localhost:8080/");
        startupMenu();
    }


    public static void startupMenu(){
        OUT.println("***********************************************************************************");
        OUT.println("♖ Welcome to Bentley's CS 240 Chess Fest! ♜");
        OUT.println("***********************************************************************************");
        OUT.println();
        OUT.print(SET_TEXT_COLOR_WHITE);

        while(authToken == null){
            OUT.println("Type \"help\" to see your options, or enter your desired action here!:");

            String userResponse = TERMINAL_READER.nextLine().toUpperCase();

            switch(userResponse){
                case "HELP":
                    helpMenuOptions();
                    break;
                case "REGISTER":
                    UserData newUser = registerInfoSteps();
                    currUser = newUser.username();
                    //authToken = res.body();
                    break;
                case "LOGIN":
                    loginVerifySteps();
                    //authToken = res.body();
                    break;
                case "QUIT":
                    OUT.println("Closing your connection to the Chess Server...");
                    TERMINAL_READER.close();
                    System.exit(0);
                default:
                OUT.println("ERROR! Unknown command: " + "\"" + userResponse + "\"");
            }
        }
        authorizedMenu();
    }

    public static void authorizedMenu(){
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.println("***********************************************************************************");
        OUT.print("Welcome user \"" + currUser + "\"! ");
        while(authToken != null){
            OUT.println("What would you like to do today? Type \"help\" to see your available commands!");

            String userResponse = TERMINAL_READER.nextLine().toUpperCase();

            switch(userResponse){
                case "HELP":
                    helpAuthorizedOptions();
                    break;
                case "LOGOUT":
                    //perform method
                    authToken = null;
                    break;
                case "CREATE":
                    //perform method
                    break;
                case "LIST":
                    //perform method
                    break;
                case "JOIN":
                    //perform method
                    break;
                case "OBSERVE":
                    //perform method
                    break;
                default:
                    OUT.println("ERROR! Unknown command: " + "\"" + userResponse + "\"");
            }
        }
        startupMenu();
    }





    private static void helpMenuOptions(){
        OUT.println("Here are the following commands: ");
        OUT.print(SET_TEXT_COLOR_GREEN);
        OUT.print("   * Register - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<USERNAME> <PASSWORD> <EMAIL> ");
        OUT.print(SET_TEXT_COLOR_GREEN);
        OUT.print("to register a new user!");
        OUT.println();

        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("   * Login - Provide an existing ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<USERNAME> <PASSWORD> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("to login!");
        OUT.println();

        OUT.print(SET_TEXT_COLOR_MAGENTA);
        OUT.println("   * Help - List of available commands");

        OUT.print(SET_TEXT_COLOR_RED);
        OUT.println("   * Quit your current session");
        OUT.print(SET_TEXT_COLOR_WHITE);
    }

    private static void helpAuthorizedOptions(){
        OUT.println("Here are the following available commands: ");
    }


    private static UserData registerInfoSteps(){
        OUT.println("Create your username:");
        String username = TERMINAL_READER.nextLine();
        while(username.isEmpty()){
            OUT.println("Username may not be blank, please try again: ");
            username = TERMINAL_READER.nextLine();
        }
        OUT.println("Create your password:");
        String password = TERMINAL_READER.nextLine();
        while(password.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            password = TERMINAL_READER.nextLine();
        }
        OUT.println("Enter your email:");
        String email = TERMINAL_READER.nextLine();
        while(email.isEmpty()){
            OUT.println("Email may not be blank, please try again:");
            email = TERMINAL_READER.nextLine();
        }
        return new UserData(username, password, email);
    }

    private static LoginRequest loginVerifySteps(){
        OUT.println("Enter your username:");
        String username = TERMINAL_READER.nextLine();
        while(username.isEmpty()){
            OUT.println("Username may not be blank, please try again: ");
            username = TERMINAL_READER.nextLine();
        }
        OUT.println("Enter your password:");
        String password = TERMINAL_READER.nextLine();
        while(password.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            password = TERMINAL_READER.nextLine();
        }
        return new LoginRequest(username, password);
    }


}
