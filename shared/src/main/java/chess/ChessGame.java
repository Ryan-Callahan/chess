package chess;

import java.util.Collection;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, teamTurn);
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
        if (piece != null) {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(chessBoard, startPosition);
            possibleMoves.removeIf(move -> isInCheckAfterMove(move, piece.getTeamColor()));
            return possibleMoves;
        }
        return null;
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
        if (validMoves != null && validMoves.contains(move) && !isInCheckAfterMove(move, piece.getTeamColor()) && isTeamsTurn(piece.getTeamColor())) {
            if (move.getPromotionPiece() != null) {
                piece.setPieceType(move.getPromotionPiece());
            }
            movePiece(move, piece);
            setNextTeamTurn(piece.getTeamColor());
        } else {
            throw new InvalidMoveException();
        }
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
            Collection<ChessMove> moves = validMoves(position);
            if (moves != null && !moves.isEmpty()) {
                return false;
            }
        }
        return true;
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
                Collection<ChessMove> moves = validMoves(position);
                if (moves != null && !moves.isEmpty()) {
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
     * Calculates if it's the given team's turn
     *
     * @param team team to check
     * @return if it is the team's turn
     */
    private boolean isTeamsTurn(TeamColor team) {
        return (getTeamTurn() == team);
    }

    /**
     * Sets the turn to the next team
     *
     * @param currentTeam the current team
     */
    private void setNextTeamTurn(TeamColor currentTeam) {
        setTeamTurn((currentTeam == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
    }
}
