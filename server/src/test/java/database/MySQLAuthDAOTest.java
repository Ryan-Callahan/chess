package database;

import dataaccess.DataAccessException;
import dataaccess.mysqldao.MySQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MySQLAuthDAOTest {
    static MySQLAuthDAO testAuthDao = new MySQLAuthDAO();
    static final String testAuthToken = "testToken";
    final String testAuthToken2 = "testToken2";
    static final String testUsername = "username";
    final String testUsername2 = "username2";
    static final AuthData testAuth = new AuthData(testAuthToken, testUsername);
    final AuthData testAuth2 = new AuthData(testAuthToken2, testUsername2);

    @BeforeAll
    static void setup() throws DataAccessException {
        testAuthDao.clear();
        testAuthDao.createAuth(testAuth);
    }

    @Test
    @DisplayName("Should create an auth correctly")
    void createAuthTest() throws DataAccessException {
        testAuthDao.createAuth(testAuth2);

    }

    @Test
    @DisplayName("Should fail to create an auth when the auth token already exists")
    void createFailTest() {}

    @Test
    @DisplayName("Should retrieve the correct auth")
    void getAuthTest() {}

    @Test
    @DisplayName("Should fail to get auth when bad authToken")
    void getBadAuthTokenTest() {}

    @Test
    @DisplayName("Should remove auth")
    void removeAuthTest() {}

    @Test
    @DisplayName("Should fail to remove auth")
    void failRemoveAuthTest() {}

    @Test
    @DisplayName("Should clear the auth db")
    void clearAuthTest() {}
}
