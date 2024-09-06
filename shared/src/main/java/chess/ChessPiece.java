package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case ROOK -> calculateMoves(board, myPosition, Directions.lateralDirections, true);
            case BISHOP -> calculateMoves(board, myPosition, Directions.diagonalDirections, true);
            case QUEEN -> calculateMoves(board, myPosition, Directions.omniDirections, true);
            case KNIGHT -> calculateMoves(board, myPosition, Directions.knightDirections, false);
            case KING -> calculateMoves(board, myPosition, Directions.omniDirections, false);
            case PAWN -> calculatePawnMoves(board, myPosition);
        };
    }

    private Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition, Direction[] directions, Boolean continuous) {
        HashSet<ChessMove> moves = new HashSet<>();
        Boolean notBlocked;
        for (Direction direction : directions) {
            int newRow = startPosition.getRow();
            int newCol = startPosition.getColumn();
            notBlocked = continuous;
            do {
                 newRow = newRow + direction.latitude();
                 newCol = newCol + direction.longitude();
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if (board.isPositionOnBoard(newPosition)) {
                    if (board.getPiece(newPosition) != null) {
                        if (board.getPiece(newPosition).pieceColor != this.pieceColor) {
                            moves.add(new ChessMove(startPosition, newPosition, null));
                        }
                        notBlocked = false;
                    } else {
                        moves.add(new ChessMove(startPosition, newPosition, null));
                    }
                } else {
                    notBlocked = false;
                }
            } while (notBlocked);
        }
        return moves;
    }

    private Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition startPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        Direction[] directions = Directions.pawnDirections;
        int vector = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        PieceType[] possiblePromotions = getPawnPromotions(startPosition);
        for (Direction direction : directions) {
            int newRow = startPosition.getRow() + vector;
            int newCol = startPosition.getColumn() + direction.longitude();
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (board.isPositionOnBoard(newPosition)) {
                if (direction.longitude() == 0) {
                    if (board.getPiece(newPosition) == null) {
                        for (PieceType promotion : possiblePromotions) {
                            moves.add(new ChessMove(startPosition, newPosition, promotion));
                        }
                        if (!pawnHasMoved(startPosition)) {
                            newPosition = new ChessPosition(newRow + vector, newCol);
                            if (board.getPiece(newPosition) == null) {
                                moves.add(new ChessMove(startPosition, newPosition, null));
                            }
                        }
                    }
                } else {
                    if (board.getPiece(newPosition) != null
                            && board.getPiece(newPosition).pieceColor != this.pieceColor) {
                        for (PieceType promotion : possiblePromotions) {
                            moves.add(new ChessMove(startPosition, newPosition, promotion));
                        }
                    }
                }
            }
        }
        return moves;
    }

    private PieceType[] getPawnPromotions(ChessPosition startPosition) {
        PieceType[] possiblePromotions;
        if ((pieceColor == ChessGame.TeamColor.WHITE && startPosition.getRow() == 7)
            || (pieceColor == ChessGame.TeamColor.BLACK && startPosition.getRow() == 2)) {
            possiblePromotions = new PieceType[]{
                PieceType.QUEEN,
                PieceType.ROOK,
                PieceType.BISHOP,
                PieceType.KNIGHT
            };
        } else {
            possiblePromotions = new PieceType[]{null};
        }
        return possiblePromotions;
    }

    private Boolean pawnHasMoved(ChessPosition position) {
        return ((pieceColor == ChessGame.TeamColor.WHITE && position.getRow() != 2)
                || (pieceColor == ChessGame.TeamColor.BLACK && position.getRow() != 7));
    }
}
