package chess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //chessBoard is (row, column)
    private final HashMap<Integer, HashMap<Integer, ChessPiece>> chessBoard = new HashMap<>();
    private final Set<ChessPosition> whitePieces = new HashSet<>();
    private final Set<ChessPosition> blackPieces = new HashSet<>();

    public ChessBoard() {
        for (int i = 1; i <= 8; i++) {
            HashMap<Integer, ChessPiece> col = new HashMap<>();
            chessBoard.put(i, col);
            for (int j = 1; j <= 8; j++) {
                col.put(j, null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(chessBoard, that.chessBoard)
                && Objects.equals(whitePieces, that.whitePieces)
                && Objects.equals(blackPieces, that.blackPieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, whitePieces, blackPieces);
    }

    /**
     * Adds a chess piece to the chessboard. If the new piece is replacing a piece,
     * removes old piece from its team.
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (getPiece(position) != null) {
            removePositionFromTeam(position);
        }
        chessBoard.get(position.getRow()).put(position.getColumn(), piece);
        if (piece != null) {
            addPositionToTeam(position);
        }
    }

    /**
     * Removes a piece from the board by replacing it with null.
     *
     * @param position the position to replace with null.
     */
    public void removePiece(ChessPosition position) {
        removePositionFromTeam(position);
        addPiece(position, null);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard.get(position.getRow()).get(position.getColumn());
    }

    /**
     * Returns a copy of the requested team
     *
     * @param teamColor the color of the requested team
     * @return a Set of ChessPositions
     */
    public Set<ChessPosition> getTeamSet(ChessGame.TeamColor teamColor) {
        return Set.copyOf((teamColor == ChessGame.TeamColor.WHITE) ? whitePieces : blackPieces);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        HashMap<Integer, ChessPiece> row;
        row = chessBoard.get(1); {
            row.put(1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
            row.put(2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
            row.put(3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
            row.put(4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
            row.put(5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
            row.put(6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
            row.put(7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
            row.put(8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        }
        row = chessBoard.get(2);
        for (int i = 1; i <= 8; i++) {
            row.put(i, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        for (int i = 3; i <= 6; i++) {
            HashMap<Integer, ChessPiece> col = chessBoard.get(i);
            for (int j = 1; j <= 8; j++) {
                col.put(j, null);
            }
        }
        row = chessBoard.get(7);
        for (int i = 1; i <= 8; i++) {
            row.put(i, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        row = chessBoard.get(8); {
            row.put(1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
            row.put(2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            row.put(3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            row.put(4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
            row.put(5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
            row.put(6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            row.put(7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            row.put(8, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        }
        resetTeams();
    }

    /**
     * Removes a position from its corresponding team.
     *
     * @param position the position to remove
     */
    private void removePositionFromTeam(ChessPosition position) {
        ChessPiece piece = getPiece(position);
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            whitePieces.remove(position);
        } else {
            blackPieces.remove(position);
        }
    }

    /**
     * adds the position to its corresponding team.
     *
     * @param position the position of the piece
     */
    private void addPositionToTeam(ChessPosition position) {
        ChessPiece piece = getPiece(position);
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                whitePieces.add(position);
            } else {
                blackPieces.add(position);
            }
        }
    }

    /**
     * Resets the team hash sets to contain all the positions currently on the board.
     */
    private void resetTeams() {
        whitePieces.clear();
        blackPieces.clear();
        for (int row : chessBoard.keySet()) {
            for (int col : chessBoard.get(row).keySet()) {
                addPositionToTeam(new ChessPosition(row, col));
            }
        }
    }
}
