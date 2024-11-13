package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private Client currentClient;

    public Repl(String serverUrl) {
        currentClient = new PreloginClient(serverUrl);
    }

    public void run() {
        printWelcome();
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = currentClient.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.toString());
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
        System.out.print(currentClient.help());
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
