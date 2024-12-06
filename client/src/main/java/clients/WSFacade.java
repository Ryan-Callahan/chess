package clients;

import chess.ChessMove;
import model.GameData;
import serializer.GSerializer;
import websocket.WSClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WSFacade {
    private final WSClient wsClient;
    private final ServerFacade server;
    private final GameData game;

    public WSFacade(String url, ServerFacade server, GameData game) throws Exception {
        this.wsClient = new WSClient(url, server);
        this.server = server;
        this.game = game;
    }

    public int getGameID() {
        return game.gameID();
    }

    public GameData getGame() {
        return game;
    }

    public void connect() throws Exception {
        var message = new UserGameCommand(CONNECT, server.getAuthToken(), game.gameID());
        wsClient.send(GSerializer.serialize(message));
    }

    public void leave() throws Exception {
        var message = new UserGameCommand(LEAVE, server.getAuthToken(), game.gameID());
        wsClient.send(GSerializer.serialize(message));
    }

    public void resign() throws Exception {
        var message = new UserGameCommand(RESIGN, server.getAuthToken(), game.gameID());
        wsClient.send(GSerializer.serialize(message));
    }

    public void makeMove(ChessMove move) throws Exception {
        var message = new MakeMoveCommand(server.getAuthToken(), game.gameID(), move);
        wsClient.send(GSerializer.serialize(message));
    }
}
