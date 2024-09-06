package chess;

import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //chessBoard is (row, column)
    private final HashMap<Integer, HashMap<Integer, ChessPiece>> chessBoard = new HashMap<>() {{
        for (int i = 1; i <= 8; i++) {
            put(i, new HashMap<>() {{
                for (int j = 1; j <= 8; j++) {
                    put(j, null);
                }
            }});
        }
    }};

    public ChessBoard() {

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
        row = chessBoard.get(8); {
            row.put(1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
            row.put(2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
            row.put(3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
            row.put(4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
            row.put(5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
            row.put(6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
            row.put(7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
            row.put(8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        }
        row = chessBoard.get(7); {
            row.put(1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            row.put(2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            row.put(3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            row.put(4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            row.put(5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            row.put(6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            row.put(7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            row.put(8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        for (int i = 3; i < 7; i++) {
            row = chessBoard.get(i);
            for (int j = 1; j <= 8; j++) {
                row.put(j, null);
            }
        }
        row = chessBoard.get(2); {
            row.put(1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            row.put(2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            row.put(3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            row.put(4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            row.put(5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            row.put(6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            row.put(7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            row.put(8, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        row = chessBoard.get(1); {
            row.put(1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
            row.put(2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            row.put(3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            row.put(4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
            row.put(5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
            row.put(6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            row.put(7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            row.put(8, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        }
    }
}
