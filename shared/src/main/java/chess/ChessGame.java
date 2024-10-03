package chess;

import java.util.Collection;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        //TODO move is illegal if the chess piece cannot move there, if the move leaves the team’s king in danger, or if it’s not the corresponding team's turn
        if (move.getPromotionPiece() != null) piece.setPieceType(move.getPromotionPiece());
        movePiece(move, piece);
    }

    /**
     * Moves a piece according to the provided move
     *
     * @param move the move to be made
     * @param piece the piece to be moved
     */
    private void movePiece(ChessMove move, ChessPiece piece) {
        board.addPiece(move.getEndPosition(), piece);
        board.removePiece(move.getStartPosition());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Set<ChessPosition> team = board.getTeamSet((teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
        for (ChessPosition position : team) {
            ChessPiece piece = board.getPiece(position);
            for (ChessMove move : piece.pieceMoves(board, position)) {
                if (board.getPiece(move.getEndPosition()) != null
                        && board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Set<ChessPosition> team  = board.getTeamSet(teamColor);
        for (ChessPosition position : team) {
            ChessPiece piece = board.getPiece(position);
            for (ChessMove move : piece.pieceMoves(board, position)) {
                if (!isInCheckAfterMove(move, teamColor)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calculates if a team would still be in check after a move
     *
     * @param move the move to be made
     * @param teamColor the team being checked
     * @return true if the team is still in check after a move
     */
    private boolean isInCheckAfterMove(ChessMove move, TeamColor teamColor) {
        ChessPiece movedPiece = board.getPiece(move.getStartPosition());
        ChessPiece removedPiece = board.getPiece(move.getEndPosition());
        ChessMove reversedMove = new ChessMove(move.getEndPosition(), move.getStartPosition(), null);
        movePiece(move, movedPiece);
        boolean inCheck = (isInCheck(teamColor));
        movePiece(reversedMove, movedPiece);
        board.addPiece(move.getEndPosition(), removedPiece);
        return inCheck;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean hasAnyMoves = false;
        if (!isInCheck(teamColor)) {
            Set<ChessPosition> team  = board.getTeamSet(teamColor);
            for (ChessPosition position : team) {
                ChessPiece piece = board.getPiece(position);
                if (!piece.pieceMoves(board, position).isEmpty()) {
                    hasAnyMoves = true;
                }
            }
            return !hasAnyMoves;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
