package dataaccess.mysqldao;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.mysqldao.DatabaseManager.TESTS;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {
    private final String authTable;
    private final String[] createStatements;

    public MySQLAuthDAO() {
        if (TESTS) {
            authTable = "testauth";
        } else {
            authTable = "auth";
        }
        createStatements = getCreateStatements(authTable);
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO " + authTable + " (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username());
    }

    @Override
    public AuthData getAuthByToken(String authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM " + authTable + " WHERE authToken=?";
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return readAuth(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        if (existsAuth(authToken)) {
            var statement = "DELETE FROM " + authTable + " WHERE authToken=?";
            executeUpdate(statement, authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE " + authTable;
        executeUpdate(statement);
    }

    @Override
    public Boolean existsAuth(String authToken) {
        try {
            getAuthByToken(authToken);
            return true;
        } catch (DataAccessException ignored) {
            return false;
        }
    }

    private AuthData readAuth(ResultSet resultSet) throws SQLException {
        var authToken = resultSet.getString("authToken");
        var username = resultSet.getString("username");
        return new AuthData(authToken, username);
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof  String p) preparedStatement.setString(i + 1, p);
                    else if (param == null) preparedStatement.setNull(i + 1, NULL);
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private String[] getCreateStatements(String authTable) {
        return new String[]{
        """
        CREATE TABLE IF NOT EXISTS `%s` (
          `authToken` varchar(200) NOT NULL,
          `username` varchar(45) NOT NULL,
          PRIMARY KEY (`authToken`),
          KEY `username` (`username`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """.formatted(authTable)
        };
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
