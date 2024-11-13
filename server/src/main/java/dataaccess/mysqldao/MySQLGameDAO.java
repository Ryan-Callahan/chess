package dataaccess.mysqldao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import serializer.GSerializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import static dataaccess.mysqldao.DatabaseManager.TESTS;
import static dataaccess.mysqldao.MySQLDAO.configureDatabase;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO {
    private final String gameTable;

    public MySQLGameDAO() {
        if (TESTS) {
            gameTable = "testgame";
        } else {
            gameTable = "game";
        }
        String[] createStatements = getCreateStatements(gameTable);
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        if (isExistingGame(game)) {
            throw new DataAccessException("Error: attempting to create an existing game!");
        }
        var statement = "INSERT INTO " + gameTable + " (whiteUsername, blackUsername, name, chessGame) VALUES (?, ?, ?, ?)";
        return executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM " + gameTable + " WHERE id=?";
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return readGame(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Error: bad request");
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new HashSet<GameData>();
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM " + gameTable;
            try (var preparedStatement = connection.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(readGame(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (!isExistingGame(game)) {
            throw new DataAccessException("Error: Could not find game to update!");
        }
        var statement = "UPDATE " + gameTable + " SET whiteUsername = ?, blackUsername = ?, chessGame = ? WHERE id = ?";
        executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.game(), game.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE " + gameTable;
        executeUpdate(statement);
    }

    private Boolean isExistingGame(GameData game) throws DataAccessException {
        var gamesList = listGames().stream().map(GameData::gameID).toList();
        return gamesList.contains(game.gameID());
    }

    private GameData readGame(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt(1);
        var whiteUsername = resultSet.getString("whiteUsername");
        var blackUsername = resultSet.getString("blackUsername");
        var gameName = resultSet.getString("name");
        var chessGame = resultSet.getString("chessGame");
        return new GameData(id, whiteUsername, blackUsername, gameName, GSerializer.deserialize(chessGame, ChessGame.class));
    }


    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof  Integer p) {
                        preparedStatement.setInt(i + 1, p);
                    }
                    if (param instanceof  String p) {
                        preparedStatement.setString(i + 1, p);
                    }
                    if (param instanceof  ChessGame p) {
                        preparedStatement.setString(i + 1, GSerializer.serialize(p));
                    }
                    else if (param == null) {
                        preparedStatement.setNull(i + 1, NULL);
                    }
                }
                preparedStatement.executeUpdate();

                var resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private String[] getCreateStatements(String gameTable) {
        return new String[]{
        """
        CREATE TABLE IF NOT EXISTS `%s` (
          `id` int NOT NULL AUTO_INCREMENT,
          `whiteUsername` varchar(45) DEFAULT NULL,
          `blackUsername` varchar(45) DEFAULT NULL,
          `name` varchar(45) NOT NULL,
          `chessGame` json NOT NULL,
          PRIMARY KEY (`id`),
          UNIQUE KEY `id_UNIQUE` (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """.formatted(gameTable)
        };
    }
}
