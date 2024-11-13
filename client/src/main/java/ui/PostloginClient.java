package ui;

import server.ServerFacade;

import java.util.Arrays;

import static ui.ClientType.LOGGED_IN;

public class PostloginClient extends Client {
    public PostloginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        currentClient = LOGGED_IN;
    }
    @Override
    public String help() {
        return """
               - logout
               - createGame <GAMENAME>
               - listGames
               - joinGame <GAMEID> <TEAMCOLOR>
               - observeGame <GAMEID>
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
                case "logout" -> logout();
                case "createGame" -> createGame(params);
                case "listGames" -> listGames();
                case "joinGame" -> joinGame(params);
                case "observeGame" -> observeGame(params);
                default -> response(help());
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String logout() {
        demoteClient();
        return response("Logging out...");
    }

    private String createGame(String... params) {
        return null;
    }

    private String listGames(String... params) {
        return null;
    }

    private String joinGame(String... params) {
        return null;
    }

    private String observeGame(String... params) {
        return null;
    }
}
