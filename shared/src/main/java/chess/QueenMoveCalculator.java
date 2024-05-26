package chess;

import java.util.HashSet;

public class QueenMoveCalculator {

    public static HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        BishopMoveCalculator.bishopMoves(board, myPosition, givenHashSet);
        RookMoveCalculator.rookMoves(board, myPosition, givenHashSet);
        return givenHashSet;

    }
}
