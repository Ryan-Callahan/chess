package service;

import dataAccess.memoryDAO.MemoryAuthDAO;
import dataAccess.memoryDAO.MemoryGameDAO;
import dataAccess.memoryDAO.MemoryUserDAO;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.result.EmptyResult;
import model.result.ErrorResult;
import model.result.Result;

public interface Service {
    GameDAO gameDAO = new MemoryGameDAO();
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    default Result clearAllDB() {
        try {
            gameDAO.clear();
            userDAO.clear();
            authDAO.clear();
            return new Result(200, new EmptyResult());
        } catch (Exception e) {
            return new Result(500, new ErrorResult(e.getMessage()));
        }
    }
}
