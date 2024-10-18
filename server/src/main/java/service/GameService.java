package service;

import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.ListGamesResult;

public class GameService extends AuthService implements Service {

    public GameService() {
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        return null;
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) {
        return null;
    }
}
