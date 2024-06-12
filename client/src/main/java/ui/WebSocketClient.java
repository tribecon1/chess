package ui;

import net.ServerMessageObserver;
import net.WebsocketClientCommunicator;
import server.SerializerDeserializer;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MoveCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketClient extends Endpoint {

    private Session session;
    private WebsocketClientCommunicator wsCommunicator;

    public WebSocketClient(ServerMessageObserver rootClient) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.wsCommunicator = new WebsocketClientCommunicator(rootClient);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                switch (SerializerDeserializer.convertFromJSON(message, ServerMessage.class).getServerMessageType()) {
                    case NOTIFICATION -> connect(session, username, SerializerDeserializer.convertFromJSON(message, ConnectCommand.class));
                    case MAKE_MOVE -> makeMove(session, username, SerializerDeserializer.convertFromJSON(message, MoveCommand.class));
                    case LEAVE -> leaveGame(session, username, SerializerDeserializer.convertFromJSON(message, LeaveCommand.class));
                }
            }
        });
    }

    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}


