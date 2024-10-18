package service;

import dataAccess.DataAccessException;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ErrorResult;
import model.result.LoginResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static serializer.GSerializer.deserialize;
import static serializer.GSerializer.serialize;
import static service.Service.authDAO;
import static service.Service.userDAO;

public class UserServiceTest {
    UserService userService = new UserService();

    @BeforeEach
    void setup() {
        userService.clearAllDB();
    }

    @Test
    @DisplayName("Should register and log in a valid user")
    void registerTest() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        var result = userService.register(registerRequest);
        Assertions.assertSame(LoginResult.class, result.getClass());

        LoginResult actualResult = deserialize(serialize(result), LoginResult.class);
        Assertions.assertEquals("username", actualResult.username());

        UserData expectedData = new UserData("username", "password", "email");
        UserData actualData = userDAO.getUser("username");
        Assertions.assertEquals(expectedData, actualData);
    }

    @Test
    @DisplayName("Should return 400 when registering an existing user")
    void invalidRegisterTest() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        userService.register(registerRequest);
        var result = userService.register(registerRequest);
        Assertions.assertSame(ErrorResult.class, result.getClass());

        ErrorResult actualResult = deserialize(serialize(result), ErrorResult.class);
        Assertions.assertEquals(403, actualResult.statusCode());
    }

    @Test
    @DisplayName("Should login correctly with correct data")
    void loginTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        LoginRequest loginRequest = new LoginRequest("username", "password");
        var result = userService.login(loginRequest);
        Assertions.assertSame(LoginResult.class, result.getClass());

        LoginResult actualResult = deserialize(serialize(result), LoginResult.class);
        Assertions.assertEquals("username", actualResult.username());
        Assertions.assertEquals("username", authDAO.getAuthByToken(actualResult.authToken()).username());
    }

    @Test
    @DisplayName("Should fail to log in a user with incorrect data")
    void incorrectLoginTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        LoginRequest loginRequest = new LoginRequest("username", "passw0rd");
        var result = userService.login(loginRequest);
        Assertions.assertSame(ErrorResult.class, result.getClass());

        ErrorResult actualResult = deserialize(serialize(result), ErrorResult.class);
        Assertions.assertEquals(401, actualResult.statusCode()); //incorrect password

        loginRequest = new LoginRequest("us3rname", "password");
        result = userService.login(loginRequest);
        Assertions.assertSame(ErrorResult.class, result.getClass());

        actualResult = deserialize(serialize(result), ErrorResult.class);
        Assertions.assertEquals(403, actualResult.statusCode()); //incorrect user
    }

    @Test
    @DisplayName("Should fail to log in an already logged in user")
    void alreadyLoggedInTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        LoginRequest loginRequest = new LoginRequest("username", "passw0rd");
        userService.login(loginRequest);
        var result = userService.login(loginRequest);
        Assertions.assertSame(ErrorResult.class, result.getClass());

        ErrorResult actualResult = deserialize(serialize(result), ErrorResult.class);
        Assertions.assertEquals(401, actualResult.statusCode());
    }

    @Test
    @DisplayName("Should log the user out")
    void logoutTest() {

    }

    @Test
    @DisplayName("Should fail to log the user out without authentication")
    void logoutFailTest() {

    }

}
