package ui;

import net.ServerMessageObserver;
import server.SerializerDeserializer;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {

    private Session session;

    public WebSocketCommunicator(ServerMessageObserver rootClient, int givenPort) throws Exception {
        URI uri = new URI("ws://localhost:"+givenPort+"/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                rootClient.notify(message);
            }
        });
    }

    public void send(UserGameCommand command) throws Exception {
        this.session.getBasicRemote().sendText(SerializerDeserializer.convertToJSON(command));
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}


