package websocket;


import org.eclipse.jetty.websocket.api.Session;

public record ConnectionContainer(String authToken, Session session) {
}
