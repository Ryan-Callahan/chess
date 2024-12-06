package clients;

import websocket.NotificationHandler;
import websocket.WSClient;

import static clients.ClientType.*;

public abstract class Client {
    protected ServerFacade server;
    protected ClientType currentClient;
    protected NotificationHandler notificationHandler;
    protected WSClient ws = null;
    protected String serverUrl;

    public Client(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;
    }

    public abstract String help();

    public abstract String eval(String input);

    protected String response(String resp) {
        return currentClient + "|||" + resp;
    }

    protected void advanceClient() {
        currentClient = switch (currentClient) {
            case LOGGED_OUT -> LOGGED_IN;
            case LOGGED_IN, IN_GAME -> IN_GAME;
        };
    }

    protected void demoteClient() {
        currentClient = switch (currentClient) {
            case IN_GAME -> LOGGED_IN;
            case LOGGED_IN, LOGGED_OUT -> LOGGED_OUT;
        };
    }
}

