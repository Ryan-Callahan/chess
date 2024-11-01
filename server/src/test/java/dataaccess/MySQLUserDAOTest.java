package dataaccess;

import dataaccess.mysqldao.MySQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class MySQLUserDAOTest {
    MySQLUserDAO testUserDao = new MySQLUserDAO();
    final String testUsername = "username";
    final String testUsername2 = "username2";
    final String testPassword = "password";
    final String testEmail = "email";
    final String testEmail2 = "email2";
    final UserData testUser = new UserData(testUsername, testPassword, testEmail);
    final UserData testUser2 = new UserData(testUsername2, testPassword, testEmail2);

    @BeforeEach
    void setup() throws DataAccessException {
        testUserDao.clear();
        testUserDao.createUser(testUser);
    }

    @Test
    @DisplayName("Should create a user correctly")
    void createUser() throws DataAccessException {
        testUserDao.createUser(testUser2);
        var actual = testUserDao.getUser(testUsername2);
        assertSameUser(testUser2, actual);
    }

    @Test
    @DisplayName("Should fail to create a user that already exists")
    void createExistingUser() {
        Assertions.assertThrows(DataAccessException.class, () -> testUserDao.createUser(testUser));
    }

    @Test
    @DisplayName("Should get a user correctly")
    void getUser() throws DataAccessException {
        var actual = testUserDao.getUser(testUsername);
        assertSameUser(testUser, actual);
    }

    @Test
    @DisplayName("Should fail to get a nonexistent user")
    void getNonexistentUser() {
        Assertions.assertThrows(DataAccessException.class, () -> testUserDao.getUser(testUsername2));
    }

    @Test
    @DisplayName("Should clear the user db")
    void clear() throws DataAccessException {
        testUserDao.clear();
        Assertions.assertThrows(DataAccessException.class, () -> testUserDao.getUser(testUsername));
    }

    private void assertSameUser(UserData expectedUser, UserData actualUser) {
        Assertions.assertTrue(BCrypt.checkpw(expectedUser.password(), actualUser.password()));
        Assertions.assertEquals(expectedUser.username(), actualUser.username());
        Assertions.assertEquals(expectedUser.email(), actualUser.email());
    }
}
