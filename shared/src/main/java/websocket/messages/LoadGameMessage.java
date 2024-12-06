package websocket.messages;

import model.GameData;

import static websocket.messages.ServerMessage.ServerMessageType.LOAD_GAME;

public class LoadGameMessage extends ServerMessage {
    private final GameData game;

    public LoadGameMessage(GameData gameData) {
        super(LOAD_GAME);
        this.game = gameData;
    }

    public GameData getGame() {
        return this.game;
    }
}
