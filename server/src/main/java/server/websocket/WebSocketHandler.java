package server.websocket;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serializer.GSerializer;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import javax.websocket.Session;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final GameSessions connections = new GameSessions();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = GSerializer.deserialize(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case LEAVE -> leave(command);
            case RESIGN -> resign(command);
            case CONNECT -> connect(command);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = GSerializer.deserialize(message, MakeMoveCommand.class);
                makeMove(makeMoveCommand);
            }
        }
    }

    private void connect(UserGameCommand command) {

    }

    private void makeMove(MakeMoveCommand command) {

    }

    private void resign(UserGameCommand command) {

    }

    private void leave(UserGameCommand command) {

    }
}
