package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.result.ErrorResult;
import model.result.LoginResult;

import java.util.UUID;

public class AuthService implements Service {
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected Object createAuth(String username) {
        var newAuth = new AuthData(generateAuthToken(), username);
        try {
            authDAO.createAuth(newAuth);
            return new LoginResult(200, username, newAuth.authToken());
        } catch (DataAccessException e) {
            return new ErrorResult(500, e.getMessage());
        }
    }
}
