package dataAccess;

import dataAccess.DataAccessException;
import model.UserData;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void updateUser(UserData user) throws DataAccessException;

    void deleteUser(UserData user) throws DataAccessException;

    void clear();
}