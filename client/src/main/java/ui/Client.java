package ui;

import java.io.PrintStream;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private static String authToken = null;

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
        OUT.println("Type \"help\" to see your options, or enter your desired action here!:");
        String userResponse = TERMINAL_READER.nextLine().toUpperCase();
        switch(userResponse){
            case "HELP":
                helpMenuOptions();
                break;
            case "LOGIN":
                OUT.println("Login code here!");
                break;
            case "REGISTER":
                OUT.println("Register code here!");
                break;
            case "QUIT":
                OUT.println("Closing your connection to the Chess Server...");
                System.exit(0);
        }

    }


    private static void helpMenuOptions(){
        OUT.println("Here are the following commands: ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("   1. Register - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<USERNAME> <PASSWORD> <EMAIL> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("to register a new user!");
        OUT.println();

        OUT.print("   2. Login - Provide an existing ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<USERNAME> <PASSWORD> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("to login!");
        OUT.println();

        OUT.print(SET_TEXT_COLOR_RED);
        OUT.println("   3. Quit your current session");
        OUT.print(SET_TEXT_COLOR_WHITE);
    }

}
