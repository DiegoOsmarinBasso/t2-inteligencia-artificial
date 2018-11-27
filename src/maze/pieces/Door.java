package maze.pieces;

import boardgame.Board;
import maze.Color;
import maze.MazePiece;

public class Door extends MazePiece {

	public Door(Board board, Color color) {
		super(board, color, true);
	}

	@Override
	public String toString() {
		return "D";
	}

}
