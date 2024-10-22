package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.result.ErrorResult;
import model.result.LoginResult;
import model.result.Result;

import java.util.UUID;

public class AuthService implements Service {
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected Result createAuth(String username) {
        var newAuth = new AuthData(generateAuthToken(), username);
        try {
            authDAO.createAuth(newAuth);
            return new Result(200, new LoginResult(newAuth.authToken(), username));
        } catch (DataAccessException e) {
            return new Result(500, new ErrorResult(e.getMessage()));
        }
    }
}
