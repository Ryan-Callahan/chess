package server.websocket;

import chess.ChessGame;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

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

    public ChessGame.TeamColor getUserColor(String authToken) {
        var username = getUserConnection(authToken).getUsername();
        if (Objects.equals(username, whitePlayer)) {
            return WHITE;
        } else if (Objects.equals(username, blackPlayer)) {
            return BLACK;
        } else {
            return null;
        }
    }

    public void updateTeams(GameData gameData) {
        whitePlayer = gameData.whiteUsername();
        blackPlayer = gameData.blackUsername();
    }
}
