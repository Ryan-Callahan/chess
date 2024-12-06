package clients;

import exception.ResponseException;

import java.util.Arrays;

import static clients.ClientType.LOGGED_OUT;

public class PreloginClient extends Client {
    public PreloginClient(ServerFacade server) {
        super(server);
        currentClient = LOGGED_OUT;
    }

    @Override
    public String help() {
        return """
                - login <USERNAME> <PASSWORD>
                - register <USERNAME> <PASSWORD> <EMAIL>
                - quit
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
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> response("quit");
                default -> response(help());
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            server.login(username, password);
            advanceClient();
            return response(String.format("You signed in as %s.", username));
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            server.register(username, password, email);
            advanceClient();
            return response(String.format("You registered and signed in as %s.", username));
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }
}
