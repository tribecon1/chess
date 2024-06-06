package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

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



    public static void main(String[] args) throws InvalidMoveException {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        ChessGame testBoard = new ChessGame();
        testBoard.makeMove(new ChessMove(new ChessPosition("b2"), new ChessPosition("b4"), null));
        testBoard.makeMove(new ChessMove(new ChessPosition("g8"), new ChessPosition("h6"), null));

        createBoardWhiteOrientation(out, testBoard.getBoard());

        createBoardBlackOrientation(out, testBoard.getBoard());

    }

    public static void createBoardWhiteOrientation(PrintStream out, ChessBoard currBoard) {
        drawHeaders(out, ChessGame.TeamColor.WHITE);
        drawBoard(out, currBoard, ChessGame.TeamColor.WHITE);
        drawHeaders(out, ChessGame.TeamColor.WHITE);
        out.print(SET_DEFAULT_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println();
    }

    public static void createBoardBlackOrientation(PrintStream out, ChessBoard currBoard) {
        drawHeaders(out, ChessGame.TeamColor.BLACK);
        drawBoard(out, currBoard, ChessGame.TeamColor.BLACK);
        drawHeaders(out, ChessGame.TeamColor.BLACK);
        out.print(SET_DEFAULT_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println();
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
        setTerminalColor(out);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String headerText) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(headerText);
    }

    private static void drawBoard(PrintStream out, ChessBoard currBoard, ChessGame.TeamColor currTeamOrientation) {
        switch (currTeamOrientation) {
            case WHITE:
                for (int rowNum = BOARD_DIMENSION_IN_SQUARES; rowNum > 0; --rowNum) {
                    printRowNum(out, rowNum);
                    if (rowNum % 2 != 0) {
                        for (int colNum = BOARD_DIMENSION_IN_SQUARES; colNum > 0; --colNum) {
                            if (colNum % 2 != 0) {
                                setGreen(out);
                            }
                            else {
                                setDarkGreen(out);
                            }
                            pieceChecker(currBoard, out, rowNum, colNum);
                        }
                    }
                    else{
                        for (int colNum = BOARD_DIMENSION_IN_SQUARES; colNum > 0; --colNum) {
                            if (colNum % 2 != 0) {
                                setDarkGreen(out);
                            }
                            else {
                                setGreen(out);
                            }
                            pieceChecker(currBoard, out, rowNum, colNum);
                        }
                    }
                    printRowNum(out, rowNum);
                    setTerminalColor(out); //could be white for other terminal color
                    out.println();
                }
                break;
            case BLACK:
                for (int rowNum = 1; rowNum < BOARD_DIMENSION_IN_SQUARES+1; ++rowNum) {
                    printRowNum(out, rowNum);
                    if (rowNum % 2 != 0) {
                        for (int colNum = 1; colNum < BOARD_DIMENSION_IN_SQUARES+1; ++colNum) {
                            if (colNum % 2 != 0) {
                                setGreen(out);
                            }
                            else {
                                setDarkGreen(out);
                            }
                            pieceChecker(currBoard, out, rowNum, colNum);
                        }
                    }
                    else{
                        for (int colNum = 1; colNum < BOARD_DIMENSION_IN_SQUARES+1; ++colNum) {
                            if (colNum % 2 != 0) {
                                setDarkGreen(out);
                            }
                            else {
                                setGreen(out);
                            }
                            pieceChecker(currBoard, out, rowNum, colNum);
                        }
                    }
                    printRowNum(out, rowNum);
                    setTerminalColor(out); //could be white for other terminal color
                    out.println();
                }
        }
    }


    private static void setGreen(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setDarkGreen(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setLightBrown(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_BROWN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setTerminalColor(PrintStream out) {
        //out.print(SET_BG_COLOR_BLACK);
        out.print(SET_DEFAULT_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printRowNum(PrintStream out, int rowNum) {
        out.print(SET_BG_COLOR_LIGHT_BROWN);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" " + rowNum + " ");
    }

    private static void pieceChecker(ChessBoard currBoard, PrintStream out, int row, int col) {
        ChessPiece potentialPiece = currBoard.getPiece(new ChessPosition(row, Math.abs(col-9) ));
        if (potentialPiece != null){
            if (potentialPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                out.print(SET_TEXT_COLOR_WHITE);
            }
            else{
                out.print(SET_TEXT_COLOR_BLACK);
            }
            out.print(" " + ICON_CATALOG.get(potentialPiece.getPieceType()) + " ");
        }
        else{
            out.print(EMPTY);
        }
    }

}
