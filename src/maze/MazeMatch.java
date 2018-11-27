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
	private Agent agent;
	private Position agentPosition;
	private boolean theEnd;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public MazeMatch() {
		board = new Board(BOARD_ROWS, BOARD_COLUMNS);
		agent = new Agent(board, Color.WHITE);
		theEnd = false;
		InitialSetup();
	}

	public Agent getAgent() {
		return agent;
	}
	
	public boolean isTheEnd() {
		return theEnd;
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

	public Piece performMazeMove(Position source, Position target) {
		Agent a = (Agent) board.removePiece(source);
		Piece piece = board.removePiece(target);

		if (piece instanceof Door) {
			theEnd = true;
		}

		if (piece instanceof Bag) {
			piecesOnTheBoard.remove((Bag) piece);
			capturedPieces.add((Bag) piece);
		}

		board.placePiece(a, target);
		return piece;
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

	public int getAgentsNeighbor(int i, int j) {
		int row = agentPosition.getRow() + i;
		int column = agentPosition.getColumn() + j;

		// Se fora do mapa retorna 1 - Parede
		if (row < 0 || row >= BOARD_ROWS || column < 0 || column >= BOARD_COLUMNS) {
			return 1;
		}

		// Obtem a peca da posicao
		Piece piece = board.piece(new Position(row, column));

		// 1 - Parede
		if (piece instanceof Wall) {
			return 1;
		}

		// 2 - Celula em branco
		if (piece == null) {
			return 2;
		}

		// 3 - Buraco
		if (piece instanceof Hole) {
			return 3;
		}

		// 4 - Saco de moedas
		if (piece instanceof Bag) {
			return 4;
		}

		// 5 - Porta
		if (piece instanceof Door) {
			return 5;
		}

		// Caso base, nao deve acontecer
		return 0;
	}

	public int performAgentMove(int i, int j, List<Bag> captured) {

		int points = 0;
		int row = agentPosition.getRow() + i;
		int column = agentPosition.getColumn() + j;
		Position target = new Position(row, column);

		Piece piece = board.piece(target);

		// 1 - Parede: nao adiciona pontos
		if (piece instanceof Wall) {
			points = 0;
		}

		// 2 - Celula em branco: adiciona 1 ponto
		if (piece == null) {
			points = 1;
		}

		// 3 - Buraco: pular buraco adiciona 3 pontos
		if (piece instanceof Hole) {
			row = agentPosition.getRow() + 2 * i;
			column = agentPosition.getColumn() + 2 * j;
			target = new Position(row, column);

			points = 3;

			piece = board.piece(target);
		}

		// 4 - Saco de moedas: adiciona 10 pontos
		if (piece instanceof Bag) {
			captured.add((Bag) piece);
			points += 10;
		}

		// 5 - Porta: adiciona 100 pontos
		if (piece instanceof Door) {
			points += 100;
		}

		validateTargetPosition(target);

		performMazeMove(agentPosition, target);
		agentPosition.setRow(target.getRow());
		agentPosition.setColumn(target.getColumn());

		return points;
	}

	private void InitialSetup() {

		// Agent Position
		agentPosition = new Position(0, 0);

		// Line 0
		placeNewPiece(0, 0, agent);
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
