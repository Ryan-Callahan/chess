package dataaccess.mysqldao;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {
    @Override
    public void createAuth(AuthData auth) {

    }

    @Override
    public AuthData getAuthByToken(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() {

    }

    @Override
    public Boolean existsAuth(String authToken) {
        return null;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof  String p) preparedStatement.setString(i + 1, p);
                    else if (param == null) preparedStatement.setNull(i + 1, NULL);
                }
                preparedStatement.executeUpdate();

                var returnStatement = preparedStatement.getGeneratedKeys();
                if (returnStatement.next()) {
                    return returnStatement.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS `auth` (
          `authToken` varchar(200) NOT NULL,
          `username` varchar(45) NOT NULL,
          PRIMARY KEY (`authToken`),
          KEY `username` (`username`)
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
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
