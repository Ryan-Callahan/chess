package server;

import model.request.*;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

import static server.serializer.GSerializer.deserialize;
import static server.serializer.GSerializer.serialize;

public class Server {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request request, Response response) {
        var clearResult = userService.clearAllDB();
        response.status(clearResult.statusCode());
        return serialize(clearResult.body());
    }

    private Object register(Request request, Response response) {
        var registerRequest = deserialize(request.body(), RegisterRequest.class);
        var registerResult = userService.register(registerRequest);
        response.status(registerResult.statusCode());
        return serialize(registerResult.body());
    }

    private Object login(Request request, Response response) {
        var loginRequest = deserialize(request.body(), LoginRequest.class);
        var registerResult = userService.login(loginRequest);
        response.status(registerResult.statusCode());
        return serialize(registerResult.body());
    }

    private Object logout(Request request, Response response) {
        var logoutRequest = new LogoutRequest(request.headers("authorization"));
        var logoutResult = userService.logout(logoutRequest);
        response.status(logoutResult.statusCode());
        return serialize(logoutResult.body());
    }

    private Object listGames(Request request, Response response) {
        var listGamesRequest = new ListGamesRequest(request.headers("authorization"));
        var listGamesResult = gameService.listGames(listGamesRequest);
        response.status(listGamesResult.statusCode());
        return serialize(listGamesResult.body());
    }

    private Object createGame(Request request, Response response) {
        var createGameRequest = new RequestWithAuth(request.headers("authorization"), deserialize(request.body(), CreateGameRequest.class));
        var createGameResult = gameService.createGame(createGameRequest);
        response.status(createGameResult.statusCode());
        return serialize(createGameResult.body());
    }

    private Object joinGame(Request request, Response response) {
        var joinGameRequest = new RequestWithAuth(request.headers("authorization"), deserialize(request.body(), JoinGameRequest.class));
        var joinGameResult = gameService.joinGame(joinGameRequest);
        response.status(joinGameResult.statusCode());
        return serialize(joinGameResult.body());
    }
}
