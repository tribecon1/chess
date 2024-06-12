package websocket;


import javax.websocket.Session;

public record ConnectionContainer(String authToken, Session session) {
}
