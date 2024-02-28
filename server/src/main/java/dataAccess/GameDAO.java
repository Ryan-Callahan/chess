package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    int createGame(String gameName);
    void addUserToGame(String username);
    GameData getGameByID(int gameID);
    GameData getGameByName(String gameName);
    HashSet<GameData> listGames();
    void clear();

}
