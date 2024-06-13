package websocket;


import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.SerializerDeserializer;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


@WebSocket
public class WebSocketHandler {

    private final AuthDao authDao;
    private final GameDao gameDao;
    private final HashMap<Integer, HashSet<ConnectionContainer>> clientConnectionsPerGame;

    public WebSocketHandler(AuthDao givenAuthDao, GameDao givenGameDao) {
        this.authDao = givenAuthDao;
        this.gameDao = givenGameDao;
        this.clientConnectionsPerGame = new HashMap<>();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String userMessage) throws Exception {
        try {
            UserGameCommand command = SerializerDeserializer.convertFromJSON(userMessage, UserGameCommand.class);
            String authToken = command.getAuthString();
            String username = authDao.getAuth(authToken).username();

            clientConnectionsPerGame.get(command.getGameID()).add(new ConnectionContainer(command.getAuthString(), session));

            switch (command.getCommandType()) {
                case CONNECT -> connect(new ConnectionContainer(authToken, session), username, SerializerDeserializer.convertFromJSON(userMessage, ConnectCommand.class));
                case MAKE_MOVE -> makeMove(new ConnectionContainer(authToken, session), username, SerializerDeserializer.convertFromJSON(userMessage, MoveCommand.class));
                case LEAVE -> leaveGame(new ConnectionContainer(authToken, session), username, SerializerDeserializer.convertFromJSON(userMessage, LeaveCommand.class));
                case RESIGN -> resign(new ConnectionContainer(authToken, session), username, SerializerDeserializer.convertFromJSON(userMessage, ResignCommand.class));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            send(session, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void broadcast(String authToken, int gameIDKey, ServerMessage message) throws Exception {
        var removeList = new ArrayList<ConnectionContainer>();
        for (var connContainer : clientConnectionsPerGame.get(gameIDKey)) {
            if (connContainer.session().isOpen()) {
                if (message instanceof LoadGameMessage){
                    send(connContainer.session(), message);
                }
                else if (!authToken.equals(connContainer.authToken())) { //only possible to be a NotificationMessage
                    send(connContainer.session(), message);
                }
            }
            else {
                removeList.add(connContainer);
            }
        }
        // Clean up any connections that were left open.
        for (var container : removeList) {
            clientConnectionsPerGame.get(gameIDKey).remove(container);
        }
    }

    public void send(Session session, ServerMessage message) throws Exception {
        session.getRemote().sendString(SerializerDeserializer.convertToJSON(message));
    }

    public void sendToAllClients(Session session, UserGameCommand givenCommand, ServerMessage message) throws Exception {
        send(session, message);
        broadcast(givenCommand.getAuthString(), givenCommand.getGameID(), message);
    }


    private void connect(ConnectionContainer connection, String username, ConnectCommand command) throws Exception {
        if (clientConnectionsPerGame.containsKey(command.getGameID())) {
            clientConnectionsPerGame.get(command.getGameID()).add(connection);
        }
        else{
            HashSet<ConnectionContainer> thisGameClients = new HashSet<>();
            thisGameClients.add(connection);
            clientConnectionsPerGame.put(command.getGameID(), thisGameClients);
            return;
        }
        String teamColor = "";
        GameData selectedGame = gameDao.getGame(command.getGameID());
        if (selectedGame != null) {
            if (selectedGame.whiteUsername().equals(username)) {
                teamColor = "White";
            }
            else if (selectedGame.blackUsername().equals(username)) {
                teamColor = "Black";
            }
            else{
                teamColor = "an observer";
            }
        }
        else{
            send(connection.session(), new ErrorMessage("Error: Invalid game ID chosen"));
        }
        var message = String.format("User \"" + username + "\" joined this game as " + teamColor);
        var notification = new NotificationMessage(message);
        broadcast(command.getAuthString(), command.getGameID(), notification);
    }

    private void makeMove(ConnectionContainer connection, String username, MoveCommand command) throws Exception {

        GameData selectedGame = gameDao.getGame(command.getGameID());
        if (selectedGame != null){
            ChessPiece pieceToBeMoved = selectedGame.game().getBoard().getPiece(command.getMove().getStartPosition());
            if (selectedGame.game().gameOver){
                send(connection.session(), new ErrorMessage("Error: Game is over, no more moves can be made")); //after a player RESIGNS or Checkmate is achieved
            }
            //PUT TURN CHECK HERE
            try{
                //make move();
                selectedGame.game().makeMove(command.getMove());
                gameDao.updateGame(selectedGame, selectedGame);
            }
            catch (InvalidMoveException e){
                send(connection.session(), new ErrorMessage(e.getMessage()));
            }

            else if (selectedGame.whiteUsername().equals(username) && selectedGame.game().getTeamTurn() != ChessGame.TeamColor.WHITE) {
                send(connection.session(), new ErrorMessage("Error: Not your turn to make a move, it is Black's turn"));
                else if (pieceToBeMoved.getTeamColor() != ChessGame.TeamColor.WHITE){
                    send(connection.session(), new ErrorMessage("Error: That piece belongs to Black's team"));
                }
            }
            else if (selectedGame.blackUsername().equals(username)) {
                if (selectedGame.game().getTeamTurn() != ChessGame.TeamColor.BLACK){
                    send(connection.session(), new ErrorMessage("Error: Not your turn to make a move, it is White's turn"));
                }
                else if(pieceToBeMoved.getTeamColor() != ChessGame.TeamColor.BLACK){
                    send(connection.session(), new ErrorMessage("Error: That piece belongs to White's team"));
                }
            }
            else if (!selectedGame.game().validMoves(command.getMove().getStartPosition()).contains(command.getMove())){
                send(connection.session(), new ErrorMessage("Error: Move is not valid, try using the \"Highlight\" command to see what your legal moves are"));
            }
            else{
                selectedGame.game().makeMove(command.getMove());
                gameDao.updateGame(selectedGame, selectedGame);
                if (selectedGame.game().isInCheck()){

                }
                else if ()
            }
        }
        else{
            send(connection.session(), new ErrorMessage("Error: Game does not exist"));
        }
    }

    private void leaveGame(ConnectionContainer connection, String username, LeaveCommand command) throws Exception {
        //removes your connection and your username from the hashmap
        GameData currGame = gameDao.getGame(command.getGameID());
        if (currGame != null){
            if (currGame.whiteUsername().equals(username)) {
                gameDao.updateGame(currGame, new GameData(currGame.gameID(), null, currGame.blackUsername(), currGame.gameName(), currGame.game()));
                broadcast(command.getAuthString(), command.getGameID(), new NotificationMessage(username + " left the game playing as White"));
            }
            else if (currGame.blackUsername().equals(username)) {
                gameDao.updateGame(currGame, new GameData(currGame.gameID(), currGame.whiteUsername(), null, currGame.gameName(), currGame.game()));
                broadcast(command.getAuthString(), command.getGameID(), new NotificationMessage(username + " left the game playing as Black"));
            }
            else{
                broadcast(command.getAuthString(), command.getGameID(), new NotificationMessage(username + " stopped observing the game"));
            }
            for (ConnectionContainer connContainer : clientConnectionsPerGame.get(command.getGameID())) {
                if (connContainer.authToken().equals(command.getAuthString())) {
                    connContainer.session().close();
                    clientConnectionsPerGame.get(command.getGameID()).remove(connContainer);
                }
            }
        }
        else{
            send(connection.session(), new ErrorMessage("Error: Invalid game ID chosen")); //this could never happen though
        }
    }

    private void resign(ConnectionContainer connection, String username, ResignCommand command) throws Exception {
        GameData currGame = gameDao.getGame(command.getGameID());
        if (!currGame.whiteUsername().equals(username) && !currGame.blackUsername().equals(username)) {
            send(connection.session(), new ErrorMessage("Error: Observers cannot resign from a game. Use \"leave\" command to exit the game"));
        }
        else{
            currGame.game().gameOver = true;
            gameDao.updateGame(currGame, currGame); //would this work since I need to put it back in the SQL database?
            NotificationMessage resignNotification;
            if (currGame.whiteUsername().equals(username)) {
                resignNotification = new NotificationMessage(username + " resigned the game as White, making " + currGame.blackUsername() + "the winner! Game over!");
            }
            else{
                resignNotification = new NotificationMessage(username + " resigned the game as Black, making " + currGame.whiteUsername() + "the winner! Game over!");
            }
            sendToAllClients(connection.session(), command, resignNotification);
        }

    }




}
