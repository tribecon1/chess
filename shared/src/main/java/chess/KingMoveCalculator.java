package chess;

import java.util.HashSet;

public class KingMoveCalculator {

    public static HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove upRight = PossibleMoves.upRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove upLeft = PossibleMoves.upLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove downRight = PossibleMoves.downRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove downLeft = PossibleMoves.downLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove up = PossibleMoves.upMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove down = PossibleMoves.downMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove right = PossibleMoves.rightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove left = PossibleMoves.leftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        if(upRight != null){
            givenHashSet.add(upRight);
        }
        if(upLeft != null){
            givenHashSet.add(upLeft);
        }
        if(downRight != null){
            givenHashSet.add(downRight);
        }
        if(downLeft != null){
            givenHashSet.add(downLeft);
        }
        if(up != null){
            givenHashSet.add(up);
        }
        if(down != null){
            givenHashSet.add(down);
        }
        if(right != null){
            givenHashSet.add(right);
        }
        if(left != null){
            givenHashSet.add(left);
        }
        return givenHashSet;
    }
}
