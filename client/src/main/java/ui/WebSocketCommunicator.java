package ui;

import net.ServerMessageObserver;
import server.SerializerDeserializer;
import websocket.commands.UserGameCommand;


import javax.websocket.*;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {

    private final Session session;

    public WebSocketCommunicator(ServerMessageObserver rootClient, int givenPort) throws Exception {
        URI uri = new URI("ws://localhost:"+givenPort+"/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
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


