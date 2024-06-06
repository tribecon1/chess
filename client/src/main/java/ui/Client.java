package ui;

import chess.ChessGame;
import model.UserData;
import net.ClientCommunicator;
import net.ServerFacade;
import request.LoginRequest;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private static String currUser = null;
    private static String authToken = null;

    public static void main(String[] args) throws IOException {
        startupMenu();
    }


    public static void startupMenu() throws IOException {
        OUT.println("***********************************************************************************");
        OUT.println("♖ Welcome to Bentley's CS 240 Chess Fest! ♜");
        OUT.println("***********************************************************************************");
        OUT.println();
        OUT.print(SET_TEXT_COLOR_WHITE);

        while(authToken == null){
            OUT.println("Type \"help\" to see your options, or enter your desired action here!:");
            OUT.print(">>> ");

            String userResponse = TERMINAL_READER.nextLine();

            switch(userResponse.toUpperCase()){
                case "HELP":
                    helpMenuOptions();
                    break;
                case "REGISTER":
                    UserData newUser = registerInfoSteps();
                    String resultText = ServerFacade.register(newUser);
                    if (resultText.contains("Error")){
                        OUT.println("Register failed!: " + resultText);
                    }
                    else{
                        currUser = newUser.username();
                        authToken = resultText;
                    }
                    //authToken = "pass"; //TEST
                    break;
                case "LOGIN":
                    LoginRequest loginRequest = loginVerifySteps();
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

    public static void authorizedMenu() throws IOException {
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.println("***********************************************************************************");
        OUT.print("Welcome user \"" + currUser + "\"! ");
        while(authToken != null){
            OUT.println("What would you like to do today? Type \"help\" to see your available commands!");
            OUT.print(">>> ");

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
                    //temporary
                    ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame().getBoard());
                    ChessBoardDrawer.createBoardBlackOrientation(OUT, new ChessGame().getBoard());
                    break;
                case "OBSERVE":
                    //temporary
                    ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame().getBoard());
                    ChessBoardDrawer.createBoardBlackOrientation(OUT, new ChessGame().getBoard());
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
        OUT.println("to register a new user!");

        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("   * Login - Provide an existing ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<USERNAME> <PASSWORD> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to login!");

        OUT.print(SET_TEXT_COLOR_MAGENTA);
        OUT.println("   * Help - List of available commands");

        OUT.print(SET_TEXT_COLOR_RED);
        OUT.println("   * Quit your current session");
        OUT.println();
        OUT.print(SET_TEXT_COLOR_WHITE);
    }

    private static void helpAuthorizedOptions(){
        OUT.println("Here are the following available commands: ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("   * Create - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<gameName> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to create a new game of chess!");

        OUT.println("   * List - to list all current chess games!");

        OUT.print("   * Join - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<gameID> [White|Black] ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to join and play in an existing game of chess!");

        OUT.print("   * Observe - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<gameID> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to watch an active game of chess!");

        OUT.println("   * Logout - When you are finished! ");

        OUT.print(SET_TEXT_COLOR_MAGENTA);
        OUT.println("   * Help - List of available commands");
        OUT.println();
        OUT.print(SET_TEXT_COLOR_WHITE);
    }


    private static UserData registerInfoSteps(){
        OUT.println("Create your username:");
        OUT.print("> ");
        String username = TERMINAL_READER.nextLine();
        while(username.isEmpty()){
            OUT.println("Username may not be blank, please try again: ");
            username = TERMINAL_READER.nextLine();
        }
        OUT.println("Create your password:");
        OUT.print("> ");
        String password = TERMINAL_READER.nextLine();
        while(password.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            password = TERMINAL_READER.nextLine();
        }
        OUT.println("Enter your email:");
        OUT.print("> ");
        String email = TERMINAL_READER.nextLine();
        while(email.isEmpty()){
            OUT.println("Email may not be blank, please try again:");
            email = TERMINAL_READER.nextLine();
        }
        return new UserData(username, password, email);
    }

    private static LoginRequest loginVerifySteps(){
        OUT.println("Enter your username:");
        OUT.print("> ");
        String username = TERMINAL_READER.nextLine();
        while(username.isEmpty()){
            OUT.println("Username may not be blank, please try again: ");
            username = TERMINAL_READER.nextLine();
        }
        OUT.println("Enter your password:");
        OUT.print("> ");
        String password = TERMINAL_READER.nextLine();
        while(password.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            password = TERMINAL_READER.nextLine();
        }
        return new LoginRequest(username, password);
    }


}
