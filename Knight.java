package chess;

class Knight extends Piece {
    
    String name = "P";
    public Knight(boolean w){
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

        if (deltaX == 1 && deltaY == 2 && isNewSpotEmpty == true){
            legal = true;
        } else if (deltaX == 2 && deltaY == 1 && isNewSpotEmpty == true){ 
            legal  = true;
        } else if (deltaX == 1 && deltaY == 2 && isNewSpotEmpty == false){ 
			if (forCheck == false){
				return Chess.eat(piece,newX,newY);
			}
			legal = true;
        }
		else if (deltaX == 2 && deltaY == 1 && isNewSpotEmpty == false){
			if (forCheck == false){
				return Chess.eat(piece,newX,newY);
			}
			legal = true;
		}

        return legal; 
    }
}
