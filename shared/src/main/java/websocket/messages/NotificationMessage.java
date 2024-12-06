package websocket.messages;

import chess.ChessMove;
import serializer.GSerializer;

public class NotificationMessage extends ServerMessage{
    private String message;

    private NotificationType notificationType;

    public enum NotificationType {
        JOIN,
        OBSERVE,
        MOVE,
        LEAVE,
        RESIGN,
        CHECK,
        CHECKMATE
    }

    public NotificationMessage(NotificationType type, String username) {
        super(ServerMessageType.NOTIFICATION);
        this.notificationType = type;
        this.message = generateMessage(username);
    }

    public String getMessage() {
        return message;
    }

    private String generateMessage(String username) {
        return switch (notificationType) {
            case JOIN -> username + " has joined the game.";
            case OBSERVE -> username + " is observing the game.";
            case MOVE -> getMoveString(username);
            case LEAVE -> username + " has left the game.";
            case RESIGN -> username + " has forfeit the game.";
            case CHECK -> username + " is in check.";
            case CHECKMATE -> username + " is in checkmate.";
        };
    }

    private String getMoveString(String message) {
        var m = message.split("\\|\\|\\|");
        String username = m[1];
        ChessMove move = GSerializer.deserialize(m[0], ChessMove.class);
        return username + " has made a move: " + move.toString();
    }
}
