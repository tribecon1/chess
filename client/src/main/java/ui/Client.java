package ui;

import chess.ChessGame;
import chess.ChessPosition;
import model.GameData;
import model.UserData;
import net.ServerFacade;
import net.ServerMessageObserver;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import response.ListGamesResponse;

import java.io.IOException;
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

    private static final PrintStream OUT = System.out;
    private static final Scanner TERMINAL_READER = new Scanner(System.in);
    private String currUser;
    private String authToken;
    private ChessGame.TeamColor teamColor;
    private static ServerFacade serverFacade;
    private static boolean forfeit = false;

    public Client(){
        this.currUser = null;
        this.authToken = null;
        this.teamColor = null;
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
                        OUT.println("Register failed! -> " + resultText);
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
                            for (int rowNum = 1; rowNum <= listOfGames.size(); ++rowNum){
                                OUT.print(rowNum + "." + gameDataInterpreter(listOfGames.get(rowNum-1)));
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
                        //OUT.println("User " + "\"" + this.currUser + "\"" + resultText + ", playing as " + newJoinReq.playerColor() + " team!");
                        //do NOT print game immediately after, print after the connect notification is returned, COMMENT OUT BELOW
                        this.teamColor = ChessGame.TeamColor.valueOf(newJoinReq.playerColor().toUpperCase());
                        serverFacade.connect(resultText, this.authToken);
                        /*switch(newJoinReq.playerColor().toUpperCase()){
                            case "WHITE" -> ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame(), null);
                            case "BLACK" -> ChessBoardDrawer.createBoardBlackOrientation(OUT, new ChessGame(), null);
                        }*/
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
                        OUT.println(resultText);
                        ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame(), null);
                    }
                    break;
                default:
                    OUT.println("ERROR! Unknown command -> " + "\"" + userResponse + "\"");
            }
        }
        startupMenu();
    }


    public void gameplayMenu() {
        OUT.print(SET_TEXT_COLOR_WHITE);
        boolean gameInSession = true; //to be replaced with forfeit?
        helpGameplayOptions(OUT);
        while(gameInSession){
            OUT.println("What would you like to do? (Type \"help\" to see your available commands!)");
            OUT.print(">>> ");
            String userResponse = TERMINAL_READER.nextLine();
            switch(userResponse.toUpperCase()){
                case "HELP":
                    helpGameplayOptions(OUT);
                    break;
                case "REDRAW":
                    switch (this.teamColor){
                        case WHITE -> ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame(), null);
                        case BLACK -> ChessBoardDrawer.createBoardBlackOrientation(OUT, new ChessGame(), null);
                    }
                    break;
                case "LEAVE":
                    //do code
                    OUT.println("Leaving game and returning to menu...");
                    gameInSession = false;
                    break;
                case "MOVE":
                    //do code
                    break;
                case "RESIGN":
                    //do code
                    forfeit = true; //so that no more moves can be made
                    break;
                case "HIGHLIGHT":
                    String chosenPiecePos = pieceHighlightedMoves(OUT, TERMINAL_READER);
                    switch (this.teamColor){
                        case WHITE -> ChessBoardDrawer.createBoardWhiteOrientation(OUT, new ChessGame(), new ChessPosition(chosenPiecePos));
                        case BLACK -> ChessBoardDrawer.createBoardBlackOrientation(OUT, new ChessGame(), new ChessPosition(chosenPiecePos));
                    }
                    break;
                default:
                    OUT.println("ERROR! Unknown command -> " + "\"" + userResponse + "\"");
            }
        }
    }


    //to be added to!!
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
    }

    public void displayError(String message) {
        OUT.print(SET_BG_COLOR_RED);
        OUT.print(SET_TEXT_COLOR_WHITE);
        OUT.print(message);
    }

    public void loadGame(String givenGameJson) {
        if (this.teamColor.equals(ChessGame.TeamColor.BLACK)){
            ChessBoardDrawer.createBoardBlackOrientation(OUT, SerializerDeserializer.convertFromJSON(givenGameJson, ChessGame.class), null);
        }
        else{ //White team OR observer
            ChessBoardDrawer.createBoardWhiteOrientation(OUT, SerializerDeserializer.convertFromJSON(givenGameJson, ChessGame.class), null);
        }
    }


}
