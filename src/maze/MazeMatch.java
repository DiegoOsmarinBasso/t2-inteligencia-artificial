package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import boardgame.Board;
import boardgame.BoardException;
import boardgame.Piece;
import boardgame.Position;
import maze.pieces.Agent;
import maze.pieces.Bag;
import maze.pieces.Chest;
import maze.pieces.Door;
import maze.pieces.Hole;
import maze.pieces.Wall;

public class MazeMatch {

	protected static final int BOARD_ROWS = 10;
	protected static final int BOARD_COLUMNS = 10;
	protected static final int CHESTS_NUMBER = 4;
	protected static final int WALLS_NUMBER = 4;
	protected static final int HOLES_NUMBER = 10;
	protected static final int BAGS_NUMBER = 12;
	protected static final int AGENTS_NUMBER = 1;
	protected static final int WALL_SIZE = 5;
	protected static final int SEPARATION = 2;
	private static final Random rand = new Random();

	private Board board;
	private boolean allBags;
	private boolean finalizeMaze;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public MazeMatch() {
		board = new Board(BOARD_ROWS, BOARD_COLUMNS);
		InitialSetup();
	}

	public boolean getAllBags() {
		return allBags;
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
		Bag capturedBag = (Bag) board.removePiece(target);
		board.placePiece(a, target);

		if (capturedBag != null) {
			piecesOnTheBoard.remove(capturedBag);
			capturedPieces.add(capturedBag);
		}

		return capturedBag;
	}

	public void validateSourcePosition(Position position) {
		Piece source = board.piece(position);
		if (source == null || !(source instanceof Agent)) {
			throw new MazeException("Only the Agent can move.");
		}
	}

	public void validateTargetPosition(Position position) {
		Piece target = board.piece(position);
		if (!(target == null || target instanceof Bag)) {
			throw new MazeException("It is not possible to move to position " + position + ".");
		}
	}

	private void placeNewPiece(int row, int column, MazePiece piece) {
		board.placePiece(piece, new Position(row, column));
		piecesOnTheBoard.add(piece);
	}

	private void InitialSetup() {

		boolean horizontal = rand.nextBoolean();

		// HORIZONTAL
		if (horizontal) {

			int doorCol = rand.nextInt(BOARD_ROWS);
			int bigWallRow = rand.nextInt(2) * (BOARD_ROWS - 1);
			int chestRow = bigWallRow == 0 ? 1 : BOARD_ROWS - 2;

			// Door
			placeNewPiece(bigWallRow, doorCol, new Door(board, Color.GREEN));

			// Big Wall
			for (int i = 1; i < BOARD_ROWS; i++) {
				placeNewPiece(bigWallRow, (i + doorCol) % BOARD_ROWS, new Wall(board, Color.CYAN));
			}

			// Chests
			for (int i = 0; i < CHESTS_NUMBER;) {
				try {
					int chestCol = rand.nextInt(BOARD_COLUMNS);

					if (chestCol != doorCol) {
						placeNewPiece(chestRow, chestCol, new Chest(board, Color.PURPLE));
						i++;
					}
				} catch (BoardException e) {
				}
			}

			// Random Walls
			int minRow = bigWallRow == 0 ? SEPARATION : 0;
			int maxRow = bigWallRow == 0 ? BOARD_ROWS : BOARD_ROWS - SEPARATION;

			for (int i = 0; i < WALLS_NUMBER;) {

				boolean includeWall = true;

				// Horizontal Random Wall
				if (rand.nextBoolean()) {
					int wallRow = minRow + rand.nextInt(maxRow - minRow);
					int initialCol = rand.nextInt(BOARD_COLUMNS - WALL_SIZE + 1);

					for (int j = 0; j < WALL_SIZE; j++) {
						if (board.thereIsAPiece(wallRow, initialCol + j)) {
							includeWall = false;
							break;
						}
					}

					if (includeWall) {
						for (int j = 0; j < WALL_SIZE; j++) {
							placeNewPiece(wallRow, initialCol + j, new Wall(board, Color.CYAN));
						}
						i++;
					}
				}
				// Vertical Random Wall
				else {
					int initialRow = minRow + rand.nextInt(BOARD_ROWS - WALL_SIZE - 1);
					int wallCol = rand.nextInt(BOARD_COLUMNS);

					for (int j = 0; j < WALL_SIZE; j++) {
						if (board.thereIsAPiece(initialRow + j, wallCol)) {
							includeWall = false;
							break;
						}
					}

					if (includeWall) {
						for (int j = 0; j < WALL_SIZE; j++) {
							placeNewPiece(initialRow + j, wallCol, new Wall(board, Color.CYAN));
						}
						i++;
					}
				}
			}

		} else { // VERTICAL

			int doorRow = rand.nextInt(BOARD_COLUMNS);
			int bigWallCol = rand.nextInt(2) * (BOARD_COLUMNS - 1);
			int chestCol = bigWallCol == 0 ? 1 : BOARD_ROWS - 2;

			// Door
			placeNewPiece(doorRow, bigWallCol, new Door(board, Color.GREEN));

			// Big Wall
			for (int i = 1; i < BOARD_COLUMNS; i++) {
				placeNewPiece((i + doorRow) % BOARD_COLUMNS, bigWallCol, new Wall(board, Color.CYAN));
			}

			// Chests
			for (int i = 0; i < CHESTS_NUMBER;) {
				try {
					int chestRow = rand.nextInt(BOARD_COLUMNS);

					if (chestRow != doorRow) {
						placeNewPiece(chestRow, chestCol, new Chest(board, Color.PURPLE));
						i++;
					}
				} catch (BoardException e) {
				}
			}

			// Random Walls
			int minCol = bigWallCol == 0 ? SEPARATION : 0;
			int maxCol = bigWallCol == 0 ? BOARD_ROWS : BOARD_ROWS - SEPARATION;

			for (int i = 0; i < WALLS_NUMBER;) {

				boolean includeWall = true;

				// Vertical Random Wall
				if (rand.nextBoolean()) {
					int wallCol = minCol + rand.nextInt(maxCol - minCol);
					int initialRow = rand.nextInt(BOARD_ROWS - WALL_SIZE + 1);

					for (int j = 0; j < WALL_SIZE; j++) {
						if (board.thereIsAPiece(initialRow + j, wallCol)) {
							includeWall = false;
							break;
						}
					}

					if (includeWall) {
						for (int j = 0; j < WALL_SIZE; j++) {
							placeNewPiece(initialRow + j, wallCol, new Wall(board, Color.CYAN));
						}
						i++;
					}
				}
				// Horizontal Random Wall
				else {
					int initialCol = minCol + rand.nextInt(BOARD_COLUMNS - WALL_SIZE - 1);
					int wallRow = rand.nextInt(BOARD_ROWS);

					for (int j = 0; j < WALL_SIZE; j++) {
						if (board.thereIsAPiece(wallRow, initialCol + j)) {
							includeWall = false;
							break;
						}
					}

					if (includeWall) {
						for (int j = 0; j < WALL_SIZE; j++) {
							placeNewPiece(wallRow, initialCol + j, new Wall(board, Color.CYAN));
						}
						i++;
					}
				}
			}

		}

		// Holes
		for (int i = 0; i < HOLES_NUMBER;) {

			int holeRow = rand.nextInt(BOARD_ROWS);
			int holeCol = rand.nextInt(BOARD_COLUMNS);

			// There is already a piece in the position
			if (board.thereIsAPiece(holeRow, holeCol)) {
				continue;
			}

			// There is already a hole below
			if (holeRow > 0 && board.piece(holeRow - 1, holeCol) instanceof Hole) {
				continue;
			}

			// There is already a hole above
			if (holeRow < BOARD_ROWS - 1 && board.piece(holeRow + 1, holeCol) instanceof Hole) {
				continue;
			}

			// There is already a hole in the left
			if (holeCol > 0 && board.piece(holeRow, holeCol - 1) instanceof Hole) {
				continue;
			}

			// There is already a hole in the right
			if (holeCol < BOARD_COLUMNS - 1 && board.piece(holeRow, holeCol + 1) instanceof Hole) {
				continue;
			}

			// Ok to place a Hole
			try {
				placeNewPiece(holeRow, holeCol, new Hole(board, Color.RED));
				i++;
			} catch (BoardException e) {
			}

		}

		// Bags
		for (int i = 0; i < BAGS_NUMBER;) {
			try {
				placeNewPiece(rand.nextInt(BOARD_ROWS), rand.nextInt(BOARD_COLUMNS), new Bag(board, Color.YELLOW));
				i++;
			} catch (BoardException e) {
			}
		}

		// Agent
		for (int i = 0; i < AGENTS_NUMBER;) {
			try {
				placeNewPiece(rand.nextInt(BOARD_ROWS), rand.nextInt(BOARD_COLUMNS), new Agent(board, Color.WHITE));
				i++;
			} catch (BoardException e) {
			}
		}
	}

}
