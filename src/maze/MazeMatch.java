package maze;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import maze.pieces.Agent;
import maze.pieces.Bag;
import maze.pieces.Door;
import maze.pieces.Hole;
import maze.pieces.Wall;

public class MazeMatch {

	protected static final int BOARD_ROWS = 8;
	protected static final int BOARD_COLUMNS = 3;
	protected static final int JUMP = 2;

	private Board board;
	private boolean finalizeMaze;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public MazeMatch() {
		board = new Board(BOARD_ROWS, BOARD_COLUMNS);
		finalizeMaze = false;
		InitialSetup();
	}

	public boolean getFinalizeMaze() {
		return finalizeMaze;
	}

	public MazePiece[][] getPieces() {
		MazePiece[][] mat = new MazePiece[board.getRows()][board.getColumns()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (MazePiece) board.piece(i, j);
			}
		}

		return mat;
	}

	public Bag performMazeMove(Position source, Position target) {
		Agent a = (Agent) board.removePiece(source);
		Piece piece = board.removePiece(target);

		if (piece instanceof Door) {
			finalizeMaze = true;
			piece = null;
		}

		if (piece instanceof Bag) {
			piecesOnTheBoard.remove((Bag) piece);
			capturedPieces.add((Bag) piece);

		}
		board.placePiece(a, target);
		return (Bag) piece;

	}

	public void validateSourcePosition(Position position) {
		Piece source = board.piece(position);
		if (source == null || !(source instanceof Agent)) {
			throw new MazeException("Only the Agent can move.");
		}
	}

	public void validateTargetPosition(Position position) {
		Piece target = board.piece(position);
		if (!(target == null || target instanceof Bag || target instanceof Door)) {
			throw new MazeException("It is not possible to move to position " + position + ".");
		}
	}

	private void placeNewPiece(int row, int column, MazePiece piece) {
		board.placePiece(piece, new Position(row, column));
		piecesOnTheBoard.add(piece);
	}

	private void InitialSetup() {

		// Line 0
		placeNewPiece(0, 0, new Agent(board, Color.WHITE));
		placeNewPiece(0, 1, new Wall(board, Color.CYAN));
		placeNewPiece(0, 2, new Wall(board, Color.CYAN));

		// Line 1
		placeNewPiece(1, 1, new Hole(board, Color.RED));

		// Line 2
		placeNewPiece(2, 0, new Wall(board, Color.CYAN));
		placeNewPiece(2, 1, new Wall(board, Color.CYAN));
		placeNewPiece(2, 2, new Bag(board, Color.YELLOW));

		// Line 3
		placeNewPiece(3, 1, new Hole(board, Color.RED));
		placeNewPiece(3, 2, new Bag(board, Color.YELLOW));

		// Line 4
		placeNewPiece(4, 0, new Hole(board, Color.RED));
		placeNewPiece(4, 1, new Wall(board, Color.CYAN));
		placeNewPiece(4, 2, new Wall(board, Color.CYAN));

		// Line 5
		placeNewPiece(5, 0, new Bag(board, Color.YELLOW));
		placeNewPiece(5, 2, new Bag(board, Color.YELLOW));

		// Line 6
		placeNewPiece(6, 0, new Wall(board, Color.CYAN));
		placeNewPiece(6, 1, new Wall(board, Color.CYAN));
		placeNewPiece(6, 2, new Hole(board, Color.RED));

		// Line 7
		placeNewPiece(7, 0, new Door(board, Color.GREEN));
		placeNewPiece(7, 1, new Bag(board, Color.YELLOW));
	}

}
