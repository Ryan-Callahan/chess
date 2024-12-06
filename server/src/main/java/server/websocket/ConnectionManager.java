package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import serializer.GSerializer;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void sendMessage(String username, NotificationMessage notificationMessage) throws IOException {
        for (var connection : connections.values()) {
            if (!connection.getUsername().equals(username)) {
                connection.send(GSerializer.serialize(notificationMessage));
            }
        }
    }

    public void sendMessage(String username, LoadGameMessage loadGameMessage) throws IOException {
        connections.get(username).send(GSerializer.serialize(loadGameMessage));
    }

    public void sendMessage(Session session, ErrorMessage errorMessage) throws IOException {
        session.getRemote().sendString(errorMessage.getErrorMessage());
    }
}
