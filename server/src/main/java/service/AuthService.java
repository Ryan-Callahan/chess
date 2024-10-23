package service;

import model.AuthData;
import model.result.LoginResult;
import model.result.Result;

import java.util.UUID;

public class AuthService implements Service {
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected Result createAuth(String username) {
        var newAuth = new AuthData(generateAuthToken(), username);
        AUTH_DAO.createAuth(newAuth);
        return new Result(200, new LoginResult(newAuth.authToken(), username));
    }
}
