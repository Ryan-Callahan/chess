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
    private ChessBoard chessBoard = new ChessBoard();
    private TeamColor teamTurn;

    public ChessGame() {
        chessBoard.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
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
        ChessPiece piece = chessBoard.getPiece(startPosition);
        return (piece != null) ? piece.pieceMoves(chessBoard, startPosition) : null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        TeamColor team = piece.getTeamColor();
        if (validMoves.contains(move) && !isInCheckAfterMove(move, team) && isTeamsTurn(team)) {
            if (move.getPromotionPiece() != null) piece.setPieceType(move.getPromotionPiece());
            movePiece(move, piece);
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Moves a piece according to the provided move
     *
     * @param move the move to be made
     * @param piece the piece to be moved
     */
    private void movePiece(ChessMove move, ChessPiece piece) {
        chessBoard.addPiece(move.getEndPosition(), piece);
        chessBoard.removePiece(move.getStartPosition());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Set<ChessPosition> team = chessBoard.getTeamSet((teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
        for (ChessPosition position : team) {
            ChessPiece piece = chessBoard.getPiece(position);
            for (ChessMove move : piece.pieceMoves(chessBoard, position)) {
                if (chessBoard.getPiece(move.getEndPosition()) != null
                        && chessBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
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
        Set<ChessPosition> team  = chessBoard.getTeamSet(teamColor);
        for (ChessPosition position : team) {
            ChessPiece piece = chessBoard.getPiece(position);
            for (ChessMove move : piece.pieceMoves(chessBoard, position)) {
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
        ChessPiece movedPiece = chessBoard.getPiece(move.getStartPosition());
        ChessPiece removedPiece = chessBoard.getPiece(move.getEndPosition());
        ChessMove reversedMove = new ChessMove(move.getEndPosition(), move.getStartPosition(), null);
        movePiece(move, movedPiece);
        boolean inCheck = (isInCheck(teamColor));
        movePiece(reversedMove, movedPiece);
        chessBoard.addPiece(move.getEndPosition(), removedPiece);
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
            Set<ChessPosition> team  = chessBoard.getTeamSet(teamColor);
            for (ChessPosition position : team) {
                ChessPiece piece = chessBoard.getPiece(position);
                if (!piece.pieceMoves(chessBoard, position).isEmpty()) {
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
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    private boolean isTeamsTurn(TeamColor team) {
        return (getTeamTurn() == team);
    }
}
