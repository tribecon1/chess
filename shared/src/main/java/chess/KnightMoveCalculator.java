package chess;

import java.util.HashSet;

public class KnightMoveCalculator {

    public static HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet){
        ChessMove forwardRightL = PossibleMoves.forwardRightLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove forwardLeftL = PossibleMoves.forwardLeftLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove rightUpL = PossibleMoves.rightUpLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove rightDownL = PossibleMoves.rightDownLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove backwardRightL = PossibleMoves.backwardRightLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove backwardLeftL = PossibleMoves.backwardLeftLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove leftUpL = PossibleMoves.leftUpLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove leftDownL = PossibleMoves.leftDownLMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        if(forwardRightL != null){
            givenHashSet.add(forwardRightL);
        }
        if(forwardLeftL != null){
            givenHashSet.add(forwardLeftL);
        }
        if(rightUpL != null){
            givenHashSet.add(rightUpL);
        }
        if(rightDownL != null){
            givenHashSet.add(rightDownL);
        }
        if(backwardRightL != null){
            givenHashSet.add(backwardRightL);
        }
        if(backwardLeftL != null){
            givenHashSet.add(backwardLeftL);
        }
        if(leftUpL != null){
            givenHashSet.add(leftUpL);
        }
        if(leftDownL != null){
            givenHashSet.add(leftDownL);
        }

        return givenHashSet;
    }


}
