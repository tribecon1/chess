package ui;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import net.ServerFacade;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import response.ListGamesResponse;
import server.SerializerDeserializer;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import static ui.ClientMenus.*;
import static ui.EscapeSequences.*;

public class Client {

    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private static String currUser = null;
    private static String authToken = null;
    private static ServerFacade serverFacade;

    public static void main(String[] args) throws IOException {
        serverFacade = new ServerFacade(8080);
        startupMenu();
    }


    public static void startupMenu() throws IOException {
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.println("***********************************************************************************");
        OUT.println("♖ Welcome to Bentley's CS 240 Chess Fest! ♜");
        OUT.println("***********************************************************************************");
        OUT.println();

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
                    String resultText = serverFacade.register(newUser);
                    if (resultText.contains("Error")){
                        OUT.println("Register failed!: " + resultText);
                    }
                    else{
                        currUser = newUser.username();
                        authToken = resultText;
                    }
                    break;
                case "LOGIN":
                    LoginRequest loginRequest = loginVerifySteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.login(loginRequest);
                    if (resultText.contains("Error")){
                        OUT.println("Login failed! -> " + resultText);
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
                OUT.println("ERROR! Unknown command -> " + "\"" + userResponse + "\"");
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

            String resultText;
            String userResponse = TERMINAL_READER.nextLine();

            switch(userResponse.toUpperCase()){
                case "HELP":
                    helpAuthorizedOptions(OUT);
                    break;
                case "LOGOUT":
                    resultText = serverFacade.logout(authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Logout failed! -> " + resultText);
                    }
                    else{
                        OUT.println(resultText + "\"" + currUser + "\"!");
                        OUT.println("Thank you for joining us! Returning to start menu...");
                        OUT.println();
                        currUser = null;
                        authToken = null;
                    }
                    break;
                case "CREATE":
                    CreateGameRequest newGame = createSteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.createGame(newGame, authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Register failed! -> " + resultText);
                    }
                    else{
                        OUT.println("Your game was created! Its ID # is: " + resultText);
                    }
                    break;
                case "LIST":
                    resultText = serverFacade.listGames(authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Getting a list of games failed! -> " + resultText);
                    }
                    else{
                        ArrayList<GameData> listOfGames = new ArrayList<>(SerializerDeserializer.convertFromJSON(resultText, ListGamesResponse.class).games());
                        if (listOfGames.isEmpty()){
                            OUT.println("There are no games created yet!");
                        }
                        else{
                            for (int rowNum = 1; rowNum <= listOfGames.size(); ++rowNum){
                                OUT.print(rowNum + ". " + gameDataInterpreter(listOfGames.get(rowNum-1)));
                            }
                        }
                        OUT.println();
                    }
                    break;
                case "JOIN":
                    JoinGameRequest newJoinReq = joinSteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.joinGame(newJoinReq, authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Join failed! -> " + resultText);
                    }
                    else{
                        OUT.println("User " + "\"" + currUser + "\"" + resultText + ", playing as " + newJoinReq.playerColor() + " team!");
                        ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame().getBoard());
                        ChessBoardDrawer.createBoardBlackOrientation(OUT, new ChessGame().getBoard());
                    }
                    break;
                case "OBSERVE":
                    String givenGameID = observeSteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.observeGame(givenGameID, authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Join failed! -> " + resultText);
                    }
                    else{
                        OUT.println(resultText);
                        ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame().getBoard());
                        ChessBoardDrawer.createBoardBlackOrientation(OUT, new ChessGame().getBoard());
                    }
                    break;
                default:
                    OUT.println("ERROR! Unknown command -> " + "\"" + userResponse + "\"");
            }
        }
        startupMenu();
    }


}
