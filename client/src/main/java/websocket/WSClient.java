package websocket;

import clients.ServerFacade;
import com.sun.nio.sctp.NotificationHandler;
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

    public WSClient(String url, ServerFacade server, NotificationHandler notificationHandler) throws Exception {
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

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private void loadGame(LoadGameMessage loadGameMessage) {
        System.out.println(new GameBoardUI(loadGameMessage.getGameData()).renderPlayer(server.getColor()));
    }

    private void handleNotification(NotificationMessage notificationMessage) {
        System.out.println(notificationMessage.getMessage());
    }

    private void handeError(ErrorMessage errorMessage) {
        System.out.println(errorMessage.getErrorMessage());
    }
}
