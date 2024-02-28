package dataAccess;

import model.AuthData;

public interface AuthDAO {
    String createAuth(String username);
    AuthData getAuthByAuthToken(String authToken);
    String getUsernameByAuthToken(String authToken);
    void removeAuth(String authToken);
    void update(String username, String authToken);
    void clear();
}
