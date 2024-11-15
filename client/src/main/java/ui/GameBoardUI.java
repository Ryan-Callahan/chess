package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import static ui.EscapeSequences.*;

public class GameBoardUI {
    private final ChessGame game;
    private final String gameName;
    private final String whiteUsername;
    private final String blackUsername;

    public GameBoardUI(GameData gameData) {
        this.game = gameData.game();
        this.gameName = gameData.gameName();
        this.whiteUsername = gameData.whiteUsername();
        this.blackUsername = gameData.blackUsername();
    }

    public String renderGame() {
        return RESET_TEXT_COLOR +
                drawGameBoard(game.getBoard(), true) +
                drawHorizontalLine() +
                drawGameBoard(game.getBoard(), false);
    }

    private String drawGameBoard(ChessBoard board, Boolean reversed) {
        StringBuilder gameBoard = new StringBuilder();
        gameBoard.append(drawColCoordinates(reversed));
        var rows = getGameBoardRows(board, reversed);
        for (var r : rows) {
            for (var c : r) {
                gameBoard.append(c);
            }
            gameBoard.append("\n");
        }
        gameBoard.append(drawColCoordinates(reversed));
        gameBoard.append(reset());
        return gameBoard.toString();
    }

    private List<String> getRow(int row, ChessBoard board, Boolean reversed) {
        List<String> rowList = new Vector<>();
        rowList.add(drawRowCoord(row));
        for (int col = 1; col <= 8; col++) {
            rowList.add(drawSquare(getSquareColor(row, col),
                    Optional.ofNullable(board.getPiece(new ChessPosition(row, col)))));
        }
        rowList.add(drawRowCoord(row));
        if (reversed) {
            Collections.reverse(rowList);
        }
        return rowList;
    }

    private String drawSquare(String squareColor, Optional<ChessPiece> piece) {
        return squareColor + getPiece(piece) + reset();
    }

    private String drawRowCoord(int row) {
        return SET_TEXT_BOLD + SET_BG_COLOR_LIGHT_GREY + " " + row + " " + reset();
    }

    private String drawColCoordinates(Boolean reversed) {
        StringBuilder coords = new StringBuilder();
        List<String> columns = new Vector<>();
        Collections.addAll(columns, " ", "ａ", "ｂ", "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", " ");
        if (reversed) {
            Collections.reverse(columns);
        }
        coords.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_BOLD);
        for (var col : columns) {
            coords.append(" " + col + " ");
        }
        coords.append(reset() + "\n");
        return coords.toString();
    }

    private String drawHorizontalLine() {
        return SET_BG_COLOR_BLACK + EMPTY.repeat(8) + " ".repeat(6) + reset() + "\n";
    }

    private List<List<String>> getGameBoardRows(ChessBoard board, Boolean reversed) {
        List<List<String>> rows = new Vector<>();
        for (int row = 1; row <= 8; row++) {
            rows.add(getRow(row, board, reversed));
        }
        if (!reversed) {
            Collections.reverse(rows);
        }
        return rows;
    }

    private String getSquareColor(int row, int col) {
        if (row % 2 == 0) {
            if (col % 2 == 0) {
                return SET_BG_COLOR_BLACK;
            }
            return SET_BG_COLOR_WHITE;
        } else {
            if (col % 2 == 0) {
                return SET_BG_COLOR_WHITE;
            }
            return SET_BG_COLOR_BLACK;
        }
    }

    private String getPiece(Optional<ChessPiece> piece) {
        if (piece.isPresent()) {
            var p = piece.get();
            var color = p.getTeamColor();
            var type = p.getPieceType();
            if (color == ChessGame.TeamColor.WHITE) {
                return SET_TEXT_COLOR_BLUE + getWhitePiece(type);
            } else {
                return SET_TEXT_COLOR_RED + getBlackPiece(type);
            }
        } else {
            return EMPTY;
        }
    }

    private String getWhitePiece(ChessPiece.PieceType type) {
        return getColoredPiece(type, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_PAWN);
    }

    private String getBlackPiece(ChessPiece.PieceType type) {
        return getColoredPiece(type, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_PAWN);
    }

    private String getColoredPiece(ChessPiece.PieceType type, String rook, String knight,
                                   String bishop, String queen, String king, String pawn) {
        return switch (type) {
            case ROOK -> rook;
            case KNIGHT -> knight;
            case BISHOP -> bishop;
            case QUEEN -> queen;
            case KING -> king;
            case PAWN -> pawn;
        };
    }

    private String resetText() {
        return RESET_TEXT_COLOR + RESET_TEXT_BLINKING + RESET_TEXT_ITALIC
                + RESET_TEXT_UNDERLINE + RESET_TEXT_BOLD_FAINT;
    }

    private String resetBG() {
        return RESET_BG_COLOR;
    }

    private String reset() {
        return resetText() + resetBG();
    }
}
