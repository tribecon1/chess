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

    private ChessPosition WKingPos;
    private ChessPosition BKingPos;

    //EXTRA CREDIT VARIABLES
    //Castling
    private boolean WKingHasMoved;
    private boolean LWRookHasMoved;
    private boolean RWRookHasMoved;

    private boolean BKingHasMoved;
    private boolean LBRookHasMoved;
    private boolean RBRookHasMoved;



    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard(); //puts all the pieces down in their places
        this.thisTeamsTurn = TeamColor.WHITE; //White always starts in Chess
        WKingPos = new ChessPosition(1,5); //default Kings' positions in a game
        BKingPos = new ChessPosition(8,5);

        //EXTRA CREDIT VARIABLES for Castling
        WKingHasMoved = false;
        LWRookHasMoved = false;
        RWRookHasMoved = false;

        BKingHasMoved = false;
        LBRookHasMoved = false;
        RBRookHasMoved = false;
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
    private boolean blankSpaceCastlingChecker (TeamColor team, ChessPosition rookPos) {
        if (team == TeamColor.WHITE) {
            if (rookPos.getColumn() == 1){
                for (int j = 2; j < 5; j++){
                    if (this.board.getPiece(new ChessPosition(1,j)) != null){
                        return false;
                    }
                }
                return true;
            }
            else if (rookPos.getColumn() == 8){
                for (int j = 6; j < 8; j++){
                    if (this.board.getPiece(new ChessPosition(1,j)) != null){
                        return false;
                    }
                }
                return true;
            }
        }
        else{
            if (rookPos.getColumn() == 1){
                for (int j = 2; j < 5; j++){
                    if (this.board.getPiece(new ChessPosition(8,j)) != null){
                        return false;
                    }
                }
                return true;
            }
            else if (rookPos.getColumn() == 8){
                for (int j = 6; j < 8; j++){
                    if (this.board.getPiece(new ChessPosition(8,j)) != null){
                        return false;
                    }
                }
                return true;
            }
        }
        return false; //needed?
    }


    private void castlingMoves (ChessPiece currPiece, ChessPosition startPosition, Collection<ChessMove> validMoves){
        TeamColor currPieceTeam = currPiece.getTeamColor();
        //Castling move checks
        if (currPieceTeam == TeamColor.WHITE && startPosition.equals(WKingPos) && !WKingHasMoved && !isInCheck(currPieceTeam)){
            if (this.board.getPiece(new ChessPosition(1,1)).getPieceType() == ChessPiece.PieceType.ROOK && !LWRookHasMoved && blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(1,1))){
                ChessMove castlingKingLeftW = new ChessMove(WKingPos, new ChessPosition(1,3), null);
                ChessMove castlingRookRightW = new ChessMove(new ChessPosition(1,1), new ChessPosition(1,4), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingLeftW.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingLeftW.getStartPosition());
                    clonedBoard.addPiece(castlingRookRightW.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingRookRightW.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        for (int i = 1; i < 9; i++) {
                            for (int j = 1; j < 9; j++) {
                                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() != currPieceTeam) {
                                    ChessPosition opponentPosition = new ChessPosition(i, j);
                                    ChessPiece opponentPiece = this.board.getPiece(opponentPosition);
                                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(this.board, opponentPosition);
                                    if (!wouldLandOnPiece(possibleCaptureMoves, new ChessPosition(1,4))){
                                        validMoves.add(castlingKingLeftW);
                                    }
                                }
                            }
                        }
                    }
                }
                catch (CloneNotSupportedException e){
                    throw new RuntimeException("Bug in cloning for castling");
                }
            }
            if (this.board.getPiece(new ChessPosition(1,8)).getPieceType() == ChessPiece.PieceType.ROOK && !RWRookHasMoved && blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(1,8))){
                ChessMove castlingKingRightW = new ChessMove(WKingPos, new ChessPosition(1,7), null);
                ChessMove castlingRookLeftW = new ChessMove(new ChessPosition(1,8), new ChessPosition(1,6), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingRightW.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingRightW.getStartPosition());
                    clonedBoard.addPiece(castlingRookLeftW.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingRookLeftW.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        for (int i = 1; i < 9; i++) {
                            for (int j = 1; j < 9; j++) {
                                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() != currPieceTeam) {
                                    ChessPosition opponentPosition = new ChessPosition(i, j);
                                    ChessPiece opponentPiece = this.board.getPiece(opponentPosition);
                                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(this.board, opponentPosition);
                                    if (wouldLandOnPiece(possibleCaptureMoves, new ChessPosition(1,6))){
                                        validMoves.add(castlingKingRightW);
                                    }
                                }
                            }
                        }
                    }
                }
                catch (CloneNotSupportedException e){
                    throw new RuntimeException("Bug in cloning for castling");
                }
            }
        }
        else if (currPieceTeam == TeamColor.BLACK && startPosition.equals(BKingPos) && !BKingHasMoved && !isInCheck(currPieceTeam)){
            if (this.board.getPiece(new ChessPosition(8,1)).getPieceType() == ChessPiece.PieceType.ROOK && !LBRookHasMoved && blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(8,1))){
                ChessMove castlingKingLeftB = new ChessMove(BKingPos, new ChessPosition(8,3), null);
                ChessMove castlingRookRightB = new ChessMove(new ChessPosition(8,1), new ChessPosition(8,4), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingLeftB.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingLeftB.getStartPosition());
                    clonedBoard.addPiece(castlingRookRightB.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingRookRightB.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        for (int i = 1; i < 9; i++) {
                            for (int j = 1; j < 9; j++) {
                                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() != currPieceTeam) {
                                    ChessPosition opponentPosition = new ChessPosition(i, j);
                                    ChessPiece opponentPiece = this.board.getPiece(opponentPosition);
                                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(this.board, opponentPosition);
                                    if (!wouldLandOnPiece(possibleCaptureMoves, new ChessPosition(8,4))){
                                        validMoves.add(castlingKingLeftB);
                                    }
                                }
                            }
                        }
                    }
                }
                catch (CloneNotSupportedException e){
                    throw new RuntimeException("Bug in cloning for castling");
                }
            }
            if (this.board.getPiece(new ChessPosition(8,8)).getPieceType() == ChessPiece.PieceType.ROOK && !RBRookHasMoved && blankSpaceCastlingChecker(currPieceTeam, new ChessPosition(8,8))){
                ChessMove castlingKingRightB = new ChessMove(BKingPos, new ChessPosition(8,7), null);
                ChessMove castlingRookLeftB = new ChessMove(new ChessPosition(8,8), new ChessPosition(8,6), null);
                try {
                    ChessBoard clonedBoard = this.board.clone();
                    clonedBoard.addPiece(castlingKingRightB.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingKingRightB.getStartPosition());
                    clonedBoard.addPiece(castlingRookLeftB.getEndPosition(), currPiece);
                    clonedBoard.removePiece(castlingRookLeftB.getStartPosition());
                    if (!isInCheck(clonedBoard, currPieceTeam)) {
                        for (int i = 1; i < 9; i++) {
                            for (int j = 1; j < 9; j++) {
                                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() != currPieceTeam) {
                                    ChessPosition opponentPosition = new ChessPosition(i, j);
                                    ChessPiece opponentPiece = this.board.getPiece(opponentPosition);
                                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(this.board, opponentPosition);
                                    if (wouldLandOnPiece(possibleCaptureMoves, new ChessPosition(8,6))){
                                        validMoves.add(castlingKingRightB);
                                    }
                                }
                            }
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
            Collection <ChessMove> possibleMoves_TEMPLATE = currPiece.pieceMoves(board, startPosition);
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
                            possibleMoves_TEMPLATE.remove(move);
                        }
                    }
                    validMoves = possibleMoves_TEMPLATE;

                    //Castling code
                    /*if (currPiece.getPieceType() == ChessPiece.PieceType.KING){
                        if (currPieceTeam == TeamColor.WHITE){
                            castlingMoves(currPiece, WKingPos, validMoves);
                        }
                        else{
                            castlingMoves(currPiece, BKingPos, validMoves);
                        }
                    }*/

                }
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException("Bug in the code");
            }
            kingsFinder(this.board);//to set it back to original board
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
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }
        else if (currValidMoves.isEmpty()){ //can't make a move that can't possibly exist
            throw new InvalidMoveException();
        }
        else if(!currValidMoves.contains(move)){ //any valid move should already be in Valid moves, and return false
            throw new InvalidMoveException();
        }
        else if(board.getPiece(move.getStartPosition()).getTeamColor() != thisTeamsTurn){ //not player's turn
            throw new InvalidMoveException();
        }
        else { //all test cases passed, a move now will be made
            if(pieceBeingMoved.getPieceType() == ChessPiece.PieceType.KING){

                if (move.getEndPosition().getColumn()+1 < move.getStartPosition().getColumn()) { //castling move LEFT
                    switch (pieceBeingMoved.getTeamColor()) {
                        case WHITE:
                            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                            this.board.removePiece(move.getStartPosition());
                            WKingPos = move.getEndPosition();

                            this.board.addPiece(new ChessPosition(1, 4), this.board.getPiece(new ChessPosition(1, 1)));//moving ROOK
                            this.board.removePiece(new ChessPosition(1, 1));
                            LWRookHasMoved = true;

                        case BLACK:
                            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                            this.board.removePiece(move.getStartPosition());
                            BKingPos = move.getEndPosition();

                            this.board.addPiece(new ChessPosition(8, 4), this.board.getPiece(new ChessPosition(8, 1)));//moving ROOK
                            this.board.removePiece(new ChessPosition(8, 1));
                            LBRookHasMoved = true;
                    }
                }
                else if (move.getEndPosition().getColumn()-1 > move.getStartPosition().getColumn()){ //castling move RIGHT
                    switch (pieceBeingMoved.getTeamColor()) {
                        case WHITE:
                            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                            this.board.removePiece(move.getStartPosition());
                            WKingPos = move.getEndPosition();

                            this.board.addPiece(new ChessPosition(1, 6), this.board.getPiece(new ChessPosition(1, 1)));//moving ROOK
                            this.board.removePiece(new ChessPosition(1, 1));
                            RWRookHasMoved = true;

                        case BLACK:
                            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                            this.board.removePiece(move.getStartPosition());
                            BKingPos = move.getEndPosition();

                            this.board.addPiece(new ChessPosition(8, 6), this.board.getPiece(new ChessPosition(8, 1)));//moving ROOK
                            this.board.removePiece(new ChessPosition(8, 1));
                            RBRookHasMoved = true;
                    }
                }
                else{
                    switch (pieceBeingMoved.getTeamColor()){
                        case WHITE:
                            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                            this.board.removePiece(move.getStartPosition());
                            WKingPos = move.getEndPosition();
                        case BLACK:
                            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                            this.board.removePiece(move.getStartPosition());
                            BKingPos = move.getEndPosition();

                            //SET BOOLEAN VARIABLES THAT KING MOVED
                            //DO A CHECK AND SEE IF THE MOVE IN QUESTION FOR THE KING IS 2+ SPACES, MUST BE A CASTLING MOVE
                    }
                }

                WKingHasMoved = true;
                BKingHasMoved = true;
            }
            else if (pieceBeingMoved.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
                ChessPiece newlyPromotedPawn = new ChessPiece(pieceBeingMoved.getTeamColor(), move.getPromotionPiece());
                this.board.addPiece(move.getEndPosition(), newlyPromotedPawn);
                this.board.removePiece(move.getStartPosition());
            }
            else{
                this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                this.board.removePiece(move.getStartPosition());
                //delete ChessPiece object at start pos of ChessMove in ChessBoard array, set to null.
                //Then set reference at end pos of ChessMove in ChessBoard array
            }
            //switches turns at the end of a move
            if (this.thisTeamsTurn == TeamColor.WHITE){
                this.thisTeamsTurn = TeamColor.BLACK;
            }
            else{
                this.thisTeamsTurn = TeamColor.WHITE;
            }
        }
    }


    private void kingsFinder(ChessBoard clonedBoard){
        //for auto-grader test scenarios
        boolean WKingFound = false;
        boolean BKingFound = false;

        for (int i = 1; i < 9; i++) { //Treat as row/column #s, not index values/one less
            for (int j = 1; j < 9; j++) {
                if (clonedBoard.getPiece(new ChessPosition(i, j)) != null && clonedBoard.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING){
                    if (clonedBoard.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.WHITE){
                        this.WKingPos = new ChessPosition(i, j);
                        WKingFound = true;
                    }
                    else{
                        this.BKingPos = new ChessPosition(i, j);
                        BKingFound = true;
                    }
                }
            }
        }
        if (!WKingFound && !BKingFound){ //literally only for the test scenarios, this would never happen in a game
            WKingPos = null;
            BKingPos = null;
        }
    }

    //capable of being used for both Kings (for isInCheck) AND to check if a rook would be captured after an attempted Castling move
    private boolean wouldLandOnPiece (Collection<ChessMove> movesProvided, ChessPosition posOfPieceInDanger){
            for (ChessMove potentialMove : movesProvided) {
                if (potentialMove.getEndPosition().equals(posOfPieceInDanger)) {
                    return true;
                    }
                }
            return false;
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

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i, j);
                    ChessPiece opponentPiece = this.board.getPiece(opponentPosition);
                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(this.board, opponentPosition);
                    if (teamColor == TeamColor.WHITE){
                        if (wouldLandOnPiece(possibleCaptureMoves, WKingPos)){
                            return true;
                        }
                    }
                    else{
                        if (wouldLandOnPiece(possibleCaptureMoves, BKingPos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(ChessBoard clonedBoard, TeamColor teamColor) { //OVERLOADED isInCheck FUNCTION
        kingsFinder(clonedBoard);

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (clonedBoard.getPiece(new ChessPosition(i, j)) != null && clonedBoard.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i, j);
                    ChessPiece opponentPiece = clonedBoard.getPiece(opponentPosition);
                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(clonedBoard, opponentPosition);
                    if (teamColor == TeamColor.WHITE){
                        if (wouldLandOnPiece(possibleCaptureMoves, WKingPos)){
                            return true;
                        }
                    }
                    else{
                        if (wouldLandOnPiece(possibleCaptureMoves, BKingPos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
        kingsFinder(this.board); //finds Kings' positions in a provided board/game (for the auto-grader tests
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
