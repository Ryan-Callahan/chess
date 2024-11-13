package ui;

public class PostloginClient implements Client {
    public PostloginClient(String serverUrl) {

    }
    @Override
    public String help() {
        return """
               - logout
               - createGame <GAMENAME>
               - listGames
               - joinGame <GAMEID> <TEAMCOLOR>
               - observeGame <GAMEID>
               - help
               """;
    }

    @Override
    public String eval(String input) {
        return null;
    }
}
