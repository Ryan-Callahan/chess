package dataaccess.memorydao;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authTable = new HashMap<>();

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        if (!authTable.containsKey(auth.authToken())) {
            authTable.put(auth.authToken(), auth);
        } else {
            throw new DataAccessException("Error: auth token generation failed; auth token already exists");
        }

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
}
