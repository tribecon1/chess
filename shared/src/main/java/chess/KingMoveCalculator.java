package chess;

import java.util.HashSet;

public class KingMoveCalculator {

    public static HashSet<ChessMove> KingMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove UpRight = PossibleMoves.UpRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove UpLeft = PossibleMoves.UpLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove DownRight = PossibleMoves.DownRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove DownLeft = PossibleMoves.DownLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Up = PossibleMoves.UpMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Down = PossibleMoves.DownMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Right = PossibleMoves.RightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Left = PossibleMoves.LeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        if(UpRight != null){
            givenHashSet.add(UpRight);
        }
        if(UpLeft != null){
            givenHashSet.add(UpLeft);
        }
        if(DownRight != null){
            givenHashSet.add(DownRight);
        }
        if(DownLeft != null){
            givenHashSet.add(DownLeft);
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
