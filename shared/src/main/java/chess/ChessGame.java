package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor thisTeamsTurn;
    private ChessBoard board;

    private ChessPosition WKingPos = new ChessPosition(1,5);
    private ChessPosition BKingPos = new ChessPosition(8,5);




    public ChessGame() {
        this.board = new ChessBoard();
        this.thisTeamsTurn = TeamColor.WHITE; //White always starts in Chess
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING){
                    if (this.board.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.WHITE){
                        this.WKingPos = new ChessPosition(i, j);
                    }
                    else{
                        this.BKingPos = new ChessPosition(i, j);
                    }
                }
            }
        }
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.thisTeamsTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.thisTeamsTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition){
        ChessPiece currPiece = board.getPiece(startPosition);
        if (currPiece == null) {
            return null;
        }
        else{
            ChessGame.TeamColor currPieceTeam = this.board.getPiece(startPosition).getTeamColor();
            Collection <ChessMove> possibleMoves = currPiece.pieceMoves(board, startPosition);
            Collection <ChessMove> validMoves = new HashSet<>();
            try {
                if (isInCheck(currPieceTeam)) { //checking if in check on CURRENT board, not cloned board
                    for (ChessMove move : possibleMoves) {
                        ChessBoard clonedBoard = this.board.clone();
                        clonedBoard.addPiece(move.getEndPosition(), currPiece);
                        clonedBoard.removePiece(move.getStartPosition());
                        if (!isInCheck(clonedBoard, currPieceTeam)) { //using overloaded isInCheck to check potential consequences of a move
                            validMoves.add(move);
                            //if the piece could make a move that takes it OUT of check, that makes it valid
                        }
                    }
                }
                else{
                    for (ChessMove move : possibleMoves) {
                        ChessBoard clonedBoard = this.board.clone();
                        clonedBoard.addPiece(move.getEndPosition(), currPiece);
                        clonedBoard.removePiece(move.getStartPosition());
                        if (isInCheck(clonedBoard, currPieceTeam)){ //using overloaded isInCheck to check potential consequences of a move
                            possibleMoves.remove(move);
                        }
                    }
                    validMoves = possibleMoves;
                }
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException("Bug in the code");
            }
            return validMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceBeingMoved = this.board.getPiece(move.getStartPosition());
        Collection<ChessMove> currValidMoves = validMoves(move.getStartPosition()); //what if currValidMoves is empty?
        if(!currValidMoves.contains(move)){ //any valid move should already be in Valid moves, and return false
            throw new InvalidMoveException();
        }
        else if(board.getPiece(move.getStartPosition()).getTeamColor() != thisTeamsTurn){ //not player's turn
            throw new InvalidMoveException();
        }
        else if(pieceBeingMoved.getPieceType() == ChessPiece.PieceType.KING){
            switch (pieceBeingMoved.getTeamColor()){
                case WHITE:
                    this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                    this.board.removePiece(move.getStartPosition());
                    WKingPos = move.getEndPosition();
                case BLACK:
                    this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                    this.board.removePiece(move.getStartPosition());
                    BKingPos = move.getEndPosition();
            }
        }
        else{
            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
            this.board.removePiece(move.getStartPosition());
            //delete ChessPiece object at start pos of ChessMove in ChessBoard array, set to null.
            //Then set reference at end pos of ChessMove in ChessBoard array
        }
    }


    /*public HashMap<ChessPiece, ChessPosition> oppTeamPieces(ChessBoard currBoard, ChessGame.TeamColor oppTeamColor) {
        HashMap<ChessPiece, ChessPosition> oppTeamPieces = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() == oppTeamColor) {
                    oppTeamPieces.put(this.board.getPiece(new ChessPosition(i, j)), new ChessPosition(i, j));
                }
            }
        }
        return oppTeamPieces;
        //an alternate idea for isInCheck
    }*/


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i, j);
                    ChessPiece opponentPiece = this.board.getPiece(opponentPosition);
                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(this.board, opponentPosition);
                    for (ChessMove potentialMove : possibleCaptureMoves) {
                        if (teamColor == TeamColor.WHITE){
                            if (potentialMove.getEndPosition() == WKingPos) {
                                return true;
                            }
                        }
                        else if (teamColor == TeamColor.BLACK){
                            if (potentialMove.getEndPosition() == BKingPos) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(ChessBoard clonedBoard, TeamColor teamColor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (clonedBoard.getPiece(new ChessPosition(i, j)) != null && clonedBoard.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i, j);
                    ChessPiece opponentPiece = clonedBoard.getPiece(opponentPosition);
                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(clonedBoard, opponentPosition);
                    for (ChessMove potentialMove : possibleCaptureMoves) {
                        if (teamColor == TeamColor.WHITE){
                            if (potentialMove.getEndPosition() == WKingPos) {
                                return true;
                            }
                        }
                        else if (teamColor == TeamColor.BLACK){
                            if (potentialMove.getEndPosition() == BKingPos) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean noValidMoves(ChessGame.TeamColor teamColor){
        Collection<ChessMove> anyValidMoves = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    ChessPosition teammatePosition = new ChessPosition(i, j);
                    if (!validMoves(teammatePosition).isEmpty()){
                        anyValidMoves = validMoves(teammatePosition);
                    }
                }
            }
        }
        return anyValidMoves.isEmpty();
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return noValidMoves(teamColor);
        }
        else{
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor) && thisTeamsTurn == teamColor){
            return noValidMoves(teamColor);
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
