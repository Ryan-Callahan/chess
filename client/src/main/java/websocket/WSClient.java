package websocket;
import serializer.GSerializer;
import clients.ServerFacade;
import ui.GameBoardUI;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WSClient extends Endpoint {
    public Session session;
    private final ServerFacade server;

    public WSClient(String url, ServerFacade server) throws Exception {
        this.server = server;
        url = url.replace("http", "ws");
        URI socketURI = new URI(url + "/ws");

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        //set message handler
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {

                if (message.contains("LOAD")) {
                    loadGame(message);
                } else if (message.contains("NOTIFICATION")) {
                    handleNotification(message);
                } else if (message.contains("ERROR")) {
                    printError(message);
                } else {
                    System.out.println(message);
                }
            }
        });
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    public void handleNotification(String message) {
        var notificationHandler = new NotificationHandler();
        NotificationMessage notificationMessage = GSerializer.deserialize(message, NotificationMessage.class);

    }

    public void loadGame(String message) {
        LoadGameMessage loadGameMessage = GSerializer.deserialize(message, LoadGameMessage.class);
        System.out.printf("\n" + new GameBoardUI(loadGameMessage.getGame()).renderPlayer(server.getTeamColor()));
    }

    public void printError(String message) {
        ErrorMessage errorMessage = GSerializer.deserialize(message, ErrorMessage.class);
        System.out.printf(errorMessage.getErrorMessage());
    }


}
