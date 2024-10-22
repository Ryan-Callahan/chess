package dataAccess;

import dataAccess.interfaces.AuthDAO;
import model.AuthData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authTable = new HashMap<>();
    @Override
    public void createAuth(AuthData auth){
        authTable.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuthByToken(String authToken) throws DataAccessException {
        if (authTable.containsKey(authToken)) {
            return authTable.get(authToken);
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        if (authTable.containsKey(authToken)) {
            authTable.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() {
        authTable = new HashMap<>();
    }

    public Boolean existsAuth(String authToken) {
        return authTable.containsKey(authToken);
    }

    private Boolean usernameExists(String username) {
        for (AuthData auth : authTable.values()) {
            if (Objects.equals(username, auth.username())) {
                return true;
            }
        }
        return false;
    }
}
