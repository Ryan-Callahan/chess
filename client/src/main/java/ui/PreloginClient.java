package ui;

import server.ServerFacade;

public class PreloginClient implements Client {
    private final ServerFacade server;
    public PreloginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    @Override
    public String eval(String input) {
        try {
            String command = input;
            return switch (command) {
                case "login" -> null;
                case "register" -> null;
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
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
}
