package service;

import dataAccess.DataAccessException;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.EmptyResult;
import model.result.ErrorResult;
import model.result.Result;

import java.util.Objects;

import static serializer.GSerializer.serialize;

public class UserService extends AuthService implements Service {

    public Result register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        var newUser = new UserData(username, password, email);
        try {
            if (password != null) {
                userDAO.createUser(newUser);
                return login(new LoginRequest(username, password));
            } else {
                return new Result(400, new ErrorResult("Error: bad request"));
            }
        } catch (DataAccessException e) {
            return new Result(403, new ErrorResult(e.getMessage()));
        }
    }

    public Result login(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        try {
            var user = userDAO.getUser(username);
            if (isPasswordCorrect(user, password)) {
                return createAuth(username);
            } else {
                return new Result(401, new ErrorResult("Error: unauthorized"));
            }
        } catch (DataAccessException e) {
            return new Result(401, new ErrorResult(e.getMessage()));
        }
    }

    public Result logout(LogoutRequest logoutRequest) {
        String authToken = logoutRequest.authToken();
        try {
            authDAO.removeAuth(authToken);
            return new Result(200, new EmptyResult());
        } catch (DataAccessException e) {
            var responseBody = new ErrorResult(e.getMessage());
            if (e.getClass() == DataAccessException.class) {
                return new Result(401, responseBody);
            } else {
                return new Result(500, responseBody);
            }
        }
    }

    private Boolean isPasswordCorrect(UserData user, String password) {
        return Objects.equals(user.password(), password);
    }
}
