package clients;

import chess.ChessMove;
import chess.ChessPosition;
import gui.GameBoardUI;
import websocket.WSClient;

import java.io.IOException;
import java.util.Arrays;

import static clients.ClientType.IN_GAME;

public class GameplayClient extends Client {
    private WSFacade ws;
    private int gameID;
    public GameplayClient(ServerFacade server) throws Exception {
        super(server);
        currentClient = IN_GAME;
        ws = server.getWebSocket();
        this.gameID = ws.getGameID();
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
            return switch (command) {
                case "redraw_board" -> redrawBoard();
                case "leave" -> leave();
                case "make_move" -> makeMove(params);
                case "resign" -> resign();
                case "highlight_legal_moves" -> highlightLegalMoves(params);
                default -> response(help());
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String redrawBoard() {
        System.out.println(new GameBoardUI(ws.getGame()).renderPlayer(server.getColor()));
        return response("");
    }

    private String leave() throws Exception {
        ws.leave();
        demoteClient();
        return response("left the game");
    }

    private String makeMove(String... params) throws Exception {
        var move = new ChessMove(params[0]);
        ws.makeMove(move);
        return move.toString();
    }

    private String resign(String... params) throws Exception {
        ws.resign();
        //todo resign from game
        return response("resigned");
    }

    private String highlightLegalMoves(String... params) throws IOException {
        var position = new ChessPosition(params[0]);
        var highlightedMoves = new GameBoardUI(ws.getGame()).renderHighlightedMoves(server.getColor(), position);
        System.out.println(highlightedMoves);
        return response("");
    }
}
