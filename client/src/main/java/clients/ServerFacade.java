package clients;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ListGamesResult;
import model.result.LoginResult;
import serializer.GSerializer;
import websocket.WSClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {
    private final String serverUrl;
        private String authToken = null;
    private String username = null;
    private ChessGame.TeamColor color = null;
    private WSFacade ws = null;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public WSFacade initWebSocket(GameData game) throws Exception {
        this.ws = new WSFacade(serverUrl, this, game);
        return this.ws;
    }

    public WSFacade getWebSocket() {
        return this.ws;
    }

    public void register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        var registerRequest = new RegisterRequest(username, password, email);
        this.username = username;
        authToken = makeRequest("POST", path, registerRequest, LoginResult.class).authToken();
    }

    public void login(String username, String password) throws ResponseException {
        var path = "/session";
        var loginRequest = new LoginRequest(username, password);
        this.username = username;
        authToken = makeRequest("POST", path, loginRequest, LoginResult.class).authToken();
    }

    public void logout() throws ResponseException {
        var path = "/session";
        makeRequest("DELETE", path, null, null);
        expireAuthToken();
    }

    public void createGame(String gameName) throws ResponseException {
        var path = "/game";
        var createGameRequest = new CreateGameRequest(gameName);
        makeRequest("POST", path, createGameRequest, null);
    }

    public Collection<GameData> listGames() throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, null, ListGamesResult.class).games();
    }

    public void joinGame(String playerColor, int gameID) throws ResponseException {
        var path = "/game";
        var joinGameRequest = new JoinGameRequest(playerColor, gameID);
        this.color = ChessGame.TeamColor.valueOf(playerColor);
        makeRequest("PUT", path, joinGameRequest, null);
    }

    public GameData observeGame(int gameID) throws ResponseException {
        var games = listGames();
        try {
            return games.stream().filter(gameData -> gameData.gameID() == gameID).findFirst().get();
        } catch (Exception e) {
            throw new ResponseException(400, e.getMessage());
        }
    }

    public void clear() throws ResponseException {
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeAuth(http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = GSerializer.serialize(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void writeAuth(HttpURLConnection http) {
        if (authToken != null) {
            http.addRequestProperty("authorization", authToken);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        var response = http.getResponseMessage();
        if (status != 200) {
            throw new ResponseException(status, "Failure: " + response);
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream responseBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(responseBody);
                if (responseClass != null) {
                    response = GSerializer.deserialize(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void expireAuthToken() {
        username = null;
        authToken = null;
    }
}
