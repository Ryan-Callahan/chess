package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.request.RequestWithAuth;
import model.result.*;

import java.util.Collection;
import java.util.Objects;

public class GameService extends AuthService implements Service {
    private int gameIDCtr = 1;

    public Result listGames(ListGamesRequest listGamesRequest) {
        if (!AUTH_DAO.existsAuth(listGamesRequest.authToken())) {
            return new Result(401, new ErrorResult("Error: unauthorized"));
        }
        try {
            Collection<GameData> games = GAME_DAO.listGames();
            return new Result(200, new ListGamesResult(games));
        } catch (DataAccessException e) {
            return new Result(500, new ErrorResult(e.getMessage()));
        }
    }

    public Result createGame(RequestWithAuth request) {
        CreateGameRequest createGameRequest = ((CreateGameRequest) request.request());
        String gameName = createGameRequest.gameName();
        String authToken = request.authToken();
        if (!AUTH_DAO.existsAuth(authToken)) {
            return new Result(401, new ErrorResult("Error: unauthorized"));
        }
        int gameID = gameIDCtr++;
        var newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        try {
            GAME_DAO.createGame(newGame);
            return new Result(200, new CreateGameResult(gameID));
        } catch (DataAccessException e) {
            return new Result(400, new ErrorResult(e.getMessage()));
        }
    }

    public Result joinGame(RequestWithAuth request) {
        JoinGameRequest joinGameRequest = ((JoinGameRequest) request.request());
        String joinColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();
        String authToken = request.authToken();
        String username;
        GameData game;
        try {
            username = AUTH_DAO.getAuthByToken(authToken).username();
        } catch (DataAccessException e) {
            return new Result(401, new ErrorResult(e.getMessage()));
        }

        try {
            game = GAME_DAO.getGame(gameID);
        } catch (DataAccessException e) {
            return new Result(400, new ErrorResult(e.getMessage()));
        }

        return tryJoiningGame(game, joinColor, username);
    }

    private Boolean isColorTaken(GameData game, String playerColor) {
        if (Objects.equals(playerColor, "WHITE")) {
            return game.whiteUsername() != null;
        } else {
            return game.blackUsername() != null;
        }
    }

    private GameData updatePlayerColor(GameData game, String playerColor, String username) {
        GameData gameUpdate;
        if (Objects.equals(playerColor, "WHITE")) {
            gameUpdate = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            gameUpdate = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        return gameUpdate;
    }

    private Result tryJoiningGame(GameData game, String joinColor, String username) {
        if (isColorTaken(game, joinColor)) {
            return new Result(403, new ErrorResult("Error: already taken"));
        } else if (joinColor == null) {
            return new Result(400, new ErrorResult("Error: bad request"));
        } else {
            try {
                GameData gameUpdate = updatePlayerColor(game, joinColor, username);
                GAME_DAO.updateGame(gameUpdate);
                return new Result(200, new EmptyResult());
            } catch (DataAccessException e) {
                return new Result(400, new ErrorResult(e.getMessage()));
            }
        }
    }
}
