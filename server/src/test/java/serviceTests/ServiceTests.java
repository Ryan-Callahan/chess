package serviceTests;

import dataAccess.GameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import service.GameService;

//TODO implement
public class ServiceTests {

    @Test
    public void clearService() throws TestException {
        GameService game = new GameService();
        game.clear();
    }
}
