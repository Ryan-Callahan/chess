package dataAccess.interfaces;

import dataAccess.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData auth);

    AuthData getAuthByToken(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    void clear();

    Boolean existsAuth(String authToken);
}
