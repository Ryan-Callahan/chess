package server.websocket;

import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serializer.GSerializer;
import service.Service;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

import static websocket.messages.NotificationMessage.NotificationType.CONNECTION;

@WebSocket
public class WebSocketHandler {
    private final GameSessions connections = new GameSessions();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = GSerializer.deserialize(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case LEAVE -> leave(command);
            case RESIGN -> resign(command);
            case CONNECT -> connect(session, command);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = GSerializer.deserialize(message, MakeMoveCommand.class);
                makeMove(makeMoveCommand);
            }
        }
    }

    private void connect(Session session, UserGameCommand command) throws IOException, DataAccessException {
        if (connections.getGameSession(command.getGameID()) == null) {
            connections.addGame(session, command.getGameID());
        }
        var gameSession = connections.getGameSession(command.getGameID());

        var username = Service.AUTH_DAO.getAuthByToken(command.getAuthToken()).username();
        var authToken = command.getAuthToken();
        if (gameSession.getUserConnection(authToken) == null) {
            gameSession.addUserConnection(session, username, authToken);
        }

        var gameData = Service.GAME_DAO.getGame(command.getGameID());
        var loadGameMessage = new LoadGameMessage(gameData);
        var notificationMessage = new NotificationMessage(CONNECTION, username);
        gameSession.messageUser(command.getAuthToken(), GSerializer.serialize(loadGameMessage));
        gameSession.broadcast(command.getAuthToken(), GSerializer.serialize(notificationMessage));
    }

    private void makeMove(MakeMoveCommand command) {

    }

    private void resign(UserGameCommand command) {

    }

    private void leave(UserGameCommand command) {

    }
}
