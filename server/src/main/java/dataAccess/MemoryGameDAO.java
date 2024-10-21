package dataAccess;

import dataAccess.interfaces.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> gameTable = new HashMap<>();
    @Override
    public void createGame(GameData game) throws DataAccessException {
        if (!gameTable.containsKey(game.gameID())) {
            gameTable.put(game.gameID(), game);
        } else {
            throw new DataAccessException("Invalid GameID; Game already exists!");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if (gameTable.containsKey(gameID)) {
            return gameTable.get(gameID);
        } else {
            throw new DataAccessException("Invalid GameID; Could not find game!");
        }
    }

    @Override
    public Collection<GameData> listGames() {
        return gameTable.values();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (gameTable.containsKey(game.gameID())) {
            gameTable.put(game.gameID(), game);
        } else {
            throw new DataAccessException("Invalid Game update; Game does not exist!");
        }
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        if (gameTable.containsKey(game.gameID())) {
            gameTable.remove(game.gameID());
        } else {
            throw new DataAccessException("Invalid Game deletion; Game does not exist!");
        }
    }

    @Override
    public void clear() {
        gameTable = new HashMap<>();
    }
}
