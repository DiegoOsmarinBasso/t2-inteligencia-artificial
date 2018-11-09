package maze;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class MazePiece extends Piece {

	private Color color;
	private boolean overwritable;

	public MazePiece(Board board, Color color, boolean overwritable) {
		super(board);
		this.color = color;
		this.overwritable = overwritable;
	}

	public Color getColor() {
		return color;
	}

	public boolean getOverwritable() {
		return overwritable;
	}

	public Position getPosition() {
		return position;
	}

	protected boolean isThereOpponentPiece(Position position) {
		MazePiece p = (MazePiece) getBoard().piece(position);
		return p != null && p.getColor() != color;
	}

}
