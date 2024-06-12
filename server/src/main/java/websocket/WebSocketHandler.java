package websocket;

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

    //use authToken instead of rootClient username?
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
        var message = String.format("User \"" + username + "\" joined this game as " + teamColor);
        var notification = new NotificationMessage(message);
        broadcast(command.getAuthString(), command.getGameID(), notification);
    }

    private void makeMove(ConnectionContainer connection, String username, MoveCommand command) throws Exception {
        return;
    }

    private void leaveGame(ConnectionContainer connection, String username, LeaveCommand command) throws Exception {
        return;
    }

    private void resign(ConnectionContainer connection, String username, ResignCommand command) throws Exception {
        return;
    }




}
