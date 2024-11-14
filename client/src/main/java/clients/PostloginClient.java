package clients;

import exception.ResponseException;
import model.GameData;
import serializer.GSerializer;
import server.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static clients.ClientType.LOGGED_IN;

public class PostloginClient extends Client {
    HashMap<Integer, GameData> games = new HashMap<>();
    HashMap<Integer, Integer> gamesKeysToIDs = new HashMap<>();

    public PostloginClient(ServerFacade server) throws ResponseException {
        super(server);
        currentClient = LOGGED_IN;
        populateGamesList();
    }
    @Override
    public String help() {
        return """
               - logout
               - createGame <GAMENAME>
               - listGames
               - playGame <GAMEID> <TEAMCOLOR>
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
                case "creategame" -> createGame(params);
                case "listgames" -> listGames();
                case "playgame" -> playGame(params);
                case "observegame" -> observeGame(params);
                default -> response(help());
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String logout() throws ResponseException {
        demoteClient();
        server.logout();
        return response("Logging out...");
    }

    private String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameName = params[0];
            server.createGame(gameName);
            return response("Created game " + gameName);
        }
        throw new ResponseException(400, "Expected: <gamename>");
    }

    private String listGames() throws ResponseException {
        populateGamesList();
        StringBuilder gamesList = new StringBuilder();
        for (var key : games.keySet()) {
            var game = games.get(key);
            gamesList.append(String.format("%s %s", key, game.gameName()));
            gamesList.append("\n  Players:");
            gamesList.append("\n   White: ").append(Objects.toString(game.whiteUsername(), ""));
            gamesList.append("\n   Black: ").append(Objects.toString(game.blackUsername(), ""));
            gamesList.append("\n---------\n");
        }
        gamesList.delete(gamesList.length()-11, gamesList.length());
        return response(gamesList.toString());
    }

    private String playGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var gameID = getGameID(Integer.parseInt(params[0]));
            var teamColor = params[1];
            server.joinGame(teamColor, gameID);
            return response("Joined game");
        }
        throw new ResponseException(400, "Expected: <gameid> <teamcolor>");
    }

    private String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameID = getGameID(Integer.parseInt(params[0]));
            var game = server.observeGame(gameID);
            return response(GSerializer.serialize(game));
        }
        throw new ResponseException(400, "Expected: <gameid>");
    }

    private void populateGamesList() throws ResponseException {
        this.games = new HashMap<>();
        this.gamesKeysToIDs = new HashMap<>();
        var games = server.listGames();
        int ctr = 1;
        for (var game : games) {
            this.games.put(ctr, game);
            this.gamesKeysToIDs.put(ctr, game.gameID());
            ctr++;
        }
    }

    private int getGameID(int gameKey) throws ResponseException {
        var gameID = gamesKeysToIDs.get(gameKey);
        if (gameID != null) {
            return gameID;
        }
        throw new ResponseException(400, "Game ID does not exist!");
    }
}
