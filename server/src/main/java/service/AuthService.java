package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.result.ErrorResult;
import model.result.LoginResult;
import model.result.Response;

import java.util.UUID;

public class AuthService implements Service {
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected Response createAuth(String username) {
        var newAuth = new AuthData(generateAuthToken(), username);
        try {
            authDAO.createAuth(newAuth);
            return new Response(200, new LoginResult(username, newAuth.authToken()));
        } catch (DataAccessException e) {
            return new Response(500, new ErrorResult(e.getMessage()));
        }
    }
}
