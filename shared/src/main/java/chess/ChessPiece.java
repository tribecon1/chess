package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import chess.PieceMoveCalculator; //stores methods for each piece

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private PieceType type; //NOT final because piece could be a pawn, gets changed at promotion. should be public?


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type; //why the double declaration? is it not declared in the constructor?
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override //to easily represent a ChessPiece object and get its information
    public String toString() {
        return "ChessPiece(" + type + ", " + pieceColor + ")";
        //why doesn't ChessPiece not have Chess Position as stored element
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to //from that position?
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    //Collection can be any sort of data grouping
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>(); //diamond format, since datatype is implied
        PieceType[] possiblePromotion = {PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK};
        //^^above is to calculate the POTENTIAL moves of the POTENTIAL promotions of a pawn (as a separate case in 'switch')
        switch (this.type){
            case BISHOP:

        }



        return possibleMoves;
    }
}
        //use switch *if this.type is "certain piece," then calculate the appropriate moves
