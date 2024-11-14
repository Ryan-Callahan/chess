package server;

import exception.ResponseException;
import model.GameData;
import model.request.*;
import model.result.ListGamesResult;
import model.result.LoginResult;
import serializer.GSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;
    private String authToken = null;
    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        authToken = makeRequest("POST", path, registerRequest, LoginResult.class).authToken();
    }

    public void login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        authToken = makeRequest("POST", path, loginRequest, LoginResult.class).authToken();
    }

    public void logout() throws ResponseException {
        var path = "/session";
        makeRequest("DELETE", path, null, null);
        expireAuthToken();
    }

    public void createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        makeRequest("POST", path, createGameRequest, null);
    }

    public ListGamesResult listGames() throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, null, ListGamesResult.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        makeRequest("PUT", path, joinGameRequest, null);
    }

    public GameData observeGame(int gameID) throws ResponseException {
        var games = listGames().games();
        return games.stream().filter(gameData -> gameData.gameID() == gameID).findFirst().get();
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
        if (status != 200){
            throw new ResponseException(status, "failure: " + status + response);
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
        authToken = null;
    }
}
