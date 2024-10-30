package dataaccess.memorydao;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> userTable = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (!userTable.containsKey(user.username())) {
            var hashedUserData = new UserData(user.username(), hashPassword(user.password()), user.email());
            userTable.put(user.username(), hashedUserData);
        } else {
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (userTable.containsKey(username)) {
            return userTable.get(username);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() {
        userTable = new HashMap<>();
    }
}
