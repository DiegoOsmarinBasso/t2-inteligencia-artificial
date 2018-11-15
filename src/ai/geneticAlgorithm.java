package ai;

import application.Program;

public class geneticAlgorithm {

	private static final int MUTATION = 10;

	public static NeuralNetwork[] nextGeneration(NeuralNetwork[] old) {

		int population = old.length;
		NeuralNetwork[] next = new NeuralNetwork[population];
		int elite = 0;

		// Obtem elite e grava no novo vetor de redes neurais
		for (int i = 1; i < population; i++) {
			if (old[i].getPoints() > old[elite].getPoints()) {
				elite = i;
			}
		}
		next[0] = new NeuralNetwork(old[elite].getWs());

		System.out.println("Elite posicao " + elite + " --> pontuacao: " + old[elite].getPoints());

		// Realiza Crossover e Mutacao
		for (int i = 1; i < population; i++) {
			int m, f;

			// sorteia dois, escolhe melhor para ser a mae
			int a = Util.rand.nextInt(population);
			int b = Util.rand.nextInt(population);

			m = old[a].getPoints() > old[b].getPoints() ? a : b;

			// sorteia dois, escolhe melhor para ser o pai
			a = Util.rand.nextInt(population);
			b = Util.rand.nextInt(population);

			f = old[a].getPoints() > old[b].getPoints() ? a : b;

			// cruza
			double[] s = new double[NeuralNetwork.WEIGHTS];
			for (int j = 0; j < NeuralNetwork.WEIGHTS; j++) {
				s[j] = (old[m].getW(j) + old[f].getW(j)) / 2;
			}

			// Mutacao com probabilidade MUTATION
			while (Util.rand.nextInt(100) < MUTATION) {
				int pos = Util.rand.nextInt(NeuralNetwork.WEIGHTS);

				System.out.println("\nANTES MUTACAO s[" + pos + "] = " + s[pos]);
				s[pos] = 2 * (Util.rand.nextDouble() - 0.5);
				System.out.println("APOS MUTACAO s[" + pos + "] = " + s[pos]);
				Program.sleep(1);
			}

			next[i] = new NeuralNetwork(s);
		}

		return next;
	}
}
