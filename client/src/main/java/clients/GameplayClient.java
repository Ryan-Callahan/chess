package clients;

import static clients.ClientType.IN_GAME;

public class GameplayClient extends Client {
    public GameplayClient(ServerFacade server) {
        super(server);
        currentClient = IN_GAME;
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String eval(String input) {
        return null;
    }
}
