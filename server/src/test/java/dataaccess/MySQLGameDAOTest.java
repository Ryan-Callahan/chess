package dataaccess;

import chess.ChessGame;
import dataaccess.mysqldao.MySQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MySQLGameDAOTest {
    MySQLGameDAO testGameDao = new MySQLGameDAO();
    final String whiteUsername = "white";
    final String blackUsername = "black";
    final String gameName = "game";
    final String gameName2 = "game2";
    final ChessGame game = new ChessGame();
    final ChessGame game2 = new ChessGame();
    final GameData testGame = new GameData(1, whiteUsername, blackUsername, gameName, game);
    final GameData testGame2 = new GameData(2, blackUsername, whiteUsername, gameName2, game2);

    @BeforeEach
    void setup() throws DataAccessException {
        testGameDao.clear();
        testGameDao.createGame(testGame);
    }

    @Test
    @DisplayName("Should create game correctly")
    void createGameTest() throws DataAccessException {
        testGameDao.createGame(testGame2);
        var actual = testGameDao.getGame(testGame2.gameID());
        Assertions.assertEquals(testGame2, actual);
    }

    @Test
    @DisplayName("Should fail to create existing game")
    void createExistingGameTest() {
        Assertions.assertThrows(DataAccessException.class, () -> testGameDao.createGame(testGame));
    }

    @Test
    @DisplayName("Should get game correctly")
    void getGameTest() throws DataAccessException {
        var actual = testGameDao.getGame(testGame.gameID());
        Assertions.assertEquals(testGame, actual);
    }

    @Test
    @DisplayName("Should fail to get nonexistent game")
    void getNonexistentGameTest() {
        Assertions.assertThrows(DataAccessException.class, () -> testGameDao.getGame(testGame2.gameID()));
    }

    @Test
    @DisplayName("Should list games correctly")
    void listGamesTest() throws DataAccessException {
        var gamesList = testGameDao.listGames();
        Assertions.assertTrue(gamesList.contains(testGame));
        Assertions.assertFalse(gamesList.contains(testGame2));
    }

    @Test
    @DisplayName("Should somehow list games incorrectly even though it's not possible...")
    void listGamesFailTest() throws DataAccessException {
        testGameDao.clear();
        Assertions.assertTrue(testGameDao.listGames().isEmpty());
    }

    @Test
    @DisplayName("Should update game correctly")
    void updateGameTest() throws DataAccessException {
        var newChessGame = new ChessGame();
        var updatedGame = new GameData(testGame.gameID(), blackUsername, blackUsername, testGame.gameName(), newChessGame);
        testGameDao.updateGame(updatedGame);
        var actual = testGameDao.getGame(testGame.gameID());
        Assertions.assertNotEquals(testGame.whiteUsername(), actual.whiteUsername());
        Assertions.assertEquals(testGame.gameName(), actual.gameName());
        Assertions.assertEquals(testGame.gameID(), actual.gameID());
        Assertions.assertEquals(testGame.game(), actual.game());
        Assertions.assertEquals(testGame.blackUsername(), actual.blackUsername());

        newChessGame.setTeamTurn(ChessGame.TeamColor.BLACK);
        updatedGame = new GameData(actual.gameID(), actual.whiteUsername(), actual.blackUsername(), actual.gameName(), newChessGame);
        testGameDao.updateGame(updatedGame);
        actual = testGameDao.getGame(testGame.gameID());
        Assertions.assertNotEquals(testGame.game(), actual.game());
    }

    @Test
    @DisplayName("Should fail to update game with invalid gameID")
    void updateGameFailTest() {
        var badGameData = new GameData(2, whiteUsername, blackUsername, gameName, game);
        Assertions.assertThrows(DataAccessException.class, () -> testGameDao.updateGame(badGameData));
    }

    @Test
    @DisplayName("Should clear the game table")
    void clearGameTest() throws DataAccessException {
        testGameDao.clear();
        Assertions.assertThrows(DataAccessException.class, () -> testGameDao.getGame(1));
    }
}
