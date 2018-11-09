package maze;

import boardgame.BoardException;

public class MazeException extends BoardException {

	private static final long serialVersionUID = 1L;

	public MazeException(String msg) {
		super(msg);
	}
}
