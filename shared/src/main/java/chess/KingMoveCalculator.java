package chess;

import java.util.HashSet;

public class KingMoveCalculator {

    public static HashSet<ChessMove> KingMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove upRight = PossibleMoves.UpRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove upLeft = PossibleMoves.UpLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove downRight = PossibleMoves.DownRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove downLeft = PossibleMoves.DownLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Up = PossibleMoves.UpMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Down = PossibleMoves.DownMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Right = PossibleMoves.RightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Left = PossibleMoves.LeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


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
        if(Up != null){
            givenHashSet.add(Up);
        }
        if(Down != null){
            givenHashSet.add(Down);
        }
        if(Right != null){
            givenHashSet.add(Right);
        }
        if(Left != null){
            givenHashSet.add(Left);
        }
        return givenHashSet;
    }
}
