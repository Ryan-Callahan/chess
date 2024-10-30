package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.request.RequestWithAuth;
import model.result.CreateGameResult;
import model.result.EmptyResult;
import model.result.ErrorResult;
import model.result.ListGamesResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static service.Service.*;

public class GameServiceTest {
    GameService gameService;

    @BeforeEach
    void setup() throws DataAccessException {
        gameService = new GameService();
        gameService.clearAllDB();
        USER_DAO.createUser(new UserData("username", "password", "email"));
        USER_DAO.createUser(new UserData("username2", "password2", "email2"));
        AUTH_DAO.createAuth(new AuthData("testToken", "username"));
        AUTH_DAO.createAuth(new AuthData("testToken2", "username2"));
    }

    @Test
    @DisplayName("Should create a game correctly")
    void createGameTest() {
        CreateGameRequest createGameRequest = new CreateGameRequest("testGame");
        RequestWithAuth request = new RequestWithAuth("testToken", createGameRequest);
        var response = gameService.createGame(request);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(CreateGameResult.class, response.body().getClass());

        CreateGameResult responseBody = ((CreateGameResult) response.body());
        Assertions.assertEquals(1, responseBody.gameID());
    }

    @Test
    @DisplayName("Should return an error when creating unauthorized")
    void unauthorizedCreateGameTest() {
        CreateGameRequest createGameRequest = new CreateGameRequest("testGame");
        RequestWithAuth request = new RequestWithAuth("falseTestToken", createGameRequest);
        var response = gameService.createGame(request);
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should correctly join a game")
    void joinGameTest() throws DataAccessException {
        GAME_DAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        RequestWithAuth request = new RequestWithAuth("testToken", joinGameRequest);
        var response = gameService.joinGame(request);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(EmptyResult.class, response.body().getClass());

        joinGameRequest = new JoinGameRequest("BLACK", 1);
        request = new RequestWithAuth("testToken2", joinGameRequest);
        response = gameService.joinGame(request);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(EmptyResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should return an error when player color is already occupied")
    void colorTakenTest() throws DataAccessException {
        GAME_DAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        RequestWithAuth request = new RequestWithAuth("testToken", joinGameRequest);
        var response = gameService.joinGame(request);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(EmptyResult.class, response.body().getClass());

        joinGameRequest = new JoinGameRequest("WHITE", 1);
        request = new RequestWithAuth("testToken2", joinGameRequest);
        response = gameService.joinGame(request);
        Assertions.assertEquals(403, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should return an error when joining with incorrect auth token")
    void invalidAuthTest() throws DataAccessException {
        GAME_DAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        RequestWithAuth request = new RequestWithAuth("invalidTestToken", joinGameRequest);
        var response = gameService.joinGame(request);
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should return an error when joining a nonexistent game")
    void invalidGameTest() throws DataAccessException {
        GAME_DAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 2);
        RequestWithAuth request = new RequestWithAuth("testToken", joinGameRequest);
        var response = gameService.joinGame(request);
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should correctly list games")
    void listGameTest() throws DataAccessException {
        var game1 = new GameData(1, null, null, "testGame", new ChessGame());
        var game2 = new GameData(2, "username", "", "testGame2", new ChessGame());
        var game3 = new GameData(3, "", "username2", "testGame3", new ChessGame());
        GAME_DAO.createGame(game1);
        GAME_DAO.createGame(game2);
        GAME_DAO.createGame(game3);

        ListGamesRequest listGamesRequest = new ListGamesRequest("testToken");
        var response = gameService.listGames(listGamesRequest);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(ListGamesResult.class, response.body().getClass());

        Collection<GameData> expectedGames = new HashSet<>(Arrays.asList(game1, game2, game3));
        Collection<GameData> games = ((ListGamesResult) response.body()).games();

        for (GameData game : games) {
            Assertions.assertTrue(game.equals(game1) || game.equals(game2) || game.equals(game3));
        }
    }

    @Test
    @DisplayName("Should return an error when trying to list unauthorized")
    void unauthorizedListTest() throws DataAccessException {
        var game1 = new GameData(1, null, null, "testGame", new ChessGame());
        GAME_DAO.createGame(game1);
        ListGamesRequest listGamesRequest = new ListGamesRequest("invalidTestToken");
        var response = gameService.listGames(listGamesRequest);
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }
}
