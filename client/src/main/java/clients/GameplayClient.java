package clients;

import java.io.IOException;
import java.util.Arrays;

import static clients.ClientType.IN_GAME;

public class GameplayClient extends Client {
    public GameplayClient(ServerFacade server) {
        super(server);
        currentClient = IN_GAME;
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
    }

    private String leave() throws IOException {

    }

    private String makeMove(String... params) throws IOException {
    }

    private String resign(String... params) throws IOException {
    }

    private String highlightLegalMoves(String... params) throws IOException {

    }
}
