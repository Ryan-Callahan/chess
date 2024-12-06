package websocket.messages;

import chess.ChessGame;

import static websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION;

public class NotificationMessage extends ServerMessage {
    private final String message;

    private final NotificationType type;

    public enum NotificationType {
        CONNECTION,
        MOVE,
        LEAVE,
        RESIGN,
        CHECK,
        CHECKMATE
    }

    public NotificationMessage(NotificationType type, String username, String message) {
        super(NOTIFICATION);
        this.type = type;
        this.message = generateMessage(username, message);
    }

    public String generateMessage(String username, String message) {
        return switch (type) {
            case CONNECTION -> getConnectionType(username, message);
            case MOVE -> String.format("%s has made a move: %s", username, message);
            case LEAVE -> String.format("%s has left the game.", username);
            case RESIGN -> String.format("%s has forfeit the game.", username);
            case CHECK -> String.format("%s is in check.", username);
            case CHECKMATE -> String.format("%s is in checkmate.", username);
        };
    }

    public String getMessage() {
        return this.message;
    }

    public NotificationType getType() {
        return this.type;
    }

    private String getConnectionType(String username, String message) {
        if (message != null) {
            return switch (ChessGame.TeamColor.valueOf(message.toUpperCase())) {
                case WHITE -> username + " has joined as white team.";
                case BLACK -> username + " has joined as black team.";
            };
        } else {
            return username + " started observing the game.";
        }
    }
}
