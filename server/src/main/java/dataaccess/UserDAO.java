package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

    default String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
