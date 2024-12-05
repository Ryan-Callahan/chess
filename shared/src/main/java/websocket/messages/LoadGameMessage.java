package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    private int game;

    public LoadGameMessage(int game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public int getGame() {
        return game;
    }
}
