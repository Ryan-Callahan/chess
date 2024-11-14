package ui;

import exception.ResponseException;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import serializer.GSerializer;
import server.ServerFacade;

import java.util.Arrays;

import static ui.ClientType.LOGGED_IN;

public class PostloginClient extends Client {
    public PostloginClient(ServerFacade server) {
        super(server);
        currentClient = LOGGED_IN;
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
            CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
            server.createGame(createGameRequest);
            return response("Created game " + gameName);
        }
        throw new ResponseException(400, "Expected: <gamename>");
    }

    private String listGames() throws ResponseException {
        var games = server.listGames().games();
        return response(GSerializer.serialize(games));
    }

    private String playGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var gameID = params[0];
            var teamColor = params[1];
            JoinGameRequest joinGameRequest = new JoinGameRequest(teamColor, Integer.parseInt(gameID));
            server.joinGame(joinGameRequest);
            return response("Joined game");
        }
        throw new ResponseException(400, "Expected: <gameid> <teamcolor>");
    }

    private String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameID = params[0];
            var game = server.observeGame(Integer.parseInt(gameID));
            return response(GSerializer.serialize(game));
        }
        throw new ResponseException(400, "Expected: <gameid>");
    }
}
