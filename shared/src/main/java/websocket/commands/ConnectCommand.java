package websocket.commands;

import chess.ChessGame;

import static websocket.commands.UserGameCommand.CommandType.CONNECT;

public class ConnectCommand extends UserGameCommand {
    private final ChessGame.TeamColor color;

    public ConnectCommand(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CONNECT, authToken, gameID);
        this.color = color;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
