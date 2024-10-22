package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
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

import java.util.Collection;

import static service.Service.*;

public class GameServiceTest {
    GameService gameService;

    @BeforeEach
    void setup() throws DataAccessException {
        gameService = new GameService();
        gameService.clearAllDB();
        userDAO.createUser(new UserData("username", "password", "email"));
        userDAO.createUser(new UserData("username2", "password2", "email2"));
        authDAO.createAuth(new AuthData("testToken", "username"));
        authDAO.createAuth(new AuthData("testToken2", "username2"));
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
        gameDAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
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
        gameDAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
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
        gameDAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        RequestWithAuth request = new RequestWithAuth("invalidTestToken", joinGameRequest);
        var response = gameService.joinGame(request);
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should return an error when joining a nonexistent game")
    void invalidGameTest() throws DataAccessException {
        gameDAO.createGame(new GameData(1, null, null, "testGame", new ChessGame()));
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
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameDAO.createGame(game3);

        ListGamesRequest listGamesRequest = new ListGamesRequest("testToken");
        var response = gameService.listGames(listGamesRequest);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(ListGamesResult.class, response.body().getClass());

        Collection<GameData> games = ((ListGamesResult) response.body()).games();

        Assertions.assertTrue(games.contains(game1));
        Assertions.assertTrue(games.contains(game2));
        Assertions.assertTrue(games.contains(game3));
    }

    @Test
    @DisplayName("Should return an error when trying to list unauthorized")
    void unauthorizedListTest() throws DataAccessException {
        var game1 = new GameData(1, null, null, "testGame", new ChessGame());
        gameDAO.createGame(game1);
        ListGamesRequest listGamesRequest = new ListGamesRequest("invalidTestToken");
        var response = gameService.listGames(listGamesRequest);
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }
}
