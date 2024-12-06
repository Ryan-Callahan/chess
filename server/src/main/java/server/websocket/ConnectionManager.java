package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private int gameID;
    private Session session;
    private final ConcurrentHashMap<String, UserConnection> userConnections = new ConcurrentHashMap<>();

    public ConnectionManager(Session session, int gameID) {
        this.session = session;
        this.gameID = gameID;
    }

    public void addUserConnection(Session session, String username, String authToken) {
        var userConnection = new UserConnection(session, username, authToken);
        userConnections.put(username, userConnection);
    }

    public void removeUserConnection(String username) {
        userConnections.remove(username);
    }

    public UserConnection getUserConnection(String username) {
        return userConnections.get(username);
    }

    public void broadcast(String excludeUsername, String message) throws IOException {
        for (var user : userConnections.values()) {
            if (!Objects.equals(user.getUsername(), excludeUsername)) {
                user.send(message);
            }
        }
    }

    public void messageUser(String username, String message) throws IOException {
        userConnections.get(username).send(message);
    }
}
