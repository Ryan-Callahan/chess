import chess.ChessGame;
import clients.ServerFacade;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    String username = "testusername";
    String password = "testpassword";
    String email = "testemail";
    String gameName = "testGameName";
    GameData defaultGameData;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade.clear();
        facade.register(username, password, email);
        facade.createGame(gameName);
        defaultGameData = new GameData(1, null, null, gameName, new ChessGame());
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerTest() {
        Assertions.assertDoesNotThrow(() -> facade.register("newusername", "newpassword", "newemail"));
    }

    @Test
    public void registerFailTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.register(username, password, email));
    }

    @Test
    public void loginTest() {
        Assertions.assertDoesNotThrow(() -> facade.login(username, password));
    }

    @Test
    public void invalidLoginTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login(username, "bogus password"));
        Assertions.assertThrows(ResponseException.class, () -> facade.login("bogus username", password));
    }

    @Test
    public void logoutTest() {
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void invalidLogoutTest() throws ResponseException {
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.logout());
    }

    @Test
    public void createGameTest() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.createGame("newGameName"));
        Assertions.assertDoesNotThrow(() -> facade.createGame(gameName));
        var expectedGameData = new GameData(2, null, null, "newGameName",
                new ChessGame());
        assertIsEquivalentGame(facade.observeGame(2), expectedGameData);
    }

    @Test
    public void invalidCreateGameTest() throws ResponseException {
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(gameName));
    }

    @Test
    public void listGamesTest() {
        Assertions.assertDoesNotThrow(() ->
                Assertions.assertTrue(facade.listGames().contains(defaultGameData)));
    }

    @Test
    public void invalidListGamesTest() throws ResponseException {
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames());
    }

    @Test
    public void joinGameTest() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.joinGame("wHiTe", 1));
        var expectedGameData = new GameData(defaultGameData.gameID(), username, defaultGameData.blackUsername(),
                defaultGameData.gameName(), defaultGameData.game());
        assertIsEquivalentGame(facade.observeGame(1), expectedGameData);

        Assertions.assertDoesNotThrow(() -> facade.joinGame("bLaCk", 1));
        expectedGameData = new GameData(defaultGameData.gameID(), username, username, defaultGameData.gameName(),
                defaultGameData.game());
        assertIsEquivalentGame(facade.observeGame(1), expectedGameData);
    }

    @Test
    public void invalidJoinGameTest() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame("Whimte", 1));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame("BleCk", 1));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame("white", 2));
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame("white", 1));
    }

    @Test
    public void observeGameTest() {
        Assertions.assertDoesNotThrow(() ->
                assertIsEquivalentGame(facade.observeGame(1), defaultGameData)
        );
    }

    @Test
    public void invalidObserveGameTest() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> facade.observeGame(2));
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.observeGame(1));
    }

    private void assertIsEquivalentGame(GameData actualGameData, GameData expectedGameData) {
        Assertions.assertEquals(actualGameData.gameID(), expectedGameData.gameID());
        Assertions.assertEquals(actualGameData.whiteUsername(), expectedGameData.whiteUsername());
        Assertions.assertEquals(actualGameData.blackUsername(), expectedGameData.blackUsername());
        Assertions.assertEquals(actualGameData.gameName(), expectedGameData.gameName());
    }
}
