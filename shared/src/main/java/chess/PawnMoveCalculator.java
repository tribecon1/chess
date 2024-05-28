package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator {

    public static final ChessPiece.PieceType[] POSSIBLE_PROMOTION = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

    public static void possiblePromotionMoveAdder(Collection<ChessMove> givenHashSet, ChessPosition givenStart, ChessPosition givenEnd) {
        for (ChessPiece.PieceType promotionOption : POSSIBLE_PROMOTION){
            givenHashSet.add(new ChessMove(givenStart, givenEnd, promotionOption));
        }
    }

    private static void teamSpecificMoves(int limit, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet, ChessPosition oneSpaceForwardMove,
                                   ChessPosition twoSpaceForwardMove, ChessPosition oneSpaceForwardLeftMove, ChessPosition oneSpaceForwardRightMove) {
        int atStartRow;
        ChessGame.TeamColor pawnColor;

        if (limit == 8){
            atStartRow = 2;
            pawnColor = ChessGame.TeamColor.WHITE;
        }
        else {
            atStartRow = 7;
            pawnColor = ChessGame.TeamColor.BLACK;
        }
        if (myPosition.getRow() == limit){ //PROMOTION Edge Case, White
            QueenMoveCalculator.queenMoves(board, myPosition, givenHashSet); //if QUEEN chosen
            BishopMoveCalculator.bishopMoves(board, myPosition, givenHashSet); //if BISHOP chosen
            KnightMoveCalculator.knightMoves(board, myPosition, givenHashSet); //if KNIGHT chosen
            RookMoveCalculator.rookMoves(board, myPosition, givenHashSet); //if ROOK chosen
        }
        if (board.getPiece(oneSpaceForwardMove) == null){
            if (myPosition.getRow() == atStartRow && board.getPiece(twoSpaceForwardMove) == null){ //START Edge Case, White
                givenHashSet.add(new ChessMove(myPosition, oneSpaceForwardMove, null));
                givenHashSet.add(new ChessMove(myPosition, twoSpaceForwardMove, null));
            }
            else if (oneSpaceForwardMove.getRow() == limit){
                possiblePromotionMoveAdder(givenHashSet, myPosition, oneSpaceForwardMove);
            }
            else{
                givenHashSet.add(new ChessMove(myPosition, oneSpaceForwardMove, null));
            }
        }
        if (board.getPiece(oneSpaceForwardRightMove) != null && board.getPiece(oneSpaceForwardRightMove).getTeamColor() != pawnColor){
            if (oneSpaceForwardRightMove.getRow() == limit){
                possiblePromotionMoveAdder(givenHashSet, myPosition, oneSpaceForwardRightMove);
            }
            else {
                givenHashSet.add(new ChessMove(myPosition, oneSpaceForwardRightMove, null));
            }
        }
        if (board.getPiece(oneSpaceForwardLeftMove) != null && board.getPiece(oneSpaceForwardLeftMove).getTeamColor() != pawnColor){
            if (oneSpaceForwardLeftMove.getRow() == limit){
                possiblePromotionMoveAdder(givenHashSet, myPosition, oneSpaceForwardLeftMove);
            }
            else {
                givenHashSet.add(new ChessMove(myPosition, oneSpaceForwardLeftMove, null));
            }
        }
    }


    public static HashSet<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet, ChessGame.TeamColor pawnColor){

        if (pawnColor == ChessGame.TeamColor.WHITE){
            ChessPosition oneUp = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            ChessPosition twoUp = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
            ChessPosition upRight = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            ChessPosition upLeft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);

            teamSpecificMoves(8, board, myPosition, givenHashSet, oneUp, twoUp, upLeft, upRight);

        }
        else { //BLACK
            ChessPosition oneDown = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            ChessPosition twoDown = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
            ChessPosition downRight = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            ChessPosition downLeft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);

            teamSpecificMoves(1, board, myPosition, givenHashSet, oneDown, twoDown, downLeft, downRight);
        }

       return givenHashSet;
    }
}
