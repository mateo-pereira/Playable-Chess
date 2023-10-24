package chess;

class King extends Piece {
    
    String name = "P";
    public King(boolean w){
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

        if (deltaX == 1 && deltaY == 1 && isNewSpotEmpty == true){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            legal = true;
		}
		else if (deltaX == 1 && deltaY == 0 && isNewSpotEmpty == true){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            legal = true;
		}
		else if (deltaX == 0 && deltaY == 1 && isNewSpotEmpty == true){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            legal = true;
		}
		else if (deltaX == 1 && deltaY == 1 && isNewSpotEmpty == false){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            return Chess.eat(piece,newX,newY);
		}
		else if (deltaX == 1 && deltaY == 0 && isNewSpotEmpty == false){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            return Chess.eat(piece,newX,newY);
		}
		else if (deltaX == 0 && deltaY == 1 && isNewSpotEmpty == false){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            return Chess.eat(piece,newX,newY);
		}

        return legal; 
    }
}
