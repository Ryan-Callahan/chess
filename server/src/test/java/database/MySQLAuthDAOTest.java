package database;

import dataaccess.DataAccessException;
import dataaccess.mysqldao.MySQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;

public class MySQLAuthDAOTest {
    MySQLAuthDAO testAuthDao = new MySQLAuthDAO();
    final String testAuthToken = "testToken";
    final String testAuthToken2 = "testToken2";
    final String testUsername = "username";
    final String testUsername2 = "username2";
    final AuthData testAuth = new AuthData(testAuthToken, testUsername);
    final AuthData testAuth2 = new AuthData(testAuthToken2, testUsername2);

    @BeforeEach
    void setup() throws DataAccessException {
        testAuthDao.clear();
        testAuthDao.createAuth(testAuth);
    }

    @Test
    @DisplayName("Should create an auth correctly")
    void createAuthTest() throws DataAccessException {
        testAuthDao.createAuth(testAuth2);
        var actual = testAuthDao.getAuthByToken(testAuthToken2);
        Assertions.assertEquals(testAuth2, actual);
    }

    @Test
    @DisplayName("Should fail to create an auth when the auth token already exists")
    void createFailTest() {
        Assertions.assertThrows(DataAccessException.class, () -> testAuthDao.createAuth(testAuth));
    }

    @Test
    @DisplayName("Should retrieve the correct auth")
    void getAuthTest() throws DataAccessException {
        var actual = testAuthDao.getAuthByToken(testAuthToken);
        Assertions.assertEquals(testAuth, actual);
    }

    @Test
    @DisplayName("Should fail to get auth when bad authToken")
    void getBadAuthTokenTest() {
        Assertions.assertThrows(DataAccessException.class, () -> testAuthDao.getAuthByToken(testAuthToken2));
    }

    @Test
    @DisplayName("Should remove auth")
    void removeAuthTest() throws DataAccessException {
        testAuthDao.removeAuth(testAuthToken);
        Assertions.assertThrows(DataAccessException.class, () -> testAuthDao.getAuthByToken(testAuthToken));
    }

    @Test
    @DisplayName("Should fail to remove auth")
    void failRemoveAuthTest() {
        Assertions.assertThrows(DataAccessException.class, () -> testAuthDao.removeAuth(testAuthToken2));
    }

    @Test
    @DisplayName("Should clear the auth db")
    void clearAuthTest() throws DataAccessException {
        testAuthDao.clear();
        Assertions.assertThrows(DataAccessException.class, () -> testAuthDao.getAuthByToken(testAuthToken));
    }

    @Test
    @DisplayName("should return true if auth exists")
    void existsAuthTest() {
        Assertions.assertTrue(testAuthDao.existsAuth(testAuthToken));
    }

    @Test
    @DisplayName("should return false if auth exists")
    void notExistsAuthTest() {
        Assertions.assertFalse(testAuthDao.existsAuth(testAuthToken2));
    }
}
