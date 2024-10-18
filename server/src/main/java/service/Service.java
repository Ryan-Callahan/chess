package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import dataAccess.interfaces.UserDAO;
import model.result.EmptyResult;
import model.result.ErrorResult;

public interface Service {
    GameDAO gameDAO = new MemoryGameDAO();
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    default Object clearAllDB() {
        try {
            gameDAO.clear();
            userDAO.clear();
            authDAO.clear();
            return new EmptyResult(200);
        } catch (Exception e) {
            return new ErrorResult(500, e.getMessage());
        }
    }
}
