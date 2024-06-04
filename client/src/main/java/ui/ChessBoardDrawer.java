package ui;

import chess.ChessGame;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class ChessBoardDrawer {

    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";

    public static void createHeader(ChessGame.TeamColor teamColorOrient){
        switch(teamColorOrient){
            case WHITE:
                //suite
                break;
            case BLACK:
                //suite
        }
    }










    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);

        out.print(player);

        setLightBrown(out);
    }







    private static void setBrown(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_BROWN);
        out.print(SET_TEXT_COLOR_WHITE);
    }



}
