package application;

import java.util.ArrayList;
import java.util.List;

import ai.NeuralNetwork;
import ai.geneticAlgorithm;
import boardgame.BoardException;
import maze.MazeMatch;
import maze.pieces.Bag;

public class Program {

	private static final int POPULATION = 10;
	private static final int MAX_GENERATION = 3000;
	private static final int SHOW = 300;
	private static final int PACE = 500;
	private static int MILIS = 0;
	private static int generation = 0;
	private static int theEnd = -1;

	public static void main(String[] args) {

		/*
		 * INSTANCIA JA CRAINDO A POPULACAO INICIAL
		 */
		NeuralNetwork[] neuralPopulation = new NeuralNetwork[POPULATION];
		for (int i = 0; i < POPULATION; i++) {
			neuralPopulation[i] = new NeuralNetwork();
		}

		/*
		 * OBTEM HEURISTICAS E RODA ALGORITMO GENETICO
		 */
		while (generation < MAX_GENERATION && theEnd < 0) {

			/*
			 * OBTEM AS HEURISTICAS DA POPULACAO DESTA GERACAO
			 */
			for (int i = 0; i < POPULATION; i++) {
				// Aumenta sleep se for elite e multiplo de SHOW
				MILIS = i == 0 && generation % SHOW == 0 ? PACE : 0;

				// Simula obtendo heuristicas
				simulatePopulation(neuralPopulation[i], i);
			}

			/*
			 * SE NAO TERMINOU OBTEM A PROXIMA POPULACAO UTILIZANDO AS HEURISTICAS PARA O
			 * ALGORITMO GENETICO
			 */
			if (theEnd < 0) {
				MILIS = generation % SHOW == 0 ? PACE : 0;

				for (int i = 0; i < POPULATION; i++) {
					System.out.println("Populacao " + i + ": " + neuralPopulation[i].getPoints() + " pontos");
				}

				System.out.println("Gerando proxima populacao");
				sleep(3);
				neuralPopulation = geneticAlgorithm.nextGeneration(neuralPopulation);
				generation++;
			}
			/*
			 * SE TERMINOU MOSTRAR EXECUCAO DE MAIOR HEURISTICA
			 */
			else {
				MILIS = PACE;

				// Mostra simulacao final
				neuralPopulation[theEnd].resetPoints();
				simulatePopulation(neuralPopulation[theEnd], theEnd);
			}

		}

	}

	/*
	 * SIMULA HEURISTICAS DA POPULACAO PASSADA POR PARAMETRO
	 */
	private static void simulatePopulation(NeuralNetwork neuralNetwork, int index) {

		MazeMatch mazeMatch = new MazeMatch();
		List<Bag> captured = new ArrayList<>();
		int heuristic = 0;

		do {
			int left = mazeMatch.getAgentsNeighbor(0, -1);
			int up = mazeMatch.getAgentsNeighbor(-1, 0);
			int right = mazeMatch.getAgentsNeighbor(0, 1);
			int down = mazeMatch.getAgentsNeighbor(1, 0);

			printMazeStatus(mazeMatch, captured, left, up, right, down, index, neuralNetwork.getPoints());

			char direction = neuralNetwork.getDirection(left, up, right, down);

			sleep(1);

			try {
				switch (direction) {
				case 'E':
					System.out.println("Movendo para Esquerda");
					heuristic = mazeMatch.performAgentMove(0, -1, captured);
					if (heuristic == 100) {
						sleep(1);
						printMazeStatus(mazeMatch, captured, 0, 0, 0, 0, index, neuralNetwork.getPoints());
					}
					break;
				case 'C':
					System.out.println("Movendo para Cima");
					heuristic = mazeMatch.performAgentMove(-1, 0, captured);
					break;
				case 'D':
					System.out.println("Movendo para Direita");
					heuristic = mazeMatch.performAgentMove(0, 1, captured);
					break;
				case 'B':
					System.out.println("Movendo para Baixo");
					heuristic = mazeMatch.performAgentMove(1, 0, captured);
					break;
				}
			}
			// Caso tentar mover para fora do labirinto
			catch (BoardException e) {
				heuristic = 0;
			}

			sleep(1);

			// Movimento invalido, fim desta simulacao
			if (heuristic == 0) {
				System.out.println("\nMovimento invalido, a simulacao deste cromossomo terminou!");
			}
			// Movimento valido, soma pontos para esta rede neural
			else {
				neuralNetwork.addPoints(heuristic);
				System.out.println("\nHeuristica " + heuristic);
				System.out.println("Pontuacao " + neuralNetwork.getPoints());
			}

			sleep(3);

		} while (heuristic != 0 && !mazeMatch.isTheEnd());

		if (mazeMatch.isTheEnd()) {
			theEnd = index;
		}

	}

	private static void printMazeStatus(MazeMatch mazeMatch, List<Bag> captured, int left, int up, int right, int down,
			int index, int points) {

		UI.clearScreen();
		System.out.println("Geracao " + generation + " - Cromossomo " + index);
		System.out.println("esq  : " + left);
		System.out.println("cima : " + up);
		System.out.println("dir  : " + right);
		System.out.println("baixo: " + down);
		UI.printMatch(mazeMatch, captured);
		System.out.println("Pontuacao " + points + "\n");
	}

	/*
	 * SLEEP PARA AJUDAR NO DEBUG
	 */
	public static void sleep(int sec) {
		try {
			if (sec < 0) {
				Thread.sleep(-sec * 1000);
			} else {
				Thread.sleep(sec * MILIS);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
