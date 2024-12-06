package clients;

import exception.ResponseException;

import java.util.Scanner;

import static clients.ClientType.LOGGED_OUT;
import static ui.EscapeSequences.*;

public class Repl {
    public Client client;
    private ClientType currentClientType;
    private final ServerFacade server;


    public Repl(String serverUrl) {
        currentClientType = LOGGED_OUT;
        server = new ServerFacade(serverUrl);
        client = new PreloginClient(server, serverUrl);

    }

    public void run() {
        printWelcome();
        Scanner scanner = new Scanner(System.in);
        var result = "";
        var nextClientType = currentClientType;
        while (!result.equals("quit")) {
            try {
                checkClient(nextClientType);
                printPrompt();
                String line = scanner.nextLine();
                var results = client.eval(line).split("\\|\\|\\|");
                nextClientType = getNextClientType(results);
                result = results[results.length - 1];
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.print(e);
            }
        }
        System.out.println();
    }

    private void printWelcome() {
        System.out.println(
                """
                Welcome to the Chess server! Please sign in or register to start.
                """
        );
        System.out.print(client.help());
    }

    private void printPrompt() {
        System.out.printf("\n%s[%s]>>> %s", RESET_TEXT_COLOR, currentClientType, SET_TEXT_COLOR_GREEN);
    }

    private void checkClient(ClientType newType) throws ResponseException {
        if (newType != currentClientType) {
            client = switch (newType) {
                case LOGGED_OUT -> new PreloginClient(server, client.serverUrl);
                case LOGGED_IN -> new PostloginClient(server, client.serverUrl);
                case IN_GAME -> new GameplayClient(server, client.serverUrl, client.ws);
            };
            currentClientType = newType;
        }
    }

    private ClientType getNextClientType(String[] results) {
        try {
            return ClientType.valueOf(results[0]);
        } catch (Exception ignored) {
            return this.currentClientType;
        }
    }
}
