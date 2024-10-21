package service;

import dataAccess.DataAccessException;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.EmptyResult;
import model.result.ErrorResult;
import model.result.Response;

import java.util.Objects;

public class UserService extends AuthService implements Service {

    public Response register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        var newUser = new UserData(username, password, email);
        try {
            userDAO.createUser(newUser);
            return login(new LoginRequest(username, password));
        } catch (DataAccessException e) {
            return new Response(403, new ErrorResult(e.getMessage()));
        }
    }

    public Response login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        try {
            var user = userDAO.getUser(username);
            if (isPasswordCorrect(user, password)) {
                return createAuth(username);
            } else {
                return new Response(401, new ErrorResult("unauthorized"));
            }
        } catch (DataAccessException e) {
            return new Response(403, new ErrorResult(e.getMessage()));
        }
    }

    public Response logout(LogoutRequest logoutRequest) {
        String authToken = logoutRequest.authToken();
        try {
            authDAO.removeAuth(authToken);
            return new Response(200, new EmptyResult());
        } catch (DataAccessException e) {
            var responseBody = new ErrorResult(e.getMessage());
            if (e.getClass() == DataAccessException.class) {
                return new Response(401, responseBody);
            } else {
                return new Response(500, responseBody);
            }
        }
    }

    private Boolean isPasswordCorrect(UserData user, String password) {
        return Objects.equals(user.password(), password);
    }
}
