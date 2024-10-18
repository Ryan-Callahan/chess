package dataAccess;

import dataAccess.interfaces.AuthDAO;
import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authTable = new HashMap<>();
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        if (!usernameExists(auth.username())) {
            authTable.put(auth.authToken(), auth);
        } else {
            throw new DataAccessException("Username is already associated with an authToken");
        }

    }

    @Override
    public AuthData getAuthByToken(String authToken) throws DataAccessException {
        if (authTable.containsKey(authToken)) {
            return authTable.get(authToken);
        } else {
            throw new DataAccessException("Invalid Auth Token; Could not find authentication!");
        }
    }

    @Override
    public void removeAuth(String authToken) {
        authTable.remove(authToken);
    }

    @Override
    public void clear() {
        authTable = new HashMap<>();
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
