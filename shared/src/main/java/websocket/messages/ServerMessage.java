package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String serverMessageContents;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, String messageContents) {
        this.serverMessageType = type;
        this.serverMessageContents = messageContents;
    }
    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
        this.serverMessageContents = null;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getServerMessageContents() {
        return this.serverMessageContents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return serverMessageType == that.serverMessageType && Objects.equals(serverMessageContents, that.serverMessageContents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverMessageType, serverMessageContents);
    }
}
