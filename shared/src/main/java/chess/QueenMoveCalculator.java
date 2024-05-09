package chess;

import java.util.HashSet;

public class QueenMoveCalculator {

    public static HashSet<ChessMove> QueenMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        BishopMoveCalculator.BishopMoves(board, myPosition, givenHashSet);
        RookMoveCalculator.RookMoves(board, myPosition, givenHashSet);
        return givenHashSet;

    }
}
