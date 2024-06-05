package ui;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private static String authToken = null;
    private static final List<String> STARTUP_COMMANDS = new ArrayList<>(Arrays.asList("REGISTER", "LOGIN", "QUIT"));
    private static boolean badCommand = false;
    private static boolean helpLoop = false;

    public static void main(String[] args){
        while (authToken == null){
            startupMenu();
        }
        /*while (authToken != null){
            //code
        }*/

    }


    public static void startupMenu(){
        OUT.println("***********************************************************************************");
        OUT.println("♖ Welcome to Bentley's CS 240 Chess Fest! ♜");
        OUT.println("***********************************************************************************");
        OUT.println();
        OUT.print(SET_TEXT_COLOR_WHITE);

        while(true){
            OUT.println("Type \"help\" to see your options, or enter your desired action here!:");

            String userResponse = TERMINAL_READER.nextLine().toUpperCase();

            switch(userResponse){
                case "HELP":
                    helpMenuOptions();
                    break;
                case "REGISTER":
                    registerInfoSteps();
                    break;
                case "LOGIN":
                    OUT.println("Login code here!");
                    break;
                case "QUIT":
                    OUT.println("Closing your connection to the Chess Server...");
                    TERMINAL_READER.close();
                    System.exit(0);
                default:
                OUT.println("ERROR! Unknown command: " + "\"" + userResponse + "\"");
            }
        }
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

    private static void registerInfoSteps(){
        OUT.println("Create your username:");
        String userResponse = TERMINAL_READER.nextLine();
        while(userResponse.isEmpty()){
            OUT.println("Username may not be blank, please try again: ");
            userResponse = TERMINAL_READER.nextLine();
        }
        OUT.println("Create your password:");
        userResponse = TERMINAL_READER.nextLine();
        while(userResponse.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            userResponse = TERMINAL_READER.nextLine();
        }
        OUT.println("Enter your email:");
        userResponse = TERMINAL_READER.nextLine();
        while(userResponse.isEmpty()){
            OUT.println("Email may not be blank, please try again:");
            userResponse = TERMINAL_READER.nextLine();
        }
    }

}
