package maze.pieces;

import boardgame.Board;
import maze.Color;
import maze.MazePiece;

public class Agent extends MazePiece {

	private char direction;
	
	public Agent(Board board, Color color) {
		super(board, color, false);
		direction = '\0';
	}

	@Override
	public String toString() {
		return "A";
	}

	public char getDirection() {
		return direction;
	}

	public void setDirection(char direction) {
		this.direction = direction;
	}
	

}
