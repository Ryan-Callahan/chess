package dataaccess.mysqldao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import server.serializer.GSerializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public int createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO game (whiteUsername, blackUsername, name, chessGame) VALUES (?, ?, ?, ?)";
        return executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE id=?";
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
            var statement = "SELECT * FROM game";
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
        var statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, chessGame = ? WHERE id = ?";
        var chessGame = GSerializer.serialize(game.game());
        executeUpdate(statement, game.whiteUsername(), game.blackUsername(), chessGame, game.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
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
                    if (param instanceof  Integer p) preparedStatement.setInt(i + 1, p);
                    if (param instanceof  String p) preparedStatement.setString(i + 1, p);
                    if (param instanceof  ChessGame p) preparedStatement.setString(i + 1, GSerializer.serialize(p));
                    else if (param == null) preparedStatement.setNull(i + 1, NULL);
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

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS `game` (
          `id` int NOT NULL AUTO_INCREMENT,
          `whiteUsername` varchar(45) DEFAULT NULL,
          `blackUsername` varchar(45) DEFAULT NULL,
          `name` varchar(45) NOT NULL,
          `chessGame` json NOT NULL,
          PRIMARY KEY (`id`),
          UNIQUE KEY `id_UNIQUE` (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var connection = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", e.getMessage()));
        }
    }
}
