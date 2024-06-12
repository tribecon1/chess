package websocket;

import dataaccess.dao.AuthDao;
import javax.websocket.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.SerializerDeserializer;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


@WebSocket
public class WebSocketHandler {

    private final AuthDao authDao;
    private final HashMap<Integer, HashSet<ConnectionContainer>> clientConnectionsPerGame;

    public WebSocketHandler(AuthDao givenAuthDao) {
        this.authDao = givenAuthDao;
        this.clientConnectionsPerGame = new HashMap<>();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand command = SerializerDeserializer.convertFromJSON(message, UserGameCommand.class);

            String username = authDao.getAuth(command.getAuthString()).username();

            clientConnectionsPerGame.get(command.getGameID()).add(new ConnectionContainer(command.getAuthString(), session));

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, SerializerDeserializer.convertFromJSON(message, ConnectCommand.class));
                case MAKE_MOVE -> makeMove(session, username, SerializerDeserializer.convertFromJSON(message, MoveCommand.class));
                case LEAVE -> leaveGame(session, username, SerializerDeserializer.convertFromJSON(message, LeaveCommand.class));
                case RESIGN -> resign(session, username, SerializerDeserializer.convertFromJSON(message, ResignCommand.class));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    //use authToken instead of rootClient username?
    private void broadcast(String rootClient, int gameIDKey NotificationMessage notificationMessage) {
        var removeList = new ArrayList<Session>();
        for (var connContainer : clientConnectionsPerGame.get(gameIDKey)) {
            if (connContainer.session().isOpen()) {
                if (!authDao.getAuth().authToken().equals(excludeVisitorName)) {
                    c.send(notificationMesage.);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void send(String message) throws Exception {
        this.session.getRemote().sendText(message);
    }

    private void connect(Session session, String username, ConnectCommand command) {
        if (clientConnectionsPerGame.containsKey(command.getGameID())) {
            clientConnectionsPerGame.get(command.getGameID()).add(session);
        }
        else{
            HashSet<Session> thisGameClients = new HashSet<>();
            thisGameClients.add(session);
            clientConnectionsPerGame.put(command.getGameID(), thisGameClients);
            return;
        }
        var message = String.format("User " + username + " joined this game as ");
        var notification = new NotificationMessage(message);
        clientConnectionsPerGame.broadcast(visitorName, notification);

    }




}
