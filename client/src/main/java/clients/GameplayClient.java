package clients;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import serializer.GSerializer;
import ui.GameBoardUI;
import websocket.WSClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static clients.ClientType.IN_GAME;
import static websocket.commands.UserGameCommand.CommandType.LEAVE;
import static websocket.commands.UserGameCommand.CommandType.RESIGN;

public class GameplayClient extends Client {
    String username;
    ChessGame.TeamColor teamColor = null;
    Boolean resigning = false;
    public GameplayClient(ServerFacade server, String serverUrl, WSClient ws) {
        super(server, serverUrl);
        currentClient = IN_GAME;
        this.ws = ws;
        this.username = server.getUsername();
        this.teamColor = server.getTeamColor();
    }

    @Override
    public String help() {
        return """
                - redraw_board
                - highlight_legal_moves
                - make_move
                - leave
                - resign
                - help
                """;
    }

    @Override
    public String eval(String input) {
        try {
            String[] inputArray = input.toLowerCase().split(" ");
            String command = inputArray[0];
            String[] params = Arrays.copyOfRange(inputArray, 1, inputArray.length);
            if (resigning) {
                return resign(command);
            }
            return switch (command) {
                case "redraw_board" -> redrawBoard();
                case "leave" -> leave();
                case "make_move" -> makeMove(params);
                case "resign" -> resign("resign");
                case "highlight_legal_moves" -> highlightLegalMoves(params);
                default -> response(help());
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String redrawBoard() {
        return response(new GameBoardUI(server.getCurrentGame()).renderPlayer(teamColor));
    }

    private String leave() throws IOException {
        sendCommand(new UserGameCommand(LEAVE, server.getAuthToken(), server.getCurrentGame().gameID()));
        demoteClient();
        return response("You have left the game.");
    }

    private String makeMove(String... params) throws IOException, ResponseException {
        if (params.length == 1) {
            if (isTeamTurn()) {
                ChessMove move = new ChessMove(params[0]);
                sendCommand(new MakeMoveCommand(move, server.getAuthToken(), server.getCurrentGame().gameID()));
                return response("You have made the move: " + move);
            } else {
                return response("It is not your turn!");
            }
        }
        throw new ResponseException(400, "Expected: <ChessMove>");
    }

    private String resign(String command) throws IOException {
        if (Objects.equals(command.toLowerCase(), "y")) {
            sendCommand(new UserGameCommand(RESIGN, server.getAuthToken(), server.getCurrentGame().gameID()));
            return response("Resigned from the game");
        } else if (Objects.equals(command.toLowerCase(), "resign")) {
            resigning = true;
            return response("Are you sure you want to resign? [Y/N]");
        } else {
            resigning = false;
            return response(" ");
        }
    }

    private String highlightLegalMoves(String... params) throws ResponseException {
        if (params.length == 1) {
            ChessPosition position = new ChessPosition(params[0]);
            Collection<ChessPosition> validPositions = calculateValidPositions(position);
            return response(new GameBoardUI(server.getCurrentGame()).renderHighlightedMoves(teamColor, validPositions));
        }
        throw new ResponseException(400, "Expected: <ChessPosition>");
    }

    private void sendCommand(UserGameCommand command) throws IOException {
        ws.send(GSerializer.serialize(command));
    }

    private Boolean isTeamTurn() {
        return server.getCurrentGame().game().getTeamTurn() == teamColor;
    }

    private Collection<ChessPosition> calculateValidPositions(ChessPosition position) {
        return server.getCurrentGame().game().validMoves(position).stream().map(ChessMove::getEndPosition).toList();
    }
}
