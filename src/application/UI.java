package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import boardgame.Position;
import maze.Color;
import maze.MazeMatch;
import maze.MazePiece;
import maze.pieces.Bag;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	// Reset
	public static final String ANSI_RESET = "\u001B[0m";

	// Regular Colors
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	// Bold High Intensity
	public static final String BLACK_BOLD_BRIGHT = "\033[1;90m";
	public static final String RED_BOLD_BRIGHT = "\033[1;91m";
	public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
	public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";
	public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";
	public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";
	public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";
	public static final String WHITE_BOLD_BRIGHT = "\033[1;97m";

	// Background
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static Position readPosition(Scanner sc) {
		try {
			String s = sc.nextLine();
			int row = Integer.parseInt(s.substring(0, 1));
			int column = Integer.parseInt(s.substring(1, 2));

			return new Position(row, column);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Error reading MazePosition. Valid values are from 00 to 99.");
		}
	}

	private static void printPiece(MazePiece piece) {
		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			Color color = piece.getColor();
			if (color == Color.CYAN) {
				System.out.print(ANSI_CYAN_BACKGROUND + piece + ANSI_RESET);
			} else if (color == Color.GREEN) {
				System.out.print(ANSI_GREEN_BACKGROUND + piece + ANSI_RESET);
			} else if (color == Color.RED) {
				System.out.print(RED_BOLD_BRIGHT + piece + ANSI_RESET);
			} else if (color == Color.YELLOW) {
				System.out.print(YELLOW_BOLD_BRIGHT + piece + ANSI_RESET);
			} else if (color == Color.WHITE) {
				System.out.print(WHITE_BOLD_BRIGHT + piece + ANSI_RESET);
			}
		}
	}

	public static void printMatch(MazeMatch chessMatch, List<Bag> captured) {

		printTabBoard(chessMatch.getPieces());

		printCapturedBags(captured);
		System.out.println();

		if (chessMatch.getFinalizeMaze()) {
			System.out.println("MAZE OVER!");
		}
	}

	public static void printTabBoard(MazePiece[][] pieces) {
		int rows = pieces.length;
		int columns = pieces[0].length;

		System.out.println("\n    0   1   2   \n");
		for (int i = 0; i < rows; i++) {
			System.out.print(i + "   ");
			for (int j = 0; j < columns; j++) {
				printPiece(pieces[i][j]);
				System.out.print("   ");
			}
			System.out.print(i + "\n\n");
		}
		System.out.println("    0   1   2   \n");
	}

	private static void printCapturedBags(List<Bag> captured) {
		System.out.println("Captured Bags: " + YELLOW_BOLD_BRIGHT + Arrays.toString(captured.toArray()) + ANSI_RESET);
	}

}
