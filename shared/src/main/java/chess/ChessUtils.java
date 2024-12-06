package chess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

/**
 * A class that holds all possible directions any piece can move in one place.
 * All methods are static, allowing them to be referenced anywhere while avoiding
 * having copies of the same lists in every piece object
 */
public interface ChessUtils {
    Direction[] lateralDirections = {
        new Direction(1, 0),
        new Direction(0, 1),
        new Direction(-1, 0),
        new Direction(0, -1)
    };

    Direction[] diagonalDirections = {
        new Direction(1, -1),
        new Direction(1, 1),
        new Direction(-1, 1),
        new Direction(-1, -1)
    };

    Direction[] omniDirections = {
        lateralDirections[0],
        lateralDirections[1],
        lateralDirections[2],
        lateralDirections[3],
        diagonalDirections[0],
        diagonalDirections[1],
        diagonalDirections[2],
        diagonalDirections[3],
    };

    Direction[] knightDirections = {
        new Direction(1, -2),
        new Direction(2, -1),
        new Direction(2, 1),
        new Direction(1, 2),
        new Direction(-1, 2),
        new Direction(-2, 1),
        new Direction(-2, -1),
        new Direction(-1, -2),
    };

    Direction[] pawnDirections = {
        new Direction(1, -1),
        new Direction(1, 0),
        new Direction(1, 1)
    };

    String[] alphaCoords = {
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H"
    };

    Map<String, Integer> alphaToInt = Map.ofEntries(
            entry("A", 1),
            entry("B", 2),
            entry("C", 3),
            entry("D", 4),
            entry("E", 5),
            entry("F", 6),
            entry("G", 7),
            entry("H", 8)
    );
}
