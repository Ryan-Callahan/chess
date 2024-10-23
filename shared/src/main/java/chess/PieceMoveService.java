package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * A service for calculating the all possible moves of a piece
 */
public class PieceMoveService {
    private static final ChessPiece.PieceType[] PROMOTION_PIECE_TYPES = {
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.ROOK
    };
    public PieceMoveService() {}

    /**
     * A method for calculating the moves a standard piece can make.
     *
     * @param board the chess board the piece is on
     * @param startPosition the position of the piece
     * @param directions the directions the piece can move
     * @param continuous true if the piece can move more than once, false otherwise
     * @return a collection of all possible ChessMove objects
     */
    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition, Direction[] directions, Boolean continuous) {
        HashSet<ChessMove> chessMoves = new HashSet<>();
        ChessPiece currentPiece = board.getPiece(startPosition);
        for (Direction direction : directions) {
            Boolean continuing = continuous;
            int currentRow = startPosition.getRow();
            int currentCol = startPosition.getColumn();
            do {
                currentRow += direction.yVector();
                currentCol += direction.xVector();
                ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);
                continuing = calcluateNextStep(board, currentPiece, startPosition, nextPosition, chessMoves, continuing);
            } while (continuing);
        }
        return chessMoves;
    }

    /**
     * A helper method for calculateMoves that calculates the next move a piece can make.
     * Adds valid moves to chessMoves.
     *
     * @param board the ChessBoard
     * @param currentPiece the current ChessPiece
     * @param startPosition the current position of the currentPiece
     * @param nextPosition the position to calculate
     * @param chessMoves the set of ChessMove
     * @param continuing whether the piece can move continuously or not
     * @return A boolean representing if the piece should keep continuing or not.
     */
    private static Boolean calcluateNextStep(ChessBoard board, ChessPiece currentPiece, ChessPosition startPosition, ChessPosition nextPosition,
                                             HashSet<ChessMove> chessMoves, Boolean continuing) {
        if (isOnBoard(nextPosition)) {
            if (board.getPiece(nextPosition) != null) {
                ChessPiece nextPiece = board.getPiece(nextPosition);
                continuing = false;
                if (currentPiece.getTeamColor() != nextPiece.getTeamColor()) {
                    chessMoves.add(new ChessMove(startPosition, nextPosition, null));
                }
            } else {
                chessMoves.add(new ChessMove(startPosition, nextPosition, null));
            }
        } else {
            continuing = false;
        }
        return continuing;
    }

    /**
     * An alternate signature for calculateMoves with the parameter "continuous" set to false
     *
     * @param board the chess board the piece is on
     * @param startPosition the position of the piece
     * @param directions the directions the piece can move
     * @return a collection of all possible ChessMove objects
     */
    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition, Direction[] directions) {
        return calculateMoves(board, startPosition, directions, false);
    }

    /**
     * A method for calculating all possible moves a pawn can make.
     *
     * @param board the chess board the pawn is on
     * @param startPosition the position of the pawn
     * @param directions the directions the pawn can move
     * @return a collection of all possible ChessMove objects
     */
    public static Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition startPosition, Direction[] directions) {
        HashSet<ChessMove> chessMoves = new HashSet<>();
        ChessPiece currentPiece = board.getPiece(startPosition);
        int polarity = (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        for (Direction direction : directions) {
            ChessPosition nextPosition = new ChessPosition(startPosition.getRow() + direction.yVector()*polarity,
                    startPosition.getColumn() + direction.xVector());
            if (isOnBoard(nextPosition)) {
                if (direction.xVector() != 0) {
                    calculatePawnDiagonalMove(board, startPosition, nextPosition, currentPiece, chessMoves);
                } else {
                    calculatePawnForwardMove(board, startPosition, nextPosition, direction, currentPiece, chessMoves);
                }
            }
        }
        return chessMoves;
    }

    /**
     * A helper method for calculatePawnMoves that calculates the forward move for a pawn.
     * Adds valid moves to chessMoves.
     *
     * @param board the Chess Board
     * @param startPosition the starting position of the piece
     * @param nextPosition the position to calculate
     * @param direction the direction the pawn is moving
     * @param currentPiece the current piece
     * @param chessMoves the list of ChessMoves
     */
    private static void calculatePawnForwardMove(ChessBoard board, ChessPosition startPosition, ChessPosition nextPosition, Direction direction,
                                                 ChessPiece currentPiece, HashSet<ChessMove> chessMoves) {
        int polarity = (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        if (board.getPiece(nextPosition) == null) {
            if (!pawnHasMoved(startPosition, currentPiece.getTeamColor())) {
                ChessPosition doubleMove = new ChessPosition(nextPosition.getRow() + direction.yVector()*polarity,
                        startPosition.getColumn());
                if (board.getPiece(doubleMove) == null) {
                    chessMoves.add(new ChessMove(startPosition, doubleMove, null));
                }
            }
            addPawnMove(startPosition, nextPosition, chessMoves);
        }
    }

    /**
     * A helper method for calculatePawnMoves that calculates the diagonal moves for a pawn.
     * Adds valid moves to chessMoves.
     *
     * @param board the Chess Board
     * @param startPosition the starting position of the piece
     * @param nextPosition the position to calculate
     * @param currentPiece the current piece
     * @param chessMoves the list of ChessMoves
     */
    private static void calculatePawnDiagonalMove(ChessBoard board, ChessPosition startPosition, ChessPosition nextPosition, ChessPiece currentPiece,
                                                  HashSet<ChessMove> chessMoves) {
        if (board.getPiece(nextPosition) != null
                && board.getPiece(nextPosition).getTeamColor() != currentPiece.getTeamColor()) {
            addPawnMove(startPosition, nextPosition, chessMoves);
        }
    }

    /**
     * A private helper method for calculatePawnMoves. Calculates if the pawn should be promoted or not.
     *
     * @param startPosition the starting position of the pawn
     * @param nextPosition the potential position of the pawn
     * @param chessMoves the collection of moves the method adds to
     */
    private static void addPawnMove(ChessPosition startPosition, ChessPosition nextPosition, Collection<ChessMove> chessMoves) {
        if (nextPosition.getRow() == 8 || nextPosition.getRow() == 1) {
            for (ChessPiece.PieceType type : PROMOTION_PIECE_TYPES) {
                chessMoves.add(new ChessMove(startPosition, nextPosition, type));
            }
        } else {
            chessMoves.add(new ChessMove(startPosition, nextPosition, null));
        }
    }

    /**
     * Calculates if a pawn has moved or not.
     *
     * @param startPosition the starting position of the pawn
     * @param color the color of the pawn
     * @return true if the pawn has moved, false if not
     */
    private static Boolean pawnHasMoved(ChessPosition startPosition, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return startPosition.getRow() != 2;
        } else {
            return startPosition.getRow() != 7;
        }
    }

    /**
     * Calculates if a piece is on the board or not.
     *
     * @param position the position of the piece
     * @return true if the piece is on the board, false if not
     */
    private static Boolean isOnBoard(ChessPosition position) {
        return (position.getRow() >= 1 && position.getRow() <= 8
                && position.getColumn() >= 1 && position.getColumn() <= 8);
    }
}
