package dataAccess.interfaces;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public void createGame(GameData game) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames();
    public void updateGame(GameData game) throws DataAccessException;
    public void deleteGame(GameData game);
    public void clear();
}
