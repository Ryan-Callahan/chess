package websocket;

import clients.ServerFacade;
import exception.ResponseException;
import gui.GameBoardUI;
import serializer.GSerializer;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSClient extends Endpoint {
    public Session session;
    public ServerFacade server;

    public WSClient(String url, ServerFacade server) throws Exception {
        try {
            this.server = server;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = GSerializer.deserialize(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGameMessage loadGameMessage = GSerializer.deserialize(message, LoadGameMessage.class);
                            loadGame(loadGameMessage);
                        }
                        case NOTIFICATION -> {
                            NotificationMessage notificationMessage = GSerializer.deserialize(message, NotificationMessage.class);
                            handleNotification(notificationMessage);
                        }
                        case ERROR -> {
                            ErrorMessage errorMessage = GSerializer.deserialize(message, ErrorMessage.class);
                            handeError(errorMessage);
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void send(String message) throws Exception {
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private void loadGame(LoadGameMessage loadGameMessage) {
        printString(new GameBoardUI(loadGameMessage.getGame()).renderPlayer(server.getColor()));
    }

    private void handleNotification(NotificationMessage notificationMessage) {
        printString(notificationMessage.getMessage());
    }

    private void handeError(ErrorMessage errorMessage) {
        printString(errorMessage.getErrorMessage());
    }

    private void printString(String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append(message);
        stringBuilder.append("\n[IN_GAME]>>> ");
        System.out.printf(stringBuilder.toString());
    }
}
