package server.websocket;

import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serializer.GSerializer;
import server.Server;
import service.Service;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Objects;

import static websocket.messages.NotificationMessage.NotificationType.*;

@WebSocket
public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
        UserGameCommand command = GSerializer.deserialize(message, UserGameCommand.class);

        String username = getUsername(command.getAuthToken());

        saveSession(username, session);

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, username, command);
            case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
            case LEAVE -> leaveGame(session, username,  command);
            case RESIGN -> resign(session, username,  command);
        }
        } catch (DataAccessException ex) {
            connections.sendMessage(session, new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            connections.sendMessage(session, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        return Service.AUTH_DAO.getAuthByToken(authToken).username();
    }

    private void saveSession(String username, Session session) {
        if (!connections.connections.containsKey(username)) {
            connections.add(username, session);
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws DataAccessException, IOException {
        connections.sendMessage(username, new LoadGameMessage(getGame(command.getGameID())));
        connections.sendMessage(username, getJoinMessage(username, command));
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws IOException {
        String move = GSerializer.serialize(command.getMove());

        connections.sendMessage(username, new NotificationMessage(MOVE, move + "|||" + username));
    }

    private void leaveGame(Session session, String username, UserGameCommand command) throws IOException {
        connections.remove(username);
        connections.sendMessage(username, new NotificationMessage(LEAVE, username));

    }

    private void resign(Session session, String username, UserGameCommand command) throws IOException {
        connections.remove(username);
        connections.sendMessage(null, new NotificationMessage(RESIGN, username));
    }

    private NotificationMessage getJoinMessage(String username, UserGameCommand command) throws DataAccessException {
        var game = getGame(command.getGameID());
        if (Objects.equals(game.whiteUsername(), username) || Objects.equals(game.blackUsername(), username)) {
            return new NotificationMessage(JOIN, username);
        } else {
            return new NotificationMessage(OBSERVE, username);
        }
    }

    private GameData getGame(int gameID) throws DataAccessException {
        return Service.GAME_DAO.getGame(gameID);
    }
}
