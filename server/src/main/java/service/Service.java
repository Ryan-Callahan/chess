package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import dataAccess.interfaces.UserDAO;
import model.result.EmptyResult;
import model.result.Response;
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
            return new Response(200, new EmptyResult());
        } catch (Exception e) {
            return new Response(500, new ErrorResult(e.getMessage()));
        }
    }
}
