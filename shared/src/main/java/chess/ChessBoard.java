package chess;

import java.util.HashMap;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //chessBoard is (row, column)
    private final HashMap<Integer, HashMap<Integer, ChessPiece>> chessBoard = new HashMap<>();

    public ChessBoard() {
        for (int i = 1; i <= 8; i++) {
            chessBoard.put(i, new HashMap<>());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard.get(position.getRow()).put(position.getColumn(), piece);
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
            for (int j = 1; j <= 8; j++) {
                chessBoard.put(i, new HashMap<>());
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
    }
}
