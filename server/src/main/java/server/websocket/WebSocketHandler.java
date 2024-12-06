package server.websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import exception.ChessMoveException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serializer.GSerializer;
import service.Service;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

import static websocket.messages.NotificationMessage.NotificationType.CONNECTION;
import static websocket.messages.NotificationMessage.NotificationType.MOVE;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = GSerializer.deserialize(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case LEAVE -> leave(command);
            case RESIGN -> resign(command);
            case CONNECT -> connect(session, command);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = GSerializer.deserialize(message, MakeMoveCommand.class);
                makeMove(session, makeMoveCommand);
            }
        }
    }

    private void connect(Session session, UserGameCommand command) throws IOException {
        try {
            if (connections.getGameSession(command.getGameID()) == null) {
                connections.addGame(session, command.getGameID());
            }
            var gameSession = connections.getGameSession(command.getGameID());
            var gameData = Service.GAME_DAO.getGame(command.getGameID());
            gameSession.updateTeams(gameData);

            var username = Service.AUTH_DAO.getAuthByToken(command.getAuthToken()).username();
            var authToken = command.getAuthToken();
            if (gameSession.getUserConnection(authToken) == null) {
                gameSession.addUserConnection(session, username, authToken);
            }

            var teamColor = gameSession.getUserColor(authToken);
            var loadGameMessage = new LoadGameMessage(gameData);
            var notificationMessage = getConnectionNotification(username, teamColor);
            gameSession.messageUser(command.getAuthToken(), GSerializer.serialize(loadGameMessage));
            gameSession.broadcast(command.getAuthToken(), GSerializer.serialize(notificationMessage));
        } catch (Exception ex) {
            sendErrorToRootClient(session, ex.getMessage());
        }
    }

    private void makeMove(Session session, MakeMoveCommand command) throws IOException {
        try {
            var gameData = Service.GAME_DAO.getGame(command.getGameID());
            var chessMove = command.getMove();
            var validMoves = gameData.game().validMoves(chessMove.getStartPosition());
            if (!validMoves.contains(chessMove)) {
                throw new ChessMoveException(String.format("move %s is invalid!", chessMove));
            }
            gameData.game().makeMove(chessMove);
            var updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), gameData.game());
            Service.GAME_DAO.updateGame(updatedGame);

            var gameSession = connections.getGameSession(command.getGameID());
            var loadMessage = new LoadGameMessage(gameData);
            var username = Service.AUTH_DAO.getAuthByToken(command.getAuthToken()).username();
            var moveNotification = new NotificationMessage(MOVE, username, chessMove.toString());
            gameSession.broadcast(null, GSerializer.serialize(loadMessage));
            gameSession.broadcast(command.getAuthToken(), GSerializer.serialize(moveNotification));
            //todo validate move
            //update game
            //send load message to root
            //send notification to all other clients
            //if in check or checkmate, send to all clients
        } catch (Exception ex) {
            sendErrorToRootClient(session, ex.getMessage());
        }
    }

    private void resign(UserGameCommand command) {
        //todo mark game as over
        //todo message all clients
    }

    private void leave(UserGameCommand command) {
        //todo remove user from game
        //todo message all other clients
    }

    private NotificationMessage getConnectionNotification(String username, ChessGame.TeamColor color) {
        if (color != null) {
            return new NotificationMessage(CONNECTION, username, color.toString());
        } else {
            return new NotificationMessage(CONNECTION, username, null);
        }
    }



    private void sendErrorToRootClient(Session session, String message) throws IOException {
        var errorMessage = new ErrorMessage(message);
        session.getRemote().sendString(GSerializer.serialize(errorMessage));
    }
}
