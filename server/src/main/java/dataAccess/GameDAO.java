package dataAccess;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames();

    void updateGame(GameData game) throws DataAccessException;

    void deleteGame(GameData game) throws DataAccessException;

    void clear();
}
