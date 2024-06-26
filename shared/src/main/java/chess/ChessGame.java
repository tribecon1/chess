package chess;

import java.util.Collection;
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
    private ChessPosition wKingPos;
    private ChessPosition bKingPos;
    private boolean gameOver;

    //Castling
    private boolean wKingHasMoved;
    private boolean lWRookHasMoved;
    private boolean rWRookHasMoved;

    private boolean bKingHasMoved;
    private boolean lBRookHasMoved;
    private boolean rBRookHasMoved;

    //En Passant
    private ChessPosition blackPawnForWhiteEnPassant;
    private ChessPosition whitePawnForBlackEnPassant;
    private boolean doubleMoveJustMade;
    private ChessMove whiteEnPassantMove;
    private ChessMove blackEnPassantMove;


    private void initCastlingVariables(){
        wKingHasMoved = false;
        lWRookHasMoved = false;
        rWRookHasMoved = false;
        bKingHasMoved = false;
        lBRookHasMoved = false;
        rBRookHasMoved = false;
    }

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard(); //puts all the pieces down in their places
        this.thisTeamsTurn = TeamColor.WHITE; //White always starts in Chess
        wKingPos = new ChessPosition(1,5); //default Kings' positions in a game
        bKingPos = new ChessPosition(8,5);
        this.gameOver = false;

        //VARIABLES for Castling
        initCastlingVariables();

        //VARIABLES for En Passant
        blackPawnForWhiteEnPassant = null;
        whitePawnForBlackEnPassant = null;
        doubleMoveJustMade = false;
        whiteEnPassantMove = null; //testing
        blackEnPassantMove = null;
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

    //CASTLING METHODS
    private void castlingMoves (ChessPiece currPiece, ChessPosition startPosition, Collection<ChessMove> validMoves){
        TeamColor currPieceTeam = currPiece.getTeamColor();
        if (currPieceTeam == TeamColor.WHITE && startPosition.equals(wKingPos) && !wKingHasMoved){ //we've already checked that we aren't already in check, otherwise castling code wouldn't have run
            if (this.board.getPiece(new ChessPosition(1,1))!= null &&
                    this.board.getPiece(new ChessPosition(1,1)).getPieceType() == ChessPiece.PieceType.ROOK &&
                    !lWRookHasMoved && PossibleMoves.blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(1,1), this.board) ){
                ChessMove castlingKingLeftW = new ChessMove(wKingPos, new ChessPosition(1,3), null);
                ChessMove castlingRookRightW = new ChessMove(new ChessPosition(1,1), new ChessPosition(1,4), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingLeftW.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingLeftW.getStartPosition());
                    clonedBoard.addPiece(castlingRookRightW.getEndPosition(), clonedBoard.getPiece(new ChessPosition(1,1)));
                    clonedBoard.removePiece(castlingRookRightW.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        boolean enemyFound = PossibleMoves.wouldLandOnThisPiece(new ChessPosition(1, 4), clonedBoard, currPieceTeam);
                        if (!enemyFound){
                            validMoves.add(castlingKingLeftW);
                        }
                    }
                }
                catch (CloneNotSupportedException e){
                    throw new RuntimeException("Bug in cloning for castling");
                }
            }
            kingsFinder(this.board);
            if (this.board.getPiece(new ChessPosition(1,8))!= null &&
                    this.board.getPiece(new ChessPosition(1,8)).getPieceType() == ChessPiece.PieceType.ROOK &&
                    !rWRookHasMoved && PossibleMoves.blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(1,8), this.board)){
                ChessMove castlingKingRightW = new ChessMove(wKingPos, new ChessPosition(1,7), null);
                ChessMove castlingRookLeftW = new ChessMove(new ChessPosition(1,8), new ChessPosition(1,6), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingRightW.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingRightW.getStartPosition());
                    clonedBoard.addPiece(castlingRookLeftW.getEndPosition(), clonedBoard.getPiece(new ChessPosition(1,8)));
                    clonedBoard.removePiece(castlingRookLeftW.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        boolean enemyFound = PossibleMoves.wouldLandOnThisPiece(new ChessPosition(1, 6), clonedBoard, currPieceTeam);
                        if (!enemyFound){
                            validMoves.add(castlingKingRightW);
                        }
                    }
                }
                catch (CloneNotSupportedException e){
                    throw new RuntimeException("Bug in cloning for castling");
                }
            }
        }
        else if (currPieceTeam == TeamColor.BLACK && startPosition.equals(bKingPos) && !bKingHasMoved){
            if (this.board.getPiece(new ChessPosition(8,1))!= null &&
                    this.board.getPiece(new ChessPosition(8,1)).getPieceType() == ChessPiece.PieceType.ROOK &&
                    !lBRookHasMoved && PossibleMoves.blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(8,1), this.board)){
                ChessMove castlingKingLeftB = new ChessMove(bKingPos, new ChessPosition(8,3), null);
                ChessMove castlingRookRightB = new ChessMove(new ChessPosition(8,1), new ChessPosition(8,4), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingLeftB.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingLeftB.getStartPosition());
                    clonedBoard.addPiece(castlingRookRightB.getEndPosition(), clonedBoard.getPiece(new ChessPosition(8,1)));
                    clonedBoard.removePiece(castlingRookRightB.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        boolean enemyFound = PossibleMoves.wouldLandOnThisPiece(new ChessPosition(8, 4), clonedBoard, currPieceTeam);
                        if (!enemyFound){
                            validMoves.add(castlingKingLeftB);
                        }
                    }
                }
                catch (CloneNotSupportedException e){
                    throw new RuntimeException("Bug in cloning for castling");
                }
            }
            kingsFinder(this.board);
            if (this.board.getPiece(new ChessPosition(8,8))!= null &&
                    this.board.getPiece(new ChessPosition(8,8)).getPieceType() == ChessPiece.PieceType.ROOK &&
                    !rBRookHasMoved && PossibleMoves.blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(8,8), this.board)){
                ChessMove castlingKingRightB = new ChessMove(bKingPos, new ChessPosition(8,7), null);
                ChessMove castlingRookLeftB = new ChessMove(new ChessPosition(8,8), new ChessPosition(8,6), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingRightB.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingRightB.getStartPosition());
                    clonedBoard.addPiece(castlingRookLeftB.getEndPosition(), clonedBoard.getPiece(new ChessPosition(8,8)));
                    clonedBoard.removePiece(castlingRookLeftB.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        boolean enemyFound = PossibleMoves.wouldLandOnThisPiece(new ChessPosition(8, 6), clonedBoard, currPieceTeam); //only for tests, since they have a board with no enemy pieces
                        if (!enemyFound){ //again, only for tests, since White Team Castle has no opposing pieces(wouldn't happen in a real game)
                            validMoves.add(castlingKingRightB);
                        }
                    }
                }
                catch (CloneNotSupportedException e){
                    throw new RuntimeException("Bug in cloning for castling");
                }
            }
        }
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
            Collection <ChessMove> possibleMovesTEMPLATE = currPiece.pieceMoves(board, startPosition);
            Collection <ChessMove> validMoves = new HashSet<>();

            try {
                if (isInCheck(currPieceTeam)) { //checking if in check on CURRENT board, not cloned board
                    for (ChessMove move : possibleMoves) {
                        ChessBoard clonedBoard = this.board.clone();
                        clonedBoard.addPiece(move.getEndPosition(), currPiece);
                        clonedBoard.removePiece(move.getStartPosition());
                        if (!isInCheck(clonedBoard, currPieceTeam)) { //using overloaded isInCheck to check potential consequences of a move
                            validMoves.add(move);
                        }
                    }
                }
                else{
                    for (ChessMove move : possibleMoves) {
                        ChessBoard clonedBoard = this.board.clone();
                        clonedBoard.addPiece(move.getEndPosition(), currPiece);
                        clonedBoard.removePiece(move.getStartPosition());
                        if (isInCheck(clonedBoard, currPieceTeam)){ //using overloaded isInCheck to check potential consequences of a move
                            possibleMovesTEMPLATE.remove(move);
                        }
                    }
                    validMoves = possibleMovesTEMPLATE;
                    kingsFinder(this.board);//to set it back to original board
                    //Castling code
                    if (currPiece.getPieceType() == ChessPiece.PieceType.KING){
                        castlingMoves(currPiece, startPosition, validMoves);
                    }
                    //En Passant code
                    if (currPiece.getPieceType() == ChessPiece.PieceType.PAWN){
                        if (doubleMoveJustMade && currPieceTeam == TeamColor.BLACK && whitePawnForBlackEnPassant != null){
                            if (startPosition.getColumn()-1 == whitePawnForBlackEnPassant.getColumn()){ //to the immediate left, black going down
                                ChessMove enPassantLeftBlack = new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1), null);
                                validMoves.add(enPassantLeftBlack);
                                blackEnPassantMove = enPassantLeftBlack;
                            }
                            else if (startPosition.getColumn()+1 == whitePawnForBlackEnPassant.getColumn()){ //to the immediate right, black going down
                                ChessMove enPassantRightBlack = new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1), null);
                                validMoves.add(enPassantRightBlack);
                                blackEnPassantMove = enPassantRightBlack;
                            }
                        }
                        else if (doubleMoveJustMade && currPieceTeam == TeamColor.WHITE && blackPawnForWhiteEnPassant != null){
                            if (startPosition.getColumn()-1 == blackPawnForWhiteEnPassant.getColumn()){ //to the immediate left, white going up
                                ChessMove enPassantLeftWhite = new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1), null);
                                validMoves.add(enPassantLeftWhite);
                                whiteEnPassantMove = enPassantLeftWhite;
                            }
                            else if (startPosition.getColumn()+1 == blackPawnForWhiteEnPassant.getColumn()){ //to the immediate right, white going up
                                ChessMove enPassantRightWhite = new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1), null);
                                validMoves.add(enPassantRightWhite);
                                whiteEnPassantMove = enPassantRightWhite;
                            }
                        }
                    }

                }
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException("Bug in the code");
            }
            kingsFinder(this.board); //after castling checks
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
        Collection<ChessMove> currValidMoves = validMoves(move.getStartPosition());
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException("Error: there is no piece at that position to move");
        }
        else if(this.isGameOver()){
            throw new InvalidMoveException("Error: game over is already over!");
        }
        else if(currValidMoves.isEmpty()){
            throw new InvalidMoveException("Error: there are no legal moves possible with this piece");
        }
        else if(!currValidMoves.contains(move)){
            throw new InvalidMoveException("Error: Move is not valid, try using the \"Highlight\" command to see what your legal moves are");
        }
        else if(board.getPiece(move.getStartPosition()).getTeamColor() != thisTeamsTurn){
            throw new InvalidMoveException("Error: it is not your team's turn to move");
        }
        else if (board.getPiece(move.getStartPosition()).getPieceType().equals(ChessPiece.PieceType.PAWN) && (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1) && move.getPromotionPiece() == null){
            throw new InvalidMoveException("Error: you must choose a promotion type for your pawn to move it to this position");
        }
        else { //all test cases passed, a move now will be made
            if(pieceBeingMoved.getPieceType() == ChessPiece.PieceType.KING){
                switch (pieceBeingMoved.getTeamColor()){
                    case WHITE:
                        if (move.getEndPosition().getColumn()+1 < move.getStartPosition().getColumn()){
                            this.board.addPiece(new ChessPosition(1, 4), this.board.getPiece(new ChessPosition(1, 1)));//moving ROOK
                            this.board.removePiece(new ChessPosition(1, 1));
                            lWRookHasMoved = true;
                        }
                        else if (move.getEndPosition().getColumn()-1 > move.getStartPosition().getColumn()){
                            this.board.addPiece(new ChessPosition(1, 6), this.board.getPiece(new ChessPosition(1, 8)));//moving ROOK
                            this.board.removePiece(new ChessPosition(1, 8));
                            rWRookHasMoved = true;
                        }
                        wKingPos = move.getEndPosition();
                        wKingHasMoved = true;
                        break;
                    case BLACK:
                        if (move.getEndPosition().getColumn()+1 < move.getStartPosition().getColumn()){
                            this.board.addPiece(new ChessPosition(8, 4), this.board.getPiece(new ChessPosition(8, 1)));//moving ROOK
                            this.board.removePiece(new ChessPosition(8, 1));
                            lBRookHasMoved = true;
                        }
                        else if (move.getEndPosition().getColumn()-1 > move.getStartPosition().getColumn()){
                            this.board.addPiece(new ChessPosition(8, 6), this.board.getPiece(new ChessPosition(8, 8)));//moving ROOK
                            this.board.removePiece(new ChessPosition(8, 8));
                            rBRookHasMoved = true;
                        }
                        bKingPos = move.getEndPosition();
                        bKingHasMoved = true;
                }
            }
            else if (pieceBeingMoved.getPieceType() == ChessPiece.PieceType.PAWN){
                if (pieceBeingMoved.getTeamColor()==TeamColor.BLACK && move.equals(blackEnPassantMove)){
                    this.board.removePiece(whitePawnForBlackEnPassant);
                    whitePawnForBlackEnPassant = null;
                }
                else if (pieceBeingMoved.getTeamColor()==TeamColor.WHITE && move.equals(whiteEnPassantMove)){
                    this.board.removePiece(blackPawnForWhiteEnPassant);
                    blackPawnForWhiteEnPassant = null;
                }
                doubleMoveJustMade = false;

                if (move.getPromotionPiece() != null){
                    pieceBeingMoved = new ChessPiece(pieceBeingMoved.getTeamColor(), move.getPromotionPiece());
                }
                if (Math.abs(move.getEndPosition().getRow() - move.getStartPosition().getRow()) == 2){
                    doubleMoveJustMade = true;
                    switch (pieceBeingMoved.getTeamColor()) {
                        case WHITE -> whitePawnForBlackEnPassant = move.getEndPosition(); //White Pawn just made a 2-space first move instead of 1 = chance for black to perform en passant
                        case BLACK -> blackPawnForWhiteEnPassant = move.getEndPosition(); //Black Pawn just made a 2-space first move instead of 1 = chance for white to perform en passant
                    }
                }
                else {
                    whitePawnForBlackEnPassant = null;
                    whiteEnPassantMove = null;
                    blackPawnForWhiteEnPassant = null;
                    blackEnPassantMove = null;
                }
            }
            else if (pieceBeingMoved.getPieceType() == ChessPiece.PieceType.ROOK){
                switch (pieceBeingMoved.getTeamColor()){
                    case WHITE:
                        if (move.getStartPosition().equals(new ChessPosition(1,1)) && !lWRookHasMoved){
                            lWRookHasMoved = true;
                        }
                        else if (move.getStartPosition().equals(new ChessPosition(1,8)) && !rWRookHasMoved){
                            rWRookHasMoved = true;
                        }
                        break;
                    case BLACK:
                        if (move.getStartPosition().equals(new ChessPosition(8,1)) && !lBRookHasMoved){
                            lBRookHasMoved = true;
                        }
                        else if (move.getStartPosition().equals(new ChessPosition(8,8)) && !rBRookHasMoved){
                            rBRookHasMoved = true;
                        }
                }
            }
            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
            this.board.removePiece(move.getStartPosition());
            if (this.thisTeamsTurn == TeamColor.WHITE){
                this.thisTeamsTurn = TeamColor.BLACK;
            }
            else{
                this.thisTeamsTurn = TeamColor.WHITE;
            }
        }
    }

    private void kingsFinder(ChessBoard clonedBoard){
        wKingPos = null; //for auto-grader test scenarios
        bKingPos = null;
        for (int i = 1; i < 9; i++) { //Treat as row/column #s, not index values/one less
            for (int j = 1; j < 9; j++) {
                if (clonedBoard.getPiece(new ChessPosition(i, j)) != null &&
                        clonedBoard.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING){
                    if (clonedBoard.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.WHITE){
                        this.wKingPos = new ChessPosition(i, j);
                    }
                    else{
                        this.bKingPos = new ChessPosition(i, j);
                    }
                }
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //establishing Kings' locations
        kingsFinder(this.board);
        if (teamColor == TeamColor.WHITE && PossibleMoves.wouldLandOnThisPiece(wKingPos, this.board, TeamColor.WHITE)){
            return true;
        }
        else return teamColor == TeamColor.BLACK && PossibleMoves.wouldLandOnThisPiece(bKingPos, this.board, TeamColor.BLACK);
    }

    public boolean isInCheck(ChessBoard clonedBoard, TeamColor teamColor) { //OVERLOADED isInCheck FUNCTION
        kingsFinder(clonedBoard);
        if (teamColor == TeamColor.WHITE && PossibleMoves.wouldLandOnThisPiece(wKingPos, clonedBoard, TeamColor.WHITE)){
            return true;
        }
        else return teamColor == TeamColor.BLACK && PossibleMoves.wouldLandOnThisPiece(bKingPos, clonedBoard, TeamColor.BLACK);
    }

    private boolean noValidMoves(ChessGame.TeamColor teamColor){
        Collection<ChessMove> anyValidMoves = new HashSet<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    ChessPosition teammatePosition = new ChessPosition(i, j);
                    Collection<ChessMove> movesPerPiece = validMoves(teammatePosition);
                    if (!movesPerPiece.isEmpty()){
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
            if (noValidMoves(teamColor)){
                setGameOver(true);
                return true;
            }
        }
        return false;
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
        kingsFinder(this.board);
        initCastlingVariables(); //EXTRA CREDIT VARIABLES for Castling being reset for testing purposes
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}