package service;

import dataAccess.MemoryUserDAO;
import dataAccess.interfaces.UserDAO;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.LogoutResult;
import model.result.RegisterResult;

public class UserService extends AuthService {
    private UserDAO userDAO;
    public UserService() {
        userDAO = new MemoryUserDAO();
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        return null;
    }

    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }

    public LogoutResult logout(LogoutRequest logoutRequest) {
        return null;
    }
}
