package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class GameSession {
    private int gameID;
    private Session session;
    private final ConcurrentHashMap<String, Connection> userConnections = new ConcurrentHashMap<>();
    private String whitePlayer;
    private String blackPlayer;

    public GameSession(Session session, int gameID) {
        this.session = session;
        this.gameID = gameID;
    }

    public void addUserConnection(Session session, String username, String authToken, ChessGame.TeamColor color) {
        var userConnection = new Connection(session, username, authToken);
        userConnections.put(authToken, userConnection);
        if (color != null) {
            switch (color) {
                case WHITE -> whitePlayer = authToken;
                case BLACK -> blackPlayer = authToken;
            }
        }
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
