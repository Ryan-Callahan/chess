package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;

    AuthData getAuthByToken(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    void clear();

    Boolean existsAuth(String authToken);
}
