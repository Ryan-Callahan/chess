package chess;

/**
 * A record for storing the direction a piece can move.
 *
 * @param yVector how many rows are being traversed in one move
 * @param xVector how many columns are being traversed in one move
 */
public record Direction(int yVector, int xVector) {}

