package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import boardgame.Position;
import maze.MazeException;
import maze.MazeMatch;
import maze.pieces.Bag;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		MazeMatch mazeMatch = new MazeMatch();
		List<Bag> captured = new ArrayList<>();

		while (!mazeMatch.getFinalizeMaze()) {
			try {
				UI.clearScreen();
				UI.printMatch(mazeMatch, captured);

				System.out.println();
				System.out.print("Source: ");
				Position source = UI.readPosition(sc);
				mazeMatch.validateSourcePosition(source);

				System.out.println();
				System.out.print("Target: ");
				Position target = UI.readPosition(sc);
				mazeMatch.validateTargetPosition(target);

				Bag capturedBag = mazeMatch.performMazeMove(source, target);

				if (capturedBag != null) {
					captured.add(capturedBag);
				}

			} catch (MazeException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(mazeMatch, captured);
	}
}
