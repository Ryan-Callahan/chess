package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.result.*;

import java.util.Collection;

public class GameService extends AuthService implements Service {
    private int gameIDCtr = 1;

    public Response listGames(ListGamesRequest listGamesRequest) {
        if (!authDAO.existsAuth(listGamesRequest.authToken())) {
            return new Response(401, new ErrorResult("unauthorized"));
        }
        Collection<GameData> games = gameDAO.listGames();
        return new Response(200, new ListGamesResult(games));
    }

    public Response createGame(CreateGameRequest createGameRequest) {
        String gameName = createGameRequest.gameName();
        String authToken = createGameRequest.authToken();
        if (!authDAO.existsAuth(authToken)) {
            return new Response(401, new ErrorResult("unauthorized"));
        }
        int gameID = gameIDCtr++;
        var newGame = new GameData(gameID, "", "", gameName, new ChessGame());
        try {
            gameDAO.createGame(newGame);
            return new Response(200, new CreateGameResult(gameID));
        } catch (DataAccessException e) {
            return new Response(400, new ErrorResult(e.getMessage()));
        }
    }

    public Response joinGame(JoinGameRequest joinGameRequest) {
        String joinColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();
        String authToken = joinGameRequest.authToken();
        String username;
        GameData game;
        try {
            username = authDAO.getAuthByToken(authToken).username();
        } catch (DataAccessException e) {
            return new Response(401, new ErrorResult(e.getMessage()));
        }

        try {
            game = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            return new Response(400, new ErrorResult(e.getMessage()));
        }

        return tryJoiningGame(game, joinColor, username);
    }

    private Boolean isColorTaken(GameData game, String playerColor) {
        if (playerColor.equals("WHITE")) {
            return !game.whiteUsername().isEmpty();
        } else {
            return !game.blackUsername().isEmpty();
        }
    }

    private GameData updatePlayerColor(GameData game, String playerColor, String username) {
        GameData gameUpdate;
        if (playerColor.equals("WHITE")) {
            gameUpdate = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            gameUpdate = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        return gameUpdate;
    }

    private Response tryJoiningGame(GameData game, String joinColor, String username) {
        if (isColorTaken(game, joinColor)) {
            return new Response(403, new ErrorResult("Could not join game; Color is already taken!"));
        } else {
            try {
                GameData gameUpdate = updatePlayerColor(game, joinColor, username);
                gameDAO.updateGame(gameUpdate);
                return new Response(200, new EmptyResult());
            } catch (DataAccessException e) {
                return new Response(400, new ErrorResult(e.getMessage()));
            }
        }
    }
}
