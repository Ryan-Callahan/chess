package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, GameSession> connections = new ConcurrentHashMap<>();

    public void addGame(Session session, int gameID) {
        var game = new GameSession(session, gameID);
        connections.put(gameID, game);
    }

    public void removeGame(int gameID) {
        connections.remove(gameID);
    }

    public GameSession getGameSession(int gameID) {
        return connections.get(gameID);
    }
}
