package application;

import java.util.ArrayList;
import java.util.List;

import ai.NeuralNetwork;
import boardgame.BoardException;
import maze.MazeMatch;
import maze.pieces.Bag;

public class Program {

	private static final int POPULATION = 5;
	private static int generation = 0;

	public static void main(String[] args) {

		// Populacao Inicial
		NeuralNetwork[] neuralPopulation = new NeuralNetwork[POPULATION];

		for (int i = 0; i < POPULATION; i++) {
			neuralPopulation[i] = new NeuralNetwork();
			neuralPopulation[i].randomInitialize();
		}

		// while (!mazeMatch.getFinalizeMaze()) {

		for (int i = 0; i < POPULATION; i++) {

			MazeMatch mazeMatch = new MazeMatch();
			List<Bag> captured = new ArrayList<>();
			int heuristic = 0;

			UI.clearScreen();
			UI.printMatch(mazeMatch, captured);

			do {

				int left = mazeMatch.getAgentsNeighbor(0, -1);
				int up = mazeMatch.getAgentsNeighbor(-1, 0);
				int right = mazeMatch.getAgentsNeighbor(0, 1);
				int down = mazeMatch.getAgentsNeighbor(1, 0);
				
				char direction = neuralPopulation[i].getDirection(left, up, right, down);

				UI.clearScreen();
				System.out.println("Geracao " + generation + " - Cromossomo " + i);
				System.out.println("left: " + left);
				System.out.println("up: " + up);
				System.out.println("right: " + right);
				System.out.println("down: " + down);
				UI.printMatch(mazeMatch, captured);

				try {
					switch (direction) {
					case 'E':
						System.out.println("Movendo para Esquerda");
						heuristic = mazeMatch.performAgentMove(0, -1);
						break;
					case 'C':
						System.out.println("Movendo para Cima");
						heuristic = mazeMatch.performAgentMove(-1, 0);
						break;
					case 'D':
						System.out.println("Movendo para Direita");
						heuristic = mazeMatch.performAgentMove(0, 1);
						break;
					case 'B':
						System.out.println("Movendo para Baixo");
						heuristic = mazeMatch.performAgentMove(1, 0);
						break;
					}
				}
				// Caso tentar mover para fora do labirinto
				catch (BoardException e) {
					heuristic = 0;
				}

				// Movimento invalido, fim desta simulacao
				if (heuristic == 0) {
					System.out.println("\nMovimento invalido, a simulacao deste cromossomo terminou!");
				}
				// Movimento valido, soma pontos para esta rede neural
				else {
					neuralPopulation[i].addPoints(heuristic);
				}

				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} while (heuristic != 0);

		}

		/*
		 * UI.clearScreen(); 
		 * UI.printMatch(mazeMatch, captured);
		 * 
		 * System.out.println(); System.out.print("Source: "); Position source =
		 * UI.readPosition(sc); mazeMatch.validateSourcePosition(source);
		 * 
		 * System.out.println(); System.out.print("Target: "); Position target =
		 * UI.readPosition(sc); mazeMatch.validateTargetPosition(target);
		 * 
		 * Bag capturedBag = mazeMatch.performMazeMove(source, target);
		 * 
		 * if (capturedBag != null) { captured.add(capturedBag); }
		 */

		generation++;
		// }

		UI.clearScreen();
		for (int i = 0; i < POPULATION; i++) {
			System.out.println("Neural Population " + i + ": " + neuralPopulation[i].getPoints() + " pontos");
		}
		
	}
}
