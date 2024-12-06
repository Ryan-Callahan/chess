package websocket.messages;

import chess.ChessGame;

import static websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION;

public class NotificationMessage extends ServerMessage {
    private final String message;

    private final NotificationType type;

    public enum NotificationType {
        CONNECTION,
        OBSERVATION,
        MOVE,
        LEAVE,
        RESIGN,
        CHECK,
        CHECKMATE
    }

    public NotificationMessage(NotificationType type, String username) {
        super(NOTIFICATION);
        this.type = type;
        this.message = generateMessage(username);
    }

    public String generateMessage(String username) {
        return switch (type) {
            case CONNECTION -> getConnectionType(username);
            case OBSERVATION -> null;
            case MOVE -> null;
            case LEAVE -> null;
            case RESIGN -> null;
            case CHECK -> null;
            case CHECKMATE -> null;
        };
    }

    public String getMessage() {
        return this.message;
    }

    public NotificationType getType() {
        return this.type;
    }

    private String getConnectionType(String message) {
        var parts = message.split("\\|\\|\\|");
        if (parts.length > 1) {
            return switch (ChessGame.TeamColor.valueOf(parts[1].toUpperCase())) {
                case WHITE -> parts[0] + " has joined as white team.";
                case BLACK -> parts[0] + " has joined as black team.";
            };
        } else {
            return message + " started observing the game.";
        }
    }
}
