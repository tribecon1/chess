package chess;

import java.util.HashSet;

public class KnightMoveCalculator {

    public static HashSet<ChessMove> KnightMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet){
        ChessMove LForwardRight = PossibleMoves.LForwardRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove LForwardLeft = PossibleMoves.LForwardLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove LRightUp = PossibleMoves.LRightUpMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove LRightDown = PossibleMoves.LRightDownMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove LBackwardRight = PossibleMoves.LBackwardRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove LBackwardLeft = PossibleMoves.LBackwardLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove LLeftUp = PossibleMoves.LLeftUpMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove LLeftDown = PossibleMoves.LLeftDownMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        if(LForwardRight != null){
            givenHashSet.add(LForwardRight);
        }
        if(LForwardLeft != null){
            givenHashSet.add(LForwardLeft);
        }
        if(LRightUp != null){
            givenHashSet.add(LRightUp);
        }
        if(LRightDown != null){
            givenHashSet.add(LRightDown);
        }
        if(LBackwardRight != null){
            givenHashSet.add(LBackwardRight);
        }
        if(LBackwardLeft != null){
            givenHashSet.add(LBackwardLeft);
        }
        if(LLeftUp != null){
            givenHashSet.add(LLeftUp);
        }
        if(LLeftDown != null){
            givenHashSet.add(LLeftDown);
        }

        return givenHashSet;
    }


}
