package service;

import dataAccess.DataAccessException;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.ErrorResult;

import java.util.Objects;

public class UserService extends AuthService implements Service {

    public Object register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        var newUser = new UserData(username, password, email);
        try {
            userDAO.createUser(newUser);
            return login(new LoginRequest(username, password));
        } catch (DataAccessException e) {
            return new ErrorResult(403, e.getMessage());
        }
    }

    public Object login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        try {
            var user = userDAO.getUser(username);
            if (isPasswordCorrect(user, password)) {
                return createAuth(username);
            } else {
                return new ErrorResult(401, "unauthorized");
            }
        } catch (DataAccessException e) {
            return new ErrorResult(403, e.getMessage());
        }
    }

    public Object logout(LogoutRequest logoutRequest) {
        return null;
    }

    private Boolean isPasswordCorrect(UserData user, String password) {
        return Objects.equals(user.password(), password);
    }
}
