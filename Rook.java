package chess;

class Rook extends Piece {
    
    String name = "P";
    public Rook(boolean w){
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

		boolean isPathClear = true;

		if (newY - oldY > 0 && newX == oldX)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 1);

		if (newY - oldY < 0 && newX == oldX)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 2);
			
		if (newY == oldY && newX - oldX < 0)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 3);
			
		if (newY == oldY && newX - oldX > 0)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 4);
			

		if (deltaX == 0 && deltaY < 8 && deltaY > 0 && isNewSpotEmpty && isPathClear){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            legal = true;
		}
		else if (deltaY == 0 && deltaX < 8 && deltaX > 0 && isNewSpotEmpty && isPathClear){
			legal = true;
		}
		else if (deltaX == 0 && deltaY < 8 && deltaY > 0 && !isNewSpotEmpty && isPathClear){ // deltaY being 1 checks of the pawn is moving one swaure foward or backwards. x =0 is checking that there is no horrizontal movement.
            	if (forCheck == false){
					return Chess.eat(piece,newX,newY);
				}
				legal = true;
		}
		else if (deltaY == 0 && deltaX < 8 && deltaX > 0 && !isNewSpotEmpty && isPathClear){
				if (forCheck == false){
					return Chess.eat(piece,newX,newY);
				}
				legal = true;
		}

        return legal; 
    }

	public boolean isPathClear(char oldX, int oldY, char newX, int newY, int direction) {
		//1 = north; 2 = south; 3 = east; 4 = west
		
        if (direction == 1){
			int travel = Math.abs(newY - oldY);
			oldY++;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldY++;
			}
		}
		else if (direction == 2){
			int travel = Math.abs(newY - oldY);
			oldY--;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldY--;
			}
		}
		else if (direction == 3){
			int travel = Math.abs(newX - oldX);
			oldX--;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldX--;
			}
		}
		else if (direction == 4){
			int travel = Math.abs(newX - oldX);
			oldX++;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldX++;
			}
		}
		
        return true; // The path is clear.
    }
}
