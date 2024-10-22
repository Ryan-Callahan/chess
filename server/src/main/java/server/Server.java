package server;

import model.request.RegisterRequest;
import service.GameService;
import service.UserService;
import spark.*;

import static serializer.GSerializer.deserialize;
import static serializer.GSerializer.serialize;

public class Server {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);


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
}
