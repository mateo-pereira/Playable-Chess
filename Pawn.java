package chess;

class Pawn extends Piece {
    
    String name = "P";
    public Pawn(boolean w){
        this.setWhite(w);

        if (w == true){
            this.name = "w" + this.name; 
        }else{
            this.name = "b" + this.name; 
        }
    }
    public boolean canMove(ReturnPiece piece, char oldX, int oldY, char newX, int newY, boolean isNewSpotEmpty, boolean forCheck) { 
        int deltaX;
        int deltaY; 
        boolean legal = false; 

		int old = 0;
		int newOne = 0;
		char[] arr = {'a','b','c','d','e','f','g','h'};
		for (int i = 0; i < arr.length;i++)
		{
			if (arr[i] == oldX) old = i;
			if (arr[i] == newX) newOne = i;
		}
		deltaX = Math.abs(newOne - old);

        deltaY = Math.abs(newY - oldY);


        if (deltaY == 1 && deltaX == 0 && isNewSpotEmpty == true){ // deltay being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            legal = true;
        } else if (deltaX == 1 && deltaY == 1 && isNewSpotEmpty == false){  //pawn capture--------- x=1 is checking if theres a one square horrizontal movement (diagonal capture) y =1 checks if there is a one square foward or backward move
			if (forCheck == false){
				return Chess.eat(piece,newX,newY); //Replacing new pawn with old pawn
			}
			legal = true;
        } else if (deltaY == 2 && deltaX == 0 && isNewSpotEmpty == true){ //this.firstmove is checking if its the pawns first move, y = 2 checks if pawn is moving 2 squares foward on first move
            if (Chess.currentPlayer == Chess.Player.white){
				if(!Chess.isEmpty(oldX, oldY + 1)) return false;
			}
			else if (Chess.currentPlayer == Chess.Player.black){
				if(!Chess.isEmpty(oldX, oldY - 1)) return false;
			}
			legal = true; 
        }

        return legal; 
    }
}
