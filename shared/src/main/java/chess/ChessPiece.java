package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessPosition startingPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());

        if (this.pieceType == PieceType.BISHOP) {
            ChessPosition nextPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
            int row = nextPosition.getRow();
            int col = nextPosition.getColumn();

            int[] cardinality = {1, 8};

            for (int longitude:cardinality) {
                for (int latitude:cardinality) {
                    while ((row != latitude) && (col != longitude)) {
                        if (row > latitude) {
                            row = row-1;
                        } else {
                            row = row+1;
                        }

                        if (col > latitude) {
                            col = col-1;
                        } else {
                            col = col+1;
                        }

                        nextPosition = new ChessPosition(row, col);
                        ChessPiece boardPiece = board.getPiece(nextPosition);
                        if (boardPiece != null) {
                            if (boardPiece.getTeamColor() != this.pieceColor) {
                                validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                                break;
                            }
                            break;
                        } else {
                            validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                        }

                    }
                }
            }

        }

        return validMoves;
    }
}
