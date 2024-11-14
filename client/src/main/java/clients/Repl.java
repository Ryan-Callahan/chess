package clients;

import server.ServerFacade;

import java.util.Scanner;

import static clients.ClientType.LOGGED_OUT;
import static ui.EscapeSequences.*;

public class Repl {
    private Client client;
    private ClientType currentClientType;
    private final ServerFacade server;


    public Repl(String serverUrl) {
        currentClientType = LOGGED_OUT;
        server = new ServerFacade(serverUrl);
        client = new PreloginClient(server);

    }

    public void run() {
        printWelcome();
        Scanner scanner = new Scanner(System.in);
        var result = "";
        var nextClientType = currentClientType;
        while (!result.equals("quit")) {
            checkClient(nextClientType);
            printPrompt();
            String line = scanner.nextLine();

            try {
                var results = client.eval(line).split("\\|\\|\\|");
                nextClientType = ClientType.valueOf(results[0]);
                result = results[1];
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

    private void checkClient(ClientType newType) {
        if (newType != currentClientType) {
            client = switch (newType) {
                case LOGGED_OUT -> new PreloginClient(server);
                case LOGGED_IN -> new PostloginClient(server);
                case IN_GAME -> new GameplayClient(server);
            };
            currentClientType = newType;
        }
    }
}