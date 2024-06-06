package ui;

import chess.ChessGame;
import model.UserData;
import net.ClientCommunicator;
import net.ServerFacade;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static ui.ClientMenus.*;
import static ui.EscapeSequences.*;

public class Client {

    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private static String currUser = null;
    private static String authToken = null;

    public static void main(String[] args) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator("8080");
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
                    helpMenuOptions(OUT);
                    break;
                case "REGISTER":
                    UserData newUser = registerInfoSteps(OUT, TERMINAL_READER);
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
                    LoginRequest loginRequest = loginVerifySteps(OUT, TERMINAL_READER);
                    resultText = ServerFacade.login(loginRequest);
                    if (resultText.contains("Error")){
                        OUT.println("Register failed!: " + resultText);
                    }
                    else{
                        currUser = loginRequest.username();
                        authToken = resultText;
                    }
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
        OUT.println(authToken);
        while(authToken != null){
            OUT.println("What would you like to do today? Type \"help\" to see your available commands!");
            OUT.print(">>> ");

            String userResponse = TERMINAL_READER.nextLine().toUpperCase();

            switch(userResponse){
                case "HELP":
                    helpAuthorizedOptions(OUT);
                    break;
                case "LOGOUT":
                    //perform method
                    OUT.println("Thank you for joining us! Returning to start menu...");
                    authToken = null;
                    break;
                case "CREATE":
                    //perform method
                    CreateGameRequest newGame = createSteps(OUT, TERMINAL_READER);
                    String resultText = ServerFacade.createGame(newGame, authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Register failed!: " + resultText);
                    }
                    else{
                        OUT.println("Your game was created! Its ID # is: " + resultText);
                    }
                    break;
                case "LIST":
                    //perform method
                    break;
                case "JOIN":
                    //temporary
                    JoinGameRequest newJoinReq = joinSteps(OUT, TERMINAL_READER);
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



}
