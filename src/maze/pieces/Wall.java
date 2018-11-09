package maze.pieces;

import boardgame.Board;
import maze.Color;
import maze.MazePiece;

public class Wall extends MazePiece {

	public Wall(Board board, Color color) {
		super(board, color, false);
	}

	@Override
	public String toString() {
		return " ";
	}

}
