package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board = new ChessBoard();
    private TeamColor turn = TeamColor.WHITE;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
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
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        } else {
            var moves = piece.pieceMoves(board, startPosition);

            //if team is in check, or if the piece is a king, test move and see if team is unchecked. If so, add move and revert tested change
            if (isInCheck(piece.getTeamColor()) || piece.getPieceType() == ChessPiece.PieceType.KING) {
                Collection<ChessMove> uncheckMoves = new HashSet<>();
                for (var move : moves) {
                    var capturedPiece = board.getPiece(move.getEndPosition());
                    board.addPiece(move.getEndPosition(), piece);
                    board.addPiece(move.getStartPosition(), null);
                    if (!isInCheck(piece.getTeamColor())) {
                        uncheckMoves.add(move);
                    }
                    board.addPiece(move.getEndPosition(), capturedPiece);
                    board.addPiece(move.getStartPosition(), piece);
                }
                return uncheckMoves;
            } else {
                return moves;
            }

        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid Move: Out of Turn");
        }

        boolean valid = false;
        for (var m : validMoves(move.getStartPosition())) {
            if (m.equals(move)) {
                valid = true;
            }
        }
        if (valid) {
            if (move.getPromotionPiece() != null) {
                piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            }
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);

            if (piece.getTeamColor() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int r=1;r<=8;r++) {
            for (int c=1;c<=8;c++) {
                ChessPiece piece = board.getPiece(new ChessPosition(r, c));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    for (var move : piece.pieceMoves(board, new ChessPosition(r, c))) {
                        if (board.getPiece(move.getEndPosition()) != null && board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
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
        if (isInCheck(teamColor)) {
            for (int r = 1; r <= 8; r++) {
                for (int c = 1; c <= 8; c++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(r, c));
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        if (!validMoves(new ChessPosition(r, c)).isEmpty()) {
                            return false;
                        }
                    }

                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPiece piece = board.getPiece(new ChessPosition(r, c));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(new ChessPosition(r, c)).isEmpty()) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
