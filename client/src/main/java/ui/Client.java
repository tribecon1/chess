package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import model.UserData;
import net.ServerFacade;
import net.ServerMessageObserver;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import response.ListGamesResponse;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import static ui.ClientMenus.*;
import static ui.EscapeSequences.*;

import server.SerializerDeserializer;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class Client implements ServerMessageObserver {
    //create static lock obj.
    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private String currUser;
    private String authToken;
    private ChessGame.TeamColor teamColor;
    private ChessGame currGame;
    private int gameID;
    private static ServerFacade serverFacade;

    public Client(){
        this.currUser = null;
        this.authToken = null;
        this.teamColor = null;
        this.currGame = null;
        this.gameID = 0; //use this to loop the gameplay menu
    }


    public static void main(String[] args) throws Exception {
        Client currClient = new Client();
        serverFacade = new ServerFacade(8080, currClient);
        currClient.startupMenu();
    }


    public void startupMenu() throws Exception {
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.println("***********************************************************************************");
        OUT.println("♖ Welcome to Bentley's CS 240 Chess Fest! ♜");
        OUT.println("***********************************************************************************");
        OUT.println();

        while(this.authToken == null){
            OUT.println("Type \"help\" to see your options, or enter your desired action here!:");
            OUT.print(">>> ");

            String resultText;
            String userResponse = TERMINAL_READER.nextLine();
            switch(userResponse.toUpperCase()){
                case "HELP":
                    helpMenuOptions(OUT);
                    break;
                case "REGISTER":
                    UserData newUser = registerInfoSteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.register(newUser);
                    if (resultText.contains("Error")){
                        OUT.println("Register failed!: " + resultText);
                    }
                    else{
                        this.currUser = newUser.username();
                        this.authToken = resultText;
                    }
                    break;
                case "LOGIN":
                    LoginRequest loginRequest = loginVerifySteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.login(loginRequest);
                    if (resultText.contains("Error")){
                        OUT.println("Login failed! -> " + resultText);
                    }
                    else{
                        this.currUser = loginRequest.username();
                        this.authToken = resultText;
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

    public void authorizedMenu() throws Exception {
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.println("***********************************************************************************");
        OUT.print("Welcome user \"" + this.currUser + "\"! ");
        while(this.authToken != null){
            OUT.println("What would you like to do today? Type \"help\" to see your available commands!");
            OUT.print(">>> ");

            String resultText;
            String userResponse = TERMINAL_READER.nextLine();

            switch(userResponse.toUpperCase()){
                case "HELP":
                    helpAuthorizedOptions(OUT);
                    break;
                case "LOGOUT":
                    resultText = serverFacade.logout(this.authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Logout failed! -> " + resultText);
                    }
                    else{
                        OUT.println(resultText + "\"" + this.currUser + "\"!");
                        OUT.println("Thank you for joining us! Returning to start menu...");
                        OUT.println();
                        this.currUser = null;
                        this.authToken = null;
                    }
                    break;
                case "CREATE":
                    CreateGameRequest newGame = createSteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.createGame(newGame, this.authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Create game failed! -> " + resultText);
                    }
                    else{
                        OUT.println(resultText);
                    }
                    break;
                case "LIST":
                    resultText = serverFacade.listGames(this.authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Getting a list of games failed! -> " + resultText);
                    }
                    else{
                        ArrayList<GameData> listOfGames = new ArrayList<>(SerializerDeserializer.convertFromJSON(resultText, ListGamesResponse.class).games());
                        if (listOfGames.isEmpty()){
                            OUT.println("There are no games created yet!");
                        }
                        else{
                            OUT.println();
                            for (int rowNum = 1; rowNum <= listOfGames.size(); ++rowNum){
                                OUT.print(rowNum + "." + gameDataInterpreter(OUT, listOfGames.get(rowNum-1)));
                            }
                        }
                        OUT.println();
                    }
                    break;
                case "JOIN":
                    JoinGameRequest newJoinReq = joinSteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.joinGame(newJoinReq, this.authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Join failed! -> " + resultText);
                    }
                    else{
                        this.teamColor = ChessGame.TeamColor.valueOf(newJoinReq.playerColor().toUpperCase());
                        this.gameID = Integer.parseInt(resultText);
                        serverFacade.connect(this.gameID, this.authToken);
                        gameplayMenu();
                    }
                    break;
                case "OBSERVE":
                    String givenGameID = observeSteps(OUT, TERMINAL_READER);
                    resultText = serverFacade.observeGame(givenGameID, this.authToken);
                    if (resultText.contains("Error")){
                        OUT.println("Observe failed! -> " + resultText);
                    }
                    else{
                        OUT.println("Enjoy watching the game! Type \"leave\" when you want to leave the game and return to the menu!");
                        this.gameID = Integer.parseInt(resultText);
                        serverFacade.connect(this.gameID, this.authToken);
                        while(this.gameID != 0){
                            String observerResponse = TERMINAL_READER.nextLine();
                            if (observerResponse.equalsIgnoreCase("LEAVE")){
                                serverFacade.leave(this.gameID, this.authToken);
                                this.gameID = 0;
                            }
                            else{
                                OUT.println("ERROR! Unknown command -> " + "\"" + observerResponse + "\"");
                            }
                        }
                    }
                    break;
                default:
                    OUT.print(SET_TEXT_COLOR_WHITE);
                    OUT.println("ERROR! Unknown command -> " + "\"" + userResponse + "\"");
            }
        }
        startupMenu();
    }


    public void gameplayMenu() throws Exception {
        OUT.print(SET_TEXT_COLOR_WHITE);
        helpGameplayOptions(OUT);
        while(this.gameID != 0){
            //OUT.println("What would you like to do? (Type \"help\" to see your available commands!)");
//            OUT.print(">>> ");
            String userResponse = TERMINAL_READER.nextLine();
            OUT.println();
            switch(userResponse.toUpperCase()){
                case "HELP":
                    helpGameplayOptions(OUT);
                    break;
                case "REDRAW":
                    switch (this.teamColor){
                        case WHITE -> ChessBoardDrawer.createBoardWhiteOrientation(OUT, this.currGame, null);
                        case BLACK -> ChessBoardDrawer.createBoardBlackOrientation(OUT, this.currGame, null);
                    }
                    break;
                case "LEAVE":
                    serverFacade.leave(this.gameID, this.authToken);
                    OUT.print(SET_TEXT_COLOR_WHITE);
                    OUT.println("Leaving game and returning to menu...");
                    this.currGame = null;
                    this.gameID = 0;
                    break;
                case "MOVE":
                    ChessMove desiredMove = makeMoveSteps(OUT, TERMINAL_READER); //BE WARY OF HOW THIS MENU WORKS
                    serverFacade.move(this.gameID,this.authToken, desiredMove);
                    break;
                case "RESIGN":
                    serverFacade.resign(this.gameID, this.authToken);
                    break;
                case "HIGHLIGHT":
                    String chosenPiecePos = pieceHighlightedMoves(OUT, TERMINAL_READER);
                    switch (this.teamColor){
                        case WHITE -> ChessBoardDrawer.createBoardWhiteOrientation(OUT, this.currGame, new ChessPosition(chosenPiecePos));
                        case BLACK -> ChessBoardDrawer.createBoardBlackOrientation(OUT, this.currGame, new ChessPosition(chosenPiecePos));
                    }
                    break;
                default:
                    OUT.print(SET_TEXT_COLOR_WHITE);
                    OUT.println("ERROR! Unknown command -> " + "\"" + userResponse + "\"");
                    OUT.println("What would you like to do? (Type \"help\" to see your available commands!)");
            }
        }
    }

    @Override
    public void notify(String jsonServerMessage) {

        ServerMessage message = SerializerDeserializer.convertFromJSON(jsonServerMessage, ServerMessage.class);

        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(SerializerDeserializer.convertFromJSON(jsonServerMessage, NotificationMessage.class).getMessage());
            case ERROR -> displayError(SerializerDeserializer.convertFromJSON(jsonServerMessage, ErrorMessage.class).getErrorMessage());
            case LOAD_GAME -> loadGame(SerializerDeserializer.convertFromJSON(jsonServerMessage, LoadGameMessage.class).getGame());
        }
    }

    public void displayNotification(String message) {
        OUT.print(SET_BG_COLOR_BLUE);
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.print(message);
        OUT.println(SET_DEFAULT_BG_COLOR);
        OUT.print(SET_TEXT_COLOR_WHITE);
    }

    public void displayError(String message) {
        OUT.print(SET_BG_COLOR_RED);
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.print(message);
        OUT.println(SET_DEFAULT_BG_COLOR);
        OUT.println("What would you like to do? (Type \"help\" to see your available commands!)");
        OUT.print(SET_TEXT_COLOR_WHITE);
    }

    public void loadGame(String givenGameJson) {
        ChessGame receivedGame = SerializerDeserializer.convertFromJSON(givenGameJson, ChessGame.class);
        this.currGame = receivedGame;
        if (this.teamColor == null){
            ChessBoardDrawer.createBoardWhiteOrientation(OUT, receivedGame, null);
            return;
        }
        else if(this.teamColor.equals(ChessGame.TeamColor.WHITE)){
            ChessBoardDrawer.createBoardWhiteOrientation(OUT, receivedGame, null);
        }
        else{ //White team OR observer
            ChessBoardDrawer.createBoardBlackOrientation(OUT, receivedGame, null);
        }
        OUT.print("What would you like to do? (Type \"help\" to see your available commands!)");
        OUT.println(SET_DEFAULT_BG_COLOR);
        OUT.print(SET_TEXT_COLOR_WHITE);
    }


}