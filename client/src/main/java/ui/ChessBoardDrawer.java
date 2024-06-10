package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

import static ui.EscapeSequences.*;

public class ChessBoardDrawer {
    private static final int BOARD_DIMENSION_IN_SQUARES = 8;
    private static final String EMPTY = "   ";
    private static final HashMap<ChessPiece.PieceType, String> ICON_CATALOG = new HashMap<>() {{
        put(ChessPiece.PieceType.PAWN, BLACK_PAWN);
        put(ChessPiece.PieceType.ROOK, BLACK_ROOK);
        put(ChessPiece.PieceType.KNIGHT, BLACK_KNIGHT);
        put(ChessPiece.PieceType.BISHOP, BLACK_BISHOP);
        put(ChessPiece.PieceType.QUEEN, BLACK_QUEEN);
        put(ChessPiece.PieceType.KING, BLACK_KING);
    }};


    public static void main(String[] args) throws InvalidMoveException {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        ChessGame testBoard = new ChessGame();
        testBoard.makeMove(new ChessMove(new ChessPosition("c2"), new ChessPosition("c4"), null));
        testBoard.makeMove(new ChessMove(new ChessPosition("g8"), new ChessPosition("h6"), null));
        testBoard.makeMove(new ChessMove(new ChessPosition("d1"), new ChessPosition("c2"), null));
        testBoard.makeMove(new ChessMove(new ChessPosition("c7"), new ChessPosition("c5"), null));

        createBoardWhiteOrientation(out, testBoard, new ChessPosition("c2"));

        createBoardBlackOrientation(out, testBoard, new ChessPosition("h6"));

    }

    public static void createBoardWhiteOrientation(PrintStream out, ChessGame currGame, ChessPosition legalMovesStart) {
        drawHeaders(out, ChessGame.TeamColor.WHITE);
        drawBoard(out, currGame, ChessGame.TeamColor.WHITE, legalMovesStart);
        drawHeaders(out, ChessGame.TeamColor.WHITE);
        out.print(SET_DEFAULT_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println();
    }

    public static void createBoardBlackOrientation(PrintStream out, ChessGame currBoard, ChessPosition legalMovesStart) {
        drawHeaders(out, ChessGame.TeamColor.BLACK);
        drawBoard(out, currBoard, ChessGame.TeamColor.BLACK, legalMovesStart);
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

    private static void drawBoard(PrintStream out, ChessGame currGame, ChessGame.TeamColor currTeamOrientation, ChessPosition legalMovesStart) {
        Collection<ChessMove> validMovesAtPos = currGame.validMoves(legalMovesStart);
        if (legalMovesStart != null) {
            legalMovesStart = new ChessPosition(legalMovesStart.getRow(), Math.abs(legalMovesStart.getColumn()-9));
        }
        switch (currTeamOrientation) {
            case WHITE:
                for (int rowNum = BOARD_DIMENSION_IN_SQUARES; rowNum > 0; --rowNum) {
                    printRowNum(out, rowNum);
                    if (rowNum % 2 != 0) {
                        for (int colNum = BOARD_DIMENSION_IN_SQUARES; colNum > 0; --colNum) {
                            createEvenRowSquare(out, currGame, legalMovesStart, validMovesAtPos, rowNum, colNum);
                        }
                    }
                    else{
                        for (int colNum = BOARD_DIMENSION_IN_SQUARES; colNum > 0; --colNum) {
                            createOddRowSquare(out, currGame, legalMovesStart, validMovesAtPos, rowNum, colNum);
                        }
                    }
                    printRowNum(out, rowNum);
                    setTerminalColor(out);
                    out.println();
                }
                break;
            case BLACK:
                for (int rowNum = 1; rowNum < BOARD_DIMENSION_IN_SQUARES+1; ++rowNum) {
                    printRowNum(out, rowNum);
                    if (rowNum % 2 != 0) {
                        for (int colNum = 1; colNum < BOARD_DIMENSION_IN_SQUARES+1; ++colNum) {
                            createEvenRowSquare(out, currGame, legalMovesStart, validMovesAtPos, rowNum, colNum);
                        }
                    }
                    else{
                        for (int colNum = 1; colNum < BOARD_DIMENSION_IN_SQUARES+1; ++colNum) {
                            createOddRowSquare(out, currGame, legalMovesStart, validMovesAtPos, rowNum, colNum);
                        }
                    }
                    printRowNum(out, rowNum);
                    setTerminalColor(out);
                    out.println();
                }
        }
    }

    private static void createOddRowSquare(PrintStream out, ChessGame currGame, ChessPosition legalMovesStart, Collection<ChessMove> validMovesAtPos, int rowNum, int colNum) {
        if (colNum % 2 != 0) {
            setDarkColorSquare(out, validMovesAtPos, rowNum, colNum);
        }
        else {
            setLightColorSquare(out, validMovesAtPos, rowNum, colNum);
        }
        if (legalMovesStart!= null && legalMovesStart.equals(new ChessPosition(rowNum, colNum))) {
            setBlue(out);
        }
        pieceChecker(currGame.getBoard(), out, rowNum, colNum);
    }

    private static void createEvenRowSquare(PrintStream out, ChessGame currGame, ChessPosition legalMovesStart, Collection<ChessMove> validMovesAtPos, int rowNum, int colNum) {
        if (colNum % 2 != 0) {
            setLightColorSquare(out, validMovesAtPos, rowNum, colNum);
        }
        else {
            setDarkColorSquare(out, validMovesAtPos, rowNum, colNum);
        }
        if (legalMovesStart!= null && legalMovesStart.equals(new ChessPosition(rowNum, colNum))) {
            setBlue(out);
        }
        pieceChecker(currGame.getBoard(), out, rowNum, colNum);
    }

    private static void setDarkColorSquare(PrintStream out, Collection<ChessMove> validMovesAtPos, int rowNum, int colNum) {
        setDarkGreen(out);
        if (validMovesAtPos != null) {
            for (ChessMove move : validMovesAtPos) {
                ChessPosition invertedPos = new ChessPosition(move.getEndPosition().getRow(), Math.abs(move.getEndPosition().getColumn()-9));
                if (invertedPos.equals(new ChessPosition(rowNum, colNum))) {
                    setDarkYellow(out);
                }
            }
        }
    }

    private static void setLightColorSquare(PrintStream out, Collection<ChessMove> validMovesAtPos, int rowNum, int colNum) {
        setGreen(out);
        if (validMovesAtPos != null) {
            for (ChessMove move : validMovesAtPos) {
                ChessPosition invertedPos = new ChessPosition(move.getEndPosition().getRow(), Math.abs(move.getEndPosition().getColumn()-9));
                if (invertedPos.equals(new ChessPosition(rowNum, colNum))) {
                    setLightYellow(out);
                }
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

    private static void setDarkYellow(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_YELLOW);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setLightYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setLightBrown(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_BROWN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setTerminalColor(PrintStream out) {
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
                out.print(ICON_CATALOG.get(potentialPiece.getPieceType()));
            }
            else{
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(ICON_CATALOG.get(potentialPiece.getPieceType()));
            }
        }
        else{
            out.print(EMPTY);
        }
    }

}
