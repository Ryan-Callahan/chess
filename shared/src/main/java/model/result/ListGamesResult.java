package model.result;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(int statusCode, Collection<GameData> games) {
}
