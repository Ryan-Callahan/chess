package service;

import dataAccess.DataAccessException;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static serializer.GSerializer.deserialize;
import static serializer.GSerializer.serialize;
import static service.Service.userDAO;

public class UserServiceTest {
    UserService userService = new UserService();

    @Test
    @DisplayName("Should register a valid user")
    void registerTest() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        Object result = userService.register(registerRequest);

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

    }

    @Test
    @DisplayName("Should login correctly with correct data")
    void loginTest() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
    }

}
