package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;

public class ChessBoardDrawer {

    private static final int BOARD_DIMENSION_IN_SQUARES = 8;
    private static final String EMPTY = "   ";
    private static final HashMap<ChessPiece.PieceType, String> ICON_CATALOG = new HashMap<>() {{
        put(ChessPiece.PieceType.PAWN, "P");
        put(ChessPiece.PieceType.ROOK, "R");
        put(ChessPiece.PieceType.KNIGHT, "N");
        put(ChessPiece.PieceType.BISHOP, "B");
        put(ChessPiece.PieceType.QUEEN, "Q");
        put(ChessPiece.PieceType.KING, "K");
    }};

    /*public static void createHeader(ChessGame.TeamColor teamColorOrient){
        switch(teamColorOrient){
            case WHITE:
                //suite
                break;
            case BLACK:
                //suite
        }
    }*/





    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        //Black-view Chess Board
        drawHeaders(out, ChessGame.TeamColor.BLACK);
        drawBoard(out, new ChessGame().getBoard());
        drawHeaders(out, ChessGame.TeamColor.BLACK);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor currTeamOrientation) {
        setLightBrown(out);
        out.print(EMPTY);
        String[] headers = switch (currTeamOrientation) {
            case WHITE -> new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
            case BLACK -> new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        };
        for (int boardCol = 0; boardCol < BOARD_DIMENSION_IN_SQUARES; ++boardCol) {
            printHeaderText(out, headers[boardCol]);
        }
        out.print(EMPTY);
        setBlack(out);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String headerText) {
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(headerText);
    }

    private static void drawBoard(PrintStream out, ChessBoard currBoard) {

        for (int rowNum = BOARD_DIMENSION_IN_SQUARES; rowNum > 0; --rowNum) {
            printRowNum(out, rowNum);
            if (rowNum % 2 != 0) {
                for (int colNum = BOARD_DIMENSION_IN_SQUARES; colNum > 0; --colNum) {
                    if (colNum % 2 != 0) {
                        setGreen(out);
                        //out.print(EMPTY);
                    }
                    else {
                        setDarkGreen(out);
                        //out.print(EMPTY);
                    }
                    pieceChecker(currBoard, out, rowNum, colNum);
                }
            }
            else{
                for (int colNum = BOARD_DIMENSION_IN_SQUARES; colNum > 0; --colNum) {
                    if (colNum % 2 != 0) {
                        setDarkGreen(out);
                        //out.print(EMPTY);
                    }
                    else {
                        setGreen(out);
                        //out.print(EMPTY);
                    }
                    pieceChecker(currBoard, out, rowNum, colNum);
                }
            }
            printRowNum(out, rowNum);
            setBlack(out); //could be white for other terminal color
            out.println();
        }
    }



    private static void setWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setGreen(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_GREEN);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setDarkGreen(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setLightBrown(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void printRowNum(PrintStream out, int rowNum) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(" " + rowNum + " ");
    }

    private static void pieceChecker(ChessBoard currBoard, PrintStream out, int row, int col) {
        ChessPiece potentialPiece = currBoard.getPiece(new ChessPosition(row, col));
        if (potentialPiece != null){
            if (potentialPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            }
            else{
                out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            }
            out.print(" " + ICON_CATALOG.get(potentialPiece.getPieceType()) + " ");
        }
        else{
            out.print(EMPTY);
        }
    }

}
