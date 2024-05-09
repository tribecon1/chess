package chess;

import java.util.HashSet;

public class PawnMoveCalculator {

    public static HashSet<ChessMove> PawnMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet, ChessGame.TeamColor pawnColor){

        ChessPiece.PieceType[] possiblePromotion = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        //^^above is to calculate the POTENTIAL moves of the POTENTIAL promotions of a pawn (as a separate case in 'switch')

        if (pawnColor == ChessGame.TeamColor.WHITE){
            ChessPosition oneUp = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            ChessPosition twoUp = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
            ChessPosition upRight = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            ChessPosition upLeft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);


            if (myPosition.getRow() == 8){ //PROMOTION Edge Case, White
                //Or better to do a switch based on the promotionPiece type? But how does that function work?
                QueenMoveCalculator.QueenMoves(board, myPosition, givenHashSet); //if QUEEN chosen
                BishopMoveCalculator.BishopMoves(board, myPosition, givenHashSet); //if BISHOP chosen
                KnightMoveCalculator.KnightMoves(board, myPosition, givenHashSet); //if KNIGHT chosen
                RookMoveCalculator.RookMoves(board, myPosition, givenHashSet); //if ROOK chosen
            }
            if (board.getPiece(oneUp) == null){
                if (myPosition.getRow() == 2 && board.getPiece(twoUp) == null){ //START Edge Case, White
                    givenHashSet.add(new ChessMove(myPosition, twoUp, null));
                    givenHashSet.add(new ChessMove(myPosition, oneUp, null));
                }
                else if (oneUp.getRow() == 8){
                    for (ChessPiece.PieceType promotionOption : possiblePromotion){
                        givenHashSet.add(new ChessMove(myPosition, oneUp, promotionOption));
                    }
                }
                else{
                    givenHashSet.add(new ChessMove(myPosition, oneUp, null));
                }
            }
            if (board.getPiece(upRight) != null && board.getPiece(upRight).getTeamColor() != pawnColor){
                if (upRight.getRow() == 8){
                    for (ChessPiece.PieceType promotionOption : possiblePromotion){
                        givenHashSet.add(new ChessMove(myPosition, upRight, promotionOption));
                    }
                }
                else {
                    givenHashSet.add(new ChessMove(myPosition, upRight, null));
                }
            }
            if (board.getPiece(upLeft) != null && board.getPiece(upLeft).getTeamColor() != pawnColor){
                if (upLeft.getRow() == 8){
                    for (ChessPiece.PieceType promotionOption : possiblePromotion){
                        givenHashSet.add(new ChessMove(myPosition, upLeft, promotionOption));
                    }
                }
                else {
                    givenHashSet.add(new ChessMove(myPosition, upLeft, null));
                }
            }
        }
        else { //BLACK
            //Black Pawn Positions
            ChessPosition oneDown = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            ChessPosition twoDown = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
            ChessPosition downRight = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            ChessPosition downLeft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);

            if (myPosition.getRow() == 1){ //PROMOTION Edge Case, Black
                QueenMoveCalculator.QueenMoves(board, myPosition, givenHashSet); //if QUEEN chosen
                BishopMoveCalculator.BishopMoves(board, myPosition, givenHashSet); //if BISHOP chosen
                KnightMoveCalculator.KnightMoves(board, myPosition, givenHashSet); //if KNIGHT chosen
                RookMoveCalculator.RookMoves(board, myPosition, givenHashSet); //if ROOK chosen
            }
            if (board.getPiece(oneDown) == null){
                if(myPosition.getRow() == 7 && board.getPiece(twoDown) == null){
                    givenHashSet.add(new ChessMove(myPosition, twoDown, null));
                    givenHashSet.add(new ChessMove(myPosition, oneDown, null));
                }
                else if (oneDown.getRow() == 1){
                    for (ChessPiece.PieceType promotionOption : possiblePromotion){
                        givenHashSet.add(new ChessMove(myPosition, oneDown, promotionOption));
                    }
                }
                else{
                    givenHashSet.add(new ChessMove(myPosition, oneDown, null));
                }
            }
            if (board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() != pawnColor){
                if (downRight.getRow() == 1){
                    for (ChessPiece.PieceType promotionOption : possiblePromotion){
                        givenHashSet.add(new ChessMove(myPosition, downRight, promotionOption));
                    }
                }
                else {
                    givenHashSet.add(new ChessMove(myPosition, downRight, null));
                }
            }
            if (board.getPiece(downLeft) != null && board.getPiece(downLeft).getTeamColor() != pawnColor){
                if (downLeft.getRow() == 1){
                    for (ChessPiece.PieceType promotionOption : possiblePromotion){
                        givenHashSet.add(new ChessMove(myPosition, downLeft, promotionOption));
                    }
                }
                else {
                    givenHashSet.add(new ChessMove(myPosition, downLeft, null));
                }
            }
        }
       return givenHashSet;
    }
}
