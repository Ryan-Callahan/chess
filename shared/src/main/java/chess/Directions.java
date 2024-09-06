package chess;

public class Directions {
    public static Direction[] lateralDirections = {
        new Direction(1, 0),
        new Direction(0, 1),
        new Direction(-1, 0),
        new Direction(0, -1)
    };

    public static Direction[] diagonalDirections = {
        new Direction(1, -1),
        new Direction(1, 1),
        new Direction(-1, 1),
        new Direction(-1, -1)
    };

    public static Direction[] omniDirections = {
        lateralDirections[0],
        lateralDirections[1],
        lateralDirections[2],
        lateralDirections[3],
        diagonalDirections[0],
        diagonalDirections[1],
        diagonalDirections[2],
        diagonalDirections[3],
    };

    public static Direction[] knightDirections = {
        new Direction(1, -2),
        new Direction(2, -1),
        new Direction(2, 1),
        new Direction(1, 2),
        new Direction(-1, 2),
        new Direction(-2, 1),
        new Direction(-2, -1),
        new Direction(-1, -2),
    };

    public static Direction[] pawnDirections = {
        new Direction(1, -1),
        new Direction(1, 0),
        new Direction(1, 1)
    };
}
