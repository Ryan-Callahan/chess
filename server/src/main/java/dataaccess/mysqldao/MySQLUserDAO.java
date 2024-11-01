package dataaccess.mysqldao;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.mysqldao.DatabaseManager.TESTS;
import static dataaccess.mysqldao.MySQLDAO.configureDatabase;
import static dataaccess.mysqldao.MySQLDAO.executeUpdate;

public class MySQLUserDAO implements UserDAO {
    private final String userTable;

    public MySQLUserDAO() {
        if (TESTS) {
            userTable = "testuser";
        } else {
            userTable = "user";
        }
        String[] createStatements = getCreateStatements(userTable);
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO " + userTable + " (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, user.username(), hashPassword(user.password()), user.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM " + userTable + " WHERE username=?";
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return readUser(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE " + userTable;
        executeUpdate(statement);
    }

    private UserData readUser(ResultSet resultSet) throws SQLException {
        var username = resultSet.getString("username");
        var password = resultSet.getString("password");
        var email = resultSet.getString("email");
        return new UserData(username, password, email);
    }

    private String[] getCreateStatements(String userTable) {
        return new String[]{
        """
        CREATE TABLE IF NOT EXISTS %s (
            `username` varchar(45) NOT NULL,
            `password` varchar(100) NOT NULL,
            `email` varchar(100) NOT NULL,
            PRIMARY KEY (`username`),
            UNIQUE KEY `username_UNIQUE` (`username`),
            UNIQUE KEY `email_UNIQUE` (`email`)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """.formatted(userTable)
        };
    }
}
