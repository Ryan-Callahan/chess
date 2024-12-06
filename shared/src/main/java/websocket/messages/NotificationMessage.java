package websocket.messages;

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
        this.message = username + " has " + type.toString();
    }

    public String getMessage() {
        return this.message;
    }

    public NotificationType getType() {
        return this.type;
    }
}
