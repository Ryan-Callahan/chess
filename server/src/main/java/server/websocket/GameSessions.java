package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class GameSessions {
    private final ConcurrentHashMap<Integer, ConnectionManager> connections = new ConcurrentHashMap<>();

    public void addGame(Session session, int gameID) {
        var game = new ConnectionManager(session, gameID);
        connections.put(gameID, game);
    }

    public void removeGame(int gameID) {
        connections.remove(gameID);
    }

    public ConnectionManager getGameSession(int gameID) {
        return connections.get(gameID);
    }
}
