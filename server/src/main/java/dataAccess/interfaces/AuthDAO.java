package dataAccess.interfaces;

import dataAccess.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData auth) throws DataAccessException;
    public AuthData getAuthByToken(String authToken) throws DataAccessException;
    public void updateAuth(AuthData auth) throws DataAccessException;
    public void removeAuth(String authToken);
    public  void clear();
}
