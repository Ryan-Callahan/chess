package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Changes the type of the piece.
     *
     * @param newType the type the piece will be changed to.
     */
    public void setPieceType(PieceType newType) {
        type = newType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case ROOK -> PieceMoveService.calculateMoves(board, myPosition, Directions.lateralDirections, true);
            case BISHOP -> PieceMoveService.calculateMoves(board, myPosition, Directions.diagonalDirections, true);
            case QUEEN -> PieceMoveService.calculateMoves(board, myPosition, Directions.omniDirections, true);
            case KNIGHT -> PieceMoveService.calculateMoves(board, myPosition, Directions.knightDirections);
            case KING -> PieceMoveService.calculateMoves(board, myPosition, Directions.omniDirections);
            case PAWN -> PieceMoveService.calculatePawnMoves(board, myPosition, Directions.pawnDirections);
        };
    }
}
