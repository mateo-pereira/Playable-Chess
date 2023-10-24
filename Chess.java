package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;
import chess.ReturnPlay.Message;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

// intended to be abstract so that it can be subclassed by other classes that have implementattion for the methods below
abstract class Piece {
    
    //Says which player the piece belongs to. True is white, false is black.
    private boolean white = true;
    
    // Sets the piece to belong to the white or black player. White is represented as True, black is represented as false.
    public void setWhite(boolean t){
        white = t;
    }
    
    //Tells whether the piece is which or black. White is true, black is false.
    public boolean isWhite(){
        return this.white;
    }
    public abstract boolean canMove(ReturnPiece piece, char oldX, int oldY, char newX, int newY, boolean isNewSpotEmpty, boolean forCheck);
}

public class Chess {
	
	static ArrayList<ReturnPiece> pieces = new ArrayList<>();
	static Player currentPlayer = Player.white;
	static boolean whiteKingMoved = false;
	static boolean blackKingMoved = false;
	static boolean kingInCheck = false;
	static ReturnPiece checkingPiece = new ReturnPiece();
	/*static boolean canBeEnPassanted = false;
	static ReturnPiece otherPawn = new ReturnPiece();
	static int check = 0;*/

	enum Player { white, black }
	
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */

	public static ReturnPiece findKing(Player player) { //  method to find the king of a specific player (either white or black).

        for (ReturnPiece piece : pieces) {     // Iterate through the pieces on the chessboard to search for the player's king.
            if ((player == Player.white && piece.pieceType == PieceType.BK) ||
                (player == Player.black && piece.pieceType == PieceType.WK)) {
                return piece;  // Return the found king piece when a match is found.
            }
        }

        return null;  // If no king piece is found----its going to return null.
    }
	
	public static ReturnPiece findPiece(char file, int rank){
		ReturnPiece foundPiece = new ReturnPiece();
		for (int i = 0; i < pieces.size(); i++ )
		{			
			if (pieces.get(i).pieceFile.toString().charAt(0) == file 
			&& pieces.get(i).pieceRank == rank){ //Comparing curr piece file to input file
				foundPiece = pieces.get(i); //Finds piece
			}
		}
		if (foundPiece == null) return null;

		return foundPiece;
	}

	public static boolean isEmpty(char file, int rank){
		boolean isEmpty = true;
		for (int i = 0; i < pieces.size(); i++ ) 
		{	
			if (pieces.get(i).pieceFile.toString().charAt(0) == file 
			&& pieces.get(i).pieceRank == rank){ //Comparing curr piece file to input file
				isEmpty = false;
			}
		}
		return isEmpty;
	}

	public static boolean castle(ReturnPiece king, String move, String castleSide){
		char file = move.charAt(3);
		int rank = Character.getNumericValue(move.charAt(4));


		if (castleSide == "White King"){
			if (isEmpty('f',1) == true && isEmpty('g', 1) == true
			&& whiteKingMoved == false){
				ReturnPiece rook = findPiece('h', 1);
				if (rook.pieceType == PieceType.WR && king.pieceType == PieceType.WK){
					actualMove(king, file, rank);
					actualMove(rook, 'f', 1);
				}
				else return false;
				
			}
			else return false;
		}
		else if (castleSide == "White Queen"){
			if (isEmpty('b',1) == true && isEmpty('c', 1) == true && isEmpty('d', 1) == true
			&& whiteKingMoved == false){
				ReturnPiece rook = findPiece('a', 1);
				if (rook.pieceType == PieceType.WR && king.pieceType == PieceType.WK){
					actualMove(king, file, rank);
					actualMove(rook, 'd', 1);
				}
				else return false;
			}
			else return false;
		}
		else if (castleSide == "Black King"){
			if (isEmpty('f',8) == true && isEmpty('g', 8) == true 
			&& blackKingMoved == false){
				ReturnPiece rook = findPiece('h', 8);
				if (rook.pieceType == PieceType.BR && king.pieceType == PieceType.BK){
					actualMove(king, file, rank);
					actualMove(rook, 'f', 8);
				}
				else return false;
			}
			else return false;
		}
		else if (castleSide == "Black Queen"){
			if (isEmpty('b',8) == true && isEmpty('c', 8) == true && isEmpty('d', 8) == true
			&& blackKingMoved == false){
				ReturnPiece rook = findPiece('a', 8);
				if (rook.pieceType == PieceType.BR && king.pieceType == PieceType.BK){
					actualMove(king, file, rank);
					actualMove(rook, 'd', 8);
				}
				else return false;
			}
			else return false;
		}
		else return false;

		return true;
	}

	public static boolean pawnPromotion(ReturnPiece piece, String move){
		if (move.charAt(6)!= 'q' && move.charAt(6)!= 'n' &&
		move.charAt(6)!= 'r' && move.charAt(6)!= 'b') return false;

		if (move(piece,move) == false) return false;
		
			//If player wants to promote to queen
		if(move.substring(6).toLowerCase().equals("q")){
				if (currentPlayer == Player.black){
					piece.pieceType = PieceType.BQ;
				}
				else if(currentPlayer == Player.white){
					piece.pieceType = PieceType.WQ;
				}
			}
		else if(move.substring(6).toLowerCase().equals("n")){
				if (currentPlayer == Player.white){
					piece.pieceType = PieceType.WN;
				}
				else if(currentPlayer == Player.black){
					piece.pieceType = PieceType.BN;
				}
			}
			//If player wants to promote to rook
			else if(move.substring(6).toLowerCase().equals("r")){
				if (currentPlayer == Player.white){
					piece.pieceType = PieceType.WR;
				}
				else if(currentPlayer == Player.black){
					piece.pieceType = PieceType.BR;
				}
			}
			//If player wants to promote to bishop
			else if(move.substring(6).toLowerCase().equals("b")){
				if (currentPlayer == Player.white){
					piece.pieceType = PieceType.WB;
				}
				else if(currentPlayer == Player.black){
					piece.pieceType = PieceType.BB;
				}
			}

		return true;
	}

	public static boolean enPassant(ReturnPiece otherPawn, char	file, int rank){
		boolean check = false;
		ReturnPiece temp = new ReturnPiece();

		//Looks for the actual piecefile value given char file
		for (PieceFile currFile: PieceFile.values()){
			String enumStr = currFile.name();
			char enumChar = enumStr.charAt(0);
			if (enumChar == file){
				temp.pieceFile = currFile;
			}
		}

		for (int i = 0; i < pieces.size(); i++ )
		{			
			if (pieces.get(i).pieceFile == otherPawn.pieceFile
			&& pieces.get(i).pieceRank == otherPawn.pieceRank){ 
				pieces.get(i).pieceFile = temp.pieceFile;
				pieces.get(i).pieceRank = rank;
				check = true;
			}
		}
		return check;
	}

	public static boolean eat(ReturnPiece oldPiece, char newX, int newY) {
		boolean legal = false;
		for (int i = 0;i < pieces.size();i++)
		{	
			String enumStr = pieces.get(i).pieceFile.name();
			char enumChar = enumStr.charAt(0);
			int newPieceRank = pieces.get(i).pieceRank;
			
			if (enumChar == newX && newPieceRank == newY) //When the eaten piece is found, delete it off the board
				//A check to make sure the player doesn't eat its own piece
				if (currentPlayer == Player.white && pieces.get(i).pieceType.toString().charAt(0) != 'W'){
					legal = true;
					pieces.remove(pieces.get(i));
				}
				else if (currentPlayer == Player.black && pieces.get(i).pieceType.toString().charAt(0) != 'B'){
					legal = true;
					pieces.remove(pieces.get(i));
				}
		}
		if (legal == true){
			for (PieceFile fileType: PieceFile.values())
			{
				String enumStr = fileType.name();
				char enumChar = enumStr.charAt(0);
				if (enumChar == newX) oldPiece.pieceFile = fileType;
			}
			oldPiece.pieceRank = newY;
		}
		

		return legal;
	}

	public static void actualMove(ReturnPiece piece, char file, int rank){
		for (PieceFile fileType: PieceFile.values()) 
		{
			String enumStr = fileType.name();
			char enumChar = enumStr.charAt(0);
			for (int i = 1;i < 9;i++)
			{
				if (enumChar == file && i == rank)
				{
					piece.pieceFile = fileType;
					piece.pieceRank = i;
				}
			}
		}
	}

	public static boolean isCheck(ReturnPiece currPiece){

		boolean isEmpty = false; // because king is occupying end space
		ReturnPiece opponentKing = findKing(currentPlayer);
		char currPieceFile = currPiece.pieceFile.name().charAt(0);
		int currPieceRank = currPiece.pieceRank;
		char kingFile = opponentKing.pieceFile.name().charAt(0);
		int kingRank = opponentKing.pieceRank;
        
		 if (currPiece.pieceType == PieceType.WP || currPiece.pieceType == PieceType.BP){
            Pawn pawn = new Pawn(isEmpty); 
			if (pawn.canMove(currPiece, currPieceFile, currPieceRank, kingFile, kingRank, isEmpty,true)){
				kingInCheck = true;
				checkingPiece = currPiece;
				return true;
			}
               
        }
        //Knight
        else if(currPiece.pieceType == PieceType.WN || currPiece.pieceType == PieceType.BN){
            Knight knight = new Knight(isEmpty);
			if (knight.canMove(currPiece, currPieceFile, currPieceRank, kingFile, kingRank, isEmpty,true)){
				kingInCheck = true;
				checkingPiece = currPiece;
				return true;
			}

        }
        //Bishop
        else if(currPiece.pieceType == PieceType.WB || currPiece.pieceType == PieceType.BB){
            Bishop bishop = new Bishop(isEmpty);
			if (bishop.canMove(currPiece, currPieceFile, currPieceRank, kingFile, kingRank, isEmpty,true)){
				kingInCheck = true;
				checkingPiece = currPiece;
				return true;
			}
        }
        //Rook
        else if(currPiece.pieceType == PieceType.WR || currPiece.pieceType == PieceType.BR){
            Rook rook = new Rook(isEmpty);
			if (rook.canMove(currPiece, currPieceFile, currPieceRank, kingFile, kingRank, isEmpty,true)){
				kingInCheck = true;
				checkingPiece = currPiece;
				return true;
			}
        }
        //Queen
        else if(currPiece.pieceType == PieceType.WQ || currPiece.pieceType == PieceType.BQ){
            Queen queen = new Queen(isEmpty);
			if (queen.canMove(currPiece, currPieceFile, currPieceRank, kingFile, kingRank, isEmpty,true)){
				kingInCheck = true;
				checkingPiece = currPiece;
				return true;
			}

		}    
        return false;
    }

	//Actually moves the piece
	public static Boolean move(ReturnPiece piece, String move) { 

		// Castling
		if (currentPlayer == Player.white){ 
			if (move.equals("e1 g1"))
				return castle(piece,move,"White King"); //if it cant castle return false
			
			else if (move.equals("e1 c1"))
				return castle(piece,move,"White Queen");
		}
		if (currentPlayer == Player.black){ 
			if (move.equals("e8 g8"))
				return castle(piece,move,"Black King");
			
			else if (move.equals("e8 c8"))
				return castle(piece,move,"Black Queen");
		}

		char fileOld = move.charAt(0);
		int rankOld = Character.getNumericValue(move.charAt(1));
		char fileNew = move.charAt(3);
		int rankNew = Character.getNumericValue(move.charAt(4));

		if((currentPlayer == Player.white && piece.pieceType.toString().charAt(0) == 'B' ) || // checks if piece belongs to the players turn
        (currentPlayer == Player.black && piece.pieceType.toString().charAt(0) == 'W' )){
            return false; // returns illegeal move if its not their color.
        }

		/*if (check == 1 && (piece.pieceType != PieceType.WP && 
							piece.pieceType != PieceType.BP)){
			canBeEnPassanted = false;
			check = 0;
		}*/
		boolean isEmpty = isEmpty(fileNew,rankNew);
			 //Sees if the square is empty
		//Pawn
		if (piece.pieceType == PieceType.WP || piece.pieceType == PieceType.BP){
			Pawn pawn = new Pawn(isEmpty); //Parameter is set to change

			if (pawn.canMove(piece, fileOld,rankOld,fileNew,rankNew,isEmpty,false) == true){ //Current first move check
				if (currentPlayer == Player.white && Math.abs(rankNew-rankOld) == 2){
					if (piece.pieceRank == 2){
						/*otherPawn = piece;
						canBeEnPassanted = true;
						check = 1;*/
						actualMove(piece, fileNew, rankNew);
					}
					else return false;
				}
				else if (currentPlayer == Player.black && Math.abs(rankNew-rankOld) == 2){
					if (piece.pieceRank == 7){
						/*otherPawn = piece;
						canBeEnPassanted = true;
						check = 1;*/
						actualMove(piece, fileNew, rankNew);
					}
						
					else return false;
				}
				else{
					actualMove(piece, fileNew, rankNew);
				}
			}
			else return false;
		}
		//Knight
		else if(piece.pieceType == PieceType.WN || piece.pieceType == PieceType.BN){
			Knight knight = new Knight(isEmpty);

			if (knight.canMove(piece, fileOld,rankOld,fileNew,rankNew,isEmpty,false) == true)
				actualMove(piece, fileNew, rankNew);
			else return false;
		}
		//Bishop
		else if(piece.pieceType == PieceType.WB || piece.pieceType == PieceType.BB){
			Bishop bishop = new Bishop(isEmpty);

			if (bishop.canMove(piece, fileOld,rankOld,fileNew,rankNew,isEmpty,false) == true)
				actualMove(piece, fileNew, rankNew);
			else return false;
		}
		//Rook
		else if(piece.pieceType == PieceType.WR || piece.pieceType == PieceType.BR){
			Rook rook = new Rook(isEmpty);

			if (rook.canMove(piece, fileOld,rankOld,fileNew,rankNew,isEmpty,false) == true)
				actualMove(piece, fileNew, rankNew);
			else return false;
		}
		//Queen
		else if(piece.pieceType == PieceType.WQ || piece.pieceType == PieceType.BQ){
			Queen queen = new Queen(isEmpty);

			if (queen.canMove(piece, fileOld,rankOld,fileNew,rankNew,isEmpty,false) == true)
				actualMove(piece, fileNew, rankNew);
			else return false;
		}
		//King
		else if(piece.pieceType == PieceType.WK || piece.pieceType == PieceType.BK){
			King king = new King(isEmpty);

			if (king.canMove(piece, fileOld,rankOld,fileNew,rankNew,isEmpty,false) == true)
				actualMove(piece, fileNew, rankNew);
			else return false;

			//Kept track if either kings moved for castling
			if (currentPlayer == Player.white) whiteKingMoved = true;
			else blackKingMoved = true;
		}

		return true;
	}

	public static ReturnPlay play(String move) {

		ReturnPlay returnMessage = new ReturnPlay();
		returnMessage.piecesOnBoard = pieces;


		if (move.length() < 5){
			returnMessage.message = Message.ILLEGAL_MOVE;
			return returnMessage;
		}

		if (move.equals("resign"))
		{
			if (currentPlayer == Player.white)
			{
				returnMessage.message = Message.RESIGN_BLACK_WINS;
				return returnMessage;
			}
			else
			{
				returnMessage.message = Message.RESIGN_WHITE_WINS;
				return returnMessage;
			}
			
		}


		char file = move.charAt(0);
		int rank = Character.getNumericValue(move.charAt(1));

		//Finds the piece that is on specified file and rank
		ReturnPiece foundPiece = findPiece(file, rank); 
		if (foundPiece.pieceType == null){ //Checks if there actually is a piece there
			returnMessage.message = Message.ILLEGAL_MOVE;
			return returnMessage;
		}

		//Actual moving

		if (move.length() == 5){
			boolean moveCheck = move(foundPiece,move);

			if (moveCheck == false)
			{
				returnMessage.message = Message.ILLEGAL_MOVE;
				return returnMessage;
			}

			if (kingInCheck == true){
				if (isCheck(checkingPiece) ==  false){
					returnMessage.message = Message.ILLEGAL_MOVE;
					actualMove(foundPiece, file, rank);
					return returnMessage;
				}
				
			}

			if (isCheck(foundPiece)){
				returnMessage.message = Message.CHECK;
			}
			else{
				kingInCheck = false;
			}

			//Pawn promotion if piece that player wants to promote to is not specified
			if (foundPiece.pieceType == PieceType.WP || foundPiece.pieceType == PieceType.BP){ 
				if (foundPiece.pieceRank == 1 || foundPiece.pieceRank == 8){
					if (currentPlayer == Player.white){
					foundPiece.pieceType = PieceType.WQ;
				}
				else if(currentPlayer == Player.black){
					foundPiece.pieceType = PieceType.BQ;
				}
				}
			}
		}
		
		if (move.length() > 5){ //Checks if the input was (e.g e2 e4 DRAW)
			if (move.substring(6).toLowerCase().equals("draw?")){
				if (move(foundPiece,move) == false){
					returnMessage.message = Message.ILLEGAL_MOVE;
					return returnMessage;
				}
				returnMessage.message = Message.DRAW;
				return returnMessage;
			}
			else if (move.length() == 7){
				if (pawnPromotion(foundPiece, move) == false){
					returnMessage.message = Message.ILLEGAL_MOVE;
					return returnMessage;
				}
			}

			else{
				returnMessage.message = Message.ILLEGAL_MOVE;
				return returnMessage;
			}
		}


		if (currentPlayer == Player.white) //Changes turns
		{
			System.out.println();
			System.out.println("Black Moves");
			System.out.println();
			currentPlayer = Player.black;
		}
		else
		{
			System.out.println();
			System.out.println("White Moves");
			System.out.println();
			currentPlayer = Player.white;

		}	

		

		return returnMessage;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {

		//Initializing each chess piece object and setting their correct positions
		pieces = new ArrayList<>();
		currentPlayer = Player.white;
		whiteKingMoved = false;
		blackKingMoved = false;
		kingInCheck = false;
		checkingPiece = new ReturnPiece();
		//WHITE PAWNS
		ReturnPiece wp1 = new ReturnPiece();
		wp1.pieceType = PieceType.WP;
		wp1.pieceFile = PieceFile.a;
		wp1.pieceRank = 2;
		pieces.add(wp1);

		ReturnPiece wp2 = new ReturnPiece();
		wp2.pieceType = PieceType.WP;
		wp2.pieceFile = PieceFile.b;
		wp2.pieceRank = 2;
		pieces.add(wp2);

		ReturnPiece wp3 = new ReturnPiece();
		wp3.pieceType = PieceType.WP;
		wp3.pieceFile = PieceFile.c;
		wp3.pieceRank = 2;
		pieces.add(wp3);

		ReturnPiece wp4 = new ReturnPiece();
		wp4.pieceType = PieceType.WP;
		wp4.pieceFile = PieceFile.d;
		wp4.pieceRank = 2;
		pieces.add(wp4);

		ReturnPiece wp5 = new ReturnPiece();
		wp5.pieceType = PieceType.WP;
		wp5.pieceFile = PieceFile.e;
		wp5.pieceRank = 2;
		pieces.add(wp5);

		ReturnPiece wp6 = new ReturnPiece();
		wp6.pieceType = PieceType.WP;
		wp6.pieceFile = PieceFile.f;
		wp6.pieceRank = 2;
		pieces.add(wp6);

		ReturnPiece wp7 = new ReturnPiece();
		wp7.pieceType = PieceType.WP;
		wp7.pieceFile = PieceFile.g;
		wp7.pieceRank = 2;
		pieces.add(wp7);

		ReturnPiece wp8 = new ReturnPiece();
		wp8.pieceType = PieceType.WP;
		wp8.pieceFile = PieceFile.h;
		wp8.pieceRank = 2;
		pieces.add(wp8);

		//BLACK PAWNS
		ReturnPiece bp1 = new ReturnPiece();
		bp1.pieceType = PieceType.BP;
		bp1.pieceFile = PieceFile.a;
		bp1.pieceRank = 7;
		pieces.add(bp1);

		ReturnPiece bp2 = new ReturnPiece();
		bp2.pieceType = PieceType.BP;
		bp2.pieceFile = PieceFile.b;
		bp2.pieceRank = 7;
		pieces.add(bp2);

		ReturnPiece bp3 = new ReturnPiece();
		bp3.pieceType = PieceType.BP;
		bp3.pieceFile = PieceFile.c;
		bp3.pieceRank = 7;
		pieces.add(bp3);

		ReturnPiece bp4 = new ReturnPiece();
		bp4.pieceType = PieceType.BP;
		bp4.pieceFile = PieceFile.d;
		bp4.pieceRank = 7;
		pieces.add(bp4);

		ReturnPiece bp5 = new ReturnPiece();
		bp5.pieceType = PieceType.BP;
		bp5.pieceFile = PieceFile.e;
		bp5.pieceRank = 7;
		pieces.add(bp5);

		ReturnPiece bp6 = new ReturnPiece();
		bp6.pieceType = PieceType.BP;
		bp6.pieceFile = PieceFile.f;
		bp6.pieceRank = 7;
		pieces.add(bp6);

		ReturnPiece bp7 = new ReturnPiece();
		bp7.pieceType = PieceType.BP;
		bp7.pieceFile = PieceFile.g;
		bp7.pieceRank = 7;
		pieces.add(bp7);

		ReturnPiece bp8 = new ReturnPiece();
		bp8.pieceType = PieceType.BP;
		bp8.pieceFile = PieceFile.h;
		bp8.pieceRank = 7;
		pieces.add(bp8);

		//WHITE ROOKS
		ReturnPiece wr1 = new ReturnPiece();
		wr1.pieceType = PieceType.WR;
		wr1.pieceFile = PieceFile.a;
		wr1.pieceRank = 1;
		pieces.add(wr1);

		ReturnPiece wr2 = new ReturnPiece();
		wr2.pieceType = PieceType.WR;
		wr2.pieceFile = PieceFile.h;
		wr2.pieceRank = 1;
		pieces.add(wr2);

		//BLACK ROOKS
		ReturnPiece br1 = new ReturnPiece();
		br1.pieceType = PieceType.BR;
		br1.pieceFile = PieceFile.a;
		br1.pieceRank = 8;
		pieces.add(br1);

		ReturnPiece br2 = new ReturnPiece();
		br2.pieceType = PieceType.BR;
		br2.pieceFile = PieceFile.h;
		br2.pieceRank = 8;
		pieces.add(br2);

		//WHITE KNIGHTS
		ReturnPiece wn1 = new ReturnPiece();
		wn1.pieceType = PieceType.WN;
		wn1.pieceFile = PieceFile.b;
		wn1.pieceRank = 1;
		pieces.add(wn1);

		ReturnPiece wn2 = new ReturnPiece();
		wn2.pieceType = PieceType.WN;
		wn2.pieceFile = PieceFile.g;
		wn2.pieceRank = 1;
		pieces.add(wn2);

		//BLACK KNIGHTS
		ReturnPiece bn1 = new ReturnPiece();
		bn1.pieceType = PieceType.BN;
		bn1.pieceFile = PieceFile.b;
		bn1.pieceRank = 8;
		pieces.add(bn1);

		ReturnPiece bn2 = new ReturnPiece();
		bn2.pieceType = PieceType.BN;
		bn2.pieceFile = PieceFile.g;
		bn2.pieceRank = 8;
		pieces.add(bn2);

		//WHITE BISHOPS
		ReturnPiece wb1 = new ReturnPiece();
		wb1.pieceType = PieceType.WB;
		wb1.pieceFile = PieceFile.c;
		wb1.pieceRank = 1;
		pieces.add(wb1);

		ReturnPiece wb2 = new ReturnPiece();
		wb2.pieceType = PieceType.WB;
		wb2.pieceFile = PieceFile.f;
		wb2.pieceRank = 1;
		pieces.add(wb2);

		//BLACK BISHOPS
		ReturnPiece bb1 = new ReturnPiece();
		bb1.pieceType = PieceType.BB;
		bb1.pieceFile = PieceFile.c;
		bb1.pieceRank = 8;
		pieces.add(bb1);

		ReturnPiece bb2 = new ReturnPiece();
		bb2.pieceType = PieceType.BB;
		bb2.pieceFile = PieceFile.f;
		bb2.pieceRank = 8;
		pieces.add(bb2);

		//WHITE QUEEN
		ReturnPiece wq = new ReturnPiece();
		wq.pieceType = PieceType.WQ;
		wq.pieceFile = PieceFile.d;
		wq.pieceRank = 1;
		pieces.add(wq);

		//BLACK QUEEN
		ReturnPiece bq = new ReturnPiece();
		bq.pieceType = PieceType.BQ;
		bq.pieceFile = PieceFile.d;
		bq.pieceRank = 8;
		pieces.add(bq);

		//WHITE KING
		ReturnPiece wk = new ReturnPiece();
		wk.pieceType = PieceType.WK;
		wk.pieceFile = PieceFile.e;
		wk.pieceRank = 1;
		pieces.add(wk);

		//BLACK KING
		ReturnPiece bk = new ReturnPiece();
		bk.pieceType = PieceType.BK;
		bk.pieceFile = PieceFile.e;
		bk.pieceRank = 8;
		pieces.add(bk);

		//PRINTING OUT STARTING BOARD
		System.out.println();
		PlayChess.printBoard(pieces);
		System.out.println();
		System.out.println("White Moves\n");
	}
}