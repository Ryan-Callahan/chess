package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.EmptyResult;
import model.result.ErrorResult;
import model.result.LoginResult;
import model.result.Response;
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
        var response = userService.register(registerRequest);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(LoginResult.class, response.body().getClass());

        LoginResult responseBody = deserialize(serialize(response.body()), LoginResult.class);
        Assertions.assertEquals("username", responseBody.username());

        UserData expectedData = new UserData("username", "password", "email");
        UserData actualData = userDAO.getUser("username");
        Assertions.assertEquals(expectedData, actualData);
    }

    @Test
    @DisplayName("Should return 400 when registering an existing user")
    void invalidRegisterTest() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        userService.register(registerRequest);
        var response = userService.register(registerRequest);
        Assertions.assertEquals(403, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should login correctly with correct data")
    void loginTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        LoginRequest loginRequest = new LoginRequest("username", "password");
        var response = userService.login(loginRequest);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(LoginResult.class, response.body().getClass());

        LoginResult responseBody = deserialize(serialize(response.body()), LoginResult.class);
        Assertions.assertEquals("username", responseBody.username());
        Assertions.assertEquals("username", authDAO.getAuthByToken(responseBody.authToken()).username());
    }

    @Test
    @DisplayName("Should fail to log in a user with incorrect data")
    void incorrectLoginTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        LoginRequest loginRequest = new LoginRequest("username", "passw0rd");
        var response = userService.login(loginRequest);
        Assertions.assertEquals(401, response.statusCode()); //incorrect password
        Assertions.assertSame(ErrorResult.class, response.body().getClass());

        loginRequest = new LoginRequest("us3rname", "password");
        response = userService.login(loginRequest);
        Assertions.assertEquals(403, response.statusCode()); //incorrect user
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should fail to log in an already logged in user")
    void alreadyLoggedInTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        LoginRequest loginRequest = new LoginRequest("username", "passw0rd");
        userService.login(loginRequest);
        var response = userService.login(loginRequest);
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should log the user out")
    void logoutTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        authDAO.createAuth(new AuthData("testToken", "username"));
        LogoutRequest logoutRequest = new LogoutRequest("testToken");
        var response = userService.logout(logoutRequest);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertSame(EmptyResult.class, response.body().getClass());
    }

    @Test
    @DisplayName("Should fail to log the user out without authentication")
    void logoutFailTest() {
        LogoutRequest logoutRequest = new LogoutRequest("testToken");
        var response = userService.logout(logoutRequest);
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertSame(ErrorResult.class, response.body().getClass());
    }

}
