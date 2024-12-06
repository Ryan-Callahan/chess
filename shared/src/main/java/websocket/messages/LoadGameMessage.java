package websocket.messages;

import model.GameData;

import static websocket.messages.ServerMessage.ServerMessageType.LOAD_GAME;

public class LoadGameMessage extends ServerMessage {
    private final GameData gameData;

    public LoadGameMessage(GameData gameData) {
        super(LOAD_GAME);
        this.gameData = gameData;
    }

    public GameData getGameData() {
        return this.gameData;
    }
}
