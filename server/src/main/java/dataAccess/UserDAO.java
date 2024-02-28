package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData getByUsername(String username);
    void createUser(String username, String password, String email);
    String getPassword(UserData user);
    void clear();

}
