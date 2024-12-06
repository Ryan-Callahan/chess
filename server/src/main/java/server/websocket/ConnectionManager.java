package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private int gameID;
    private Session session;
    private final ConcurrentHashMap<String, Connection> userConnections = new ConcurrentHashMap<>();

    public ConnectionManager(Session session, int gameID) {
        this.session = session;
        this.gameID = gameID;
    }

    public void addUserConnection(Session session, String username, String authToken) {
        var userConnection = new Connection(session, username, authToken);
        userConnections.put(authToken, userConnection);
    }

    public void removeUserConnection(String authToken) {
        userConnections.remove(authToken);
    }

    public Connection getUserConnection(String authToken) {
        return userConnections.get(authToken);
    }

    public void broadcast(String excludeAuthToken, String message) throws IOException {
        for (var user : userConnections.values()) {
            if (!Objects.equals(user.getAuthToken(), excludeAuthToken)) {
                user.send(message);
            }
        }
    }

    public void messageUser(String authToken, String message) throws IOException {
        userConnections.get(authToken).send(message);
    }
}
