package websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
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
                if (!authToken.equals(connContainer.authToken())) { //only possible to be a NotificationMessage
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
        }
        String teamColor;
        GameData selectedGame = gameDao.getGame(command.getGameID());
        if (selectedGame != null) {
            if (selectedGame.whiteUsername()!= null && selectedGame.whiteUsername().equals(username)) {
                teamColor = "White";
            }
            else if (selectedGame.blackUsername() != null && selectedGame.blackUsername().equals(username)) {
                teamColor = "Black";
            }
            else{
                teamColor = "an observer";
            }
        }
        else{
            send(connection.session(), new ErrorMessage("Error: Invalid game ID chosen"));
            return;
        }
        var message = String.format("User \"" + username + "\" joined this game as " + teamColor);
        var notification = new NotificationMessage(message);
        send(connection.session(), new LoadGameMessage(SerializerDeserializer.convertToJSON(gameDao.getGame(command.getGameID()).game()) ) );
        broadcast(command.getAuthString(), command.getGameID(), notification);
    }

    private void makeMove(ConnectionContainer connection, String username, MoveCommand command) throws Exception {
        GameData selectedGame = gameDao.getGame(command.getGameID());
        if (selectedGame != null){
            if (selectedGame.game().isGameOver()){
                send(connection.session(), new ErrorMessage("Error: Game is over, no more moves can be made")); //after a player RESIGNS or Checkmate is achieved
            }
            else if(selectedGame.blackUsername() == null || selectedGame.whiteUsername() == null){
                send(connection.session(), new ErrorMessage("Error: Must have a team to play against before making a move"));
            }
            else if (selectedGame.whiteUsername().equals(username) && selectedGame.game().getTeamTurn() != ChessGame.TeamColor.WHITE ||
                    selectedGame.blackUsername().equals(username) && selectedGame.game().getTeamTurn() != ChessGame.TeamColor.BLACK ) {
                send(connection.session(), new ErrorMessage("Error: Not your turn to make a move"));
            }
            else if (!selectedGame.whiteUsername().equals(username) && !selectedGame.blackUsername().equals(username)) {
                send(connection.session(), new ErrorMessage("Error: You are only allowed to observe the game"));
            }
            else if(selectedGame.game().getBoard().getPiece(command.getMove().getStartPosition()).getPieceType().equals(ChessPiece.PieceType.PAWN)) {
                if ((command.getMove().getEndPosition().getRow() == 8 || command.getMove().getEndPosition().getRow() == 1) && command.getMove().getPromotionPiece() == null){
                    send(connection.session(), new ErrorMessage("Error: You must declare the type of piece you want to promote your pawn to at the other end of the board"));
                }
            }
            else {
                try{
                    selectedGame.game().makeMove(command.getMove());
                    sendToAllClients(connection.session(), command, new LoadGameMessage(SerializerDeserializer.convertToJSON(selectedGame.game()))); //more needed below on describing move?
                    broadcast(command.getAuthString(), command.getGameID(), new NotificationMessage(username+" moved their "+selectedGame.game().getBoard().getPiece(command.getMove().getEndPosition()) ));
                    if (selectedGame.game().isInStalemate(ChessGame.TeamColor.WHITE) && selectedGame.game().isInStalemate(ChessGame.TeamColor.BLACK)){
                        selectedGame.game().setGameOver(true);
                        sendToAllClients(connection.session(), command, new NotificationMessage("Both teams are in Stalemate! Game over, it's a draw!"));
                    }
                    gameDao.updateGame(selectedGame, selectedGame); //in case stalemate changes it one last time after move
                }
                catch (InvalidMoveException | DataAccessException e){
                    send(connection.session(), new ErrorMessage(e.getMessage()));
                }
                if (selectedGame.whiteUsername().equals(username)){
                    if (selectedGame.game().isInCheck(ChessGame.TeamColor.BLACK)){
                        sendToAllClients(connection.session(), command, new NotificationMessage("Black Team is in Check!"));
                    }
                    else if (selectedGame.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                        sendToAllClients(connection.session(), command, new NotificationMessage("Black Team is in Checkmate! Game over, White Team wins!"));
                    }
                    else if (selectedGame.game().isInStalemate(ChessGame.TeamColor.BLACK)){
                        sendToAllClients(connection.session(), command, new NotificationMessage("Black Team is in Stalemate! It has no available moves, so White Team's turn again!"));
                    }
                }
                else {
                    if (selectedGame.game().isInCheck(ChessGame.TeamColor.WHITE)){
                        sendToAllClients(connection.session(), command, new NotificationMessage("White Team is in Check!"));
                    }
                    else if (selectedGame.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                        sendToAllClients(connection.session(), command, new NotificationMessage("White Team is in Checkmate! Game over, Black Team wins!"));
                    }
                    else if (selectedGame.game().isInStalemate(ChessGame.TeamColor.WHITE)){
                        sendToAllClients(connection.session(), command, new NotificationMessage("White Team is in Stalemate! It has no available moves, so Black Team's turn again!"));
                    }
                }
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
            if (currGame.whiteUsername()!= null && currGame.whiteUsername().equals(username)) {
                gameDao.updateGame(currGame, new GameData(currGame.gameID(), null, currGame.blackUsername(), currGame.gameName(), currGame.game()));
                broadcast(command.getAuthString(), command.getGameID(), new NotificationMessage(username + " left the game playing as White"));
            }
            else if (currGame.blackUsername()!= null && currGame.blackUsername().equals(username)) {
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
        else if(currGame.game().isGameOver()){
            send(connection.session(), new ErrorMessage("Error: the game is already over, no way to resign again"));
        }
        else{
            currGame.game().setGameOver(true);

            gameDao.updateGame(currGame, currGame); //Why is this not updating gameIsOver when I put it back in the SQL database?
            NotificationMessage resignNotification;
            if (currGame.whiteUsername().equals(username)) {
                resignNotification = new NotificationMessage(username + " resigned the game as White, making " + currGame.blackUsername() + " the winner! Game over!");
            }
            else{
                resignNotification = new NotificationMessage(username + " resigned the game as Black, making " + currGame.whiteUsername() + " the winner! Game over!");
            }
            sendToAllClients(connection.session(), command, resignNotification);
        }

    }



}
