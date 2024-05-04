package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

public class PieceMoveCalculator {
    public static List<ChessMove> potentialMoves = new ArrayList<>();


    public static HashSet<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet, ChessPiece currPiece){

        ChessPosition potentialUpRightPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
        ChessMove potentialUpRightMove = new ChessMove(myPosition, potentialUpRightPos,currPiece.getPieceType());
        potentialMoves.add(potentialUpRightMove);
        ChessPosition potentialUpLeftPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
        ChessMove potentialUpLeftMove = new ChessMove(myPosition, potentialUpLeftPos,currPiece.getPieceType());
        potentialMoves.add(potentialUpLeftMove);
        ChessPosition potentialDownRightPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
        ChessMove potentialDownRightMove = new ChessMove(myPosition, potentialDownRightPos,currPiece.getPieceType());
        potentialMoves.add(potentialDownRightMove);
        ChessPosition potentialDownLeftPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
        ChessMove potentialDownLeftMove = new ChessMove(myPosition, potentialDownLeftPos,currPiece.getPieceType());
        potentialMoves.add(potentialDownLeftMove);





        if ((myPosition.getRow() == 8 || myPosition.getRow() == 1) && (myPosition.getColumn() == 8 || myPosition.getColumn() == 1)){
            //maybe try > than
            return givenHashSet;
        }
        else if(board.getPiece(myPosition) != null && board.getPiece(myPosition).getTeamColor() != currPiece.getTeamColor()) {
            return givenHashSet;
        }
        else{
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
        }
        return givenHashSet;
    }
}
