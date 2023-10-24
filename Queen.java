package chess;

class Queen extends Piece {
    
    String name = "P";
    public Queen(boolean w){
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

		//rook contraints 
		if (newY - oldY > 0 && newX == oldX)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 5);

		if (newY - oldY < 0 && newX == oldX)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 6);
			
		if (newY == oldY && newX - oldX < 0)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 7);
			
		if (newY == oldY && newX - oldX > 0)
				isPathClear = isPathClear(oldX, oldY, newX, newY, 8);
		//Rook moves
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

	 //bishop contraints and moves

        if (deltaX == deltaY && deltaX < 9 && deltaX > 0) {
            // Check if the path is clear (no pieces in between).
		if (newX - oldX < 0 && newY - oldY > 0){
			isPathClear = isPathClear(oldX, oldY, newX, newY, 1);
		}
		else if (newX - oldX > 0 && newY - oldY > 0){
			isPathClear = isPathClear(oldX, oldY, newX, newY, 2);
		}
		else if (newX - oldX < 0 && newY - oldY < 0){
			isPathClear = isPathClear(oldX, oldY, newX, newY, 3);
		}
		else if (newX - oldX > 0 && newY - oldY < 0){
			isPathClear = isPathClear(oldX, oldY, newX, newY, 4);
		}
		if (isNewSpotEmpty == true && isPathClear == true) {
			legal = true;
		} else if (isNewSpotEmpty == false && isPathClear == true) {
				if (forCheck == false){
					return Chess.eat(piece,newX,newY);
				}
				legal = true;
		}
    }
        return legal; 
    }

	public boolean isPathClear(char oldX, int oldY, char newX, int newY, int direction) {
		//1 = northwest; 2 = northeast; 3 = southwest; 4 = southeast
		
        if (direction == 1){
			int travel = Math.abs(newY - oldY);
			oldX--;
			oldY++;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldX--;
				oldY++;
			}
		}
		else if (direction == 2){
			int travel = Math.abs(newY - oldY);
			oldX++;
			oldY++;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldX++;
				oldY++;
			}
		}
		else if (direction == 3){
			int travel = Math.abs(newY - oldY);
			oldX--;
			oldY--;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldX--;
				oldY--;
			}
		}
		else if (direction == 4){
			int travel = Math.abs(newY - oldY);
			oldX++;
			oldY--;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldX++;
				oldY--;
			}
		}
		else if (direction == 5){				// start of rook movements
			int travel = Math.abs(newY - oldY);
			oldY++;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldY++;
			}
		}
		else if (direction == 6){
			int travel = Math.abs(newY - oldY);
			oldY--;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldY--;
			}
		}
		else if (direction == 7){
			int travel = Math.abs(newX - oldX);
			oldX--;
			for (int i = 0;i < travel - 1; i++){
				if (Chess.isEmpty(oldX,oldY) == false){
					return false;
				}
				oldX--;
			}
		}
		else if (direction == 8){
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
