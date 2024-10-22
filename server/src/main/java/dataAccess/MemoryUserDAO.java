package dataAccess;

import dataAccess.interfaces.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> userTable = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (!userTable.containsKey(user.username())) {
            userTable.put(user.username(), user);
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
    public void updateUser(UserData user) throws DataAccessException {
        if (userTable.containsKey(user.username())) {
            userTable.put(user.username(), user);
        } else {
            throw new DataAccessException("Invalid User Update; User does not exist!");
        }
    }

    @Override
    public void deleteUser(UserData user) throws DataAccessException {
        if (userTable.containsKey(user.username())) {
            userTable.remove(user.username());
        } else {
            throw new DataAccessException("Invalid User Removal; User does not exist!");
        }
    }

    @Override
    public void clear() {
        userTable = new HashMap<>();
    }
}
