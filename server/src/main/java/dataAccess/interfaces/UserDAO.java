package dataAccess.interfaces;

import dataAccess.DataAccessException;
import model.UserData;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void updateUser(UserData user) throws DataAccessException;
    public void deleteUser(UserData user);
    public void clear();
}
