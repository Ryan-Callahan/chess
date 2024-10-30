package service;

import dataaccess.DataAccessException;
import dataaccess.memorydao.MemoryAuthDAO;
import dataaccess.memorydao.MemoryGameDAO;
import dataaccess.memorydao.MemoryUserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.mysqldao.MySQLAuthDAO;
import dataaccess.mysqldao.MySQLUserDAO;
import model.result.EmptyResult;
import model.result.ErrorResult;
import model.result.Result;

public interface Service {
    GameDAO GAME_DAO = new MemoryGameDAO();
    UserDAO USER_DAO = new MySQLUserDAO();
    AuthDAO AUTH_DAO = new MySQLAuthDAO();

    default Result clearAllDB() {
        try {
            GAME_DAO.clear();
            USER_DAO.clear();
            AUTH_DAO.clear();
            return new Result(200, new EmptyResult());
        } catch (DataAccessException e) {
            return new Result(500, new ErrorResult(e.getMessage()));
        }
    }
}
