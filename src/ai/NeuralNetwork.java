package ai;

import application.Program;

public class NeuralNetwork {

	public static final int WEIGHTS = 40;
	private static final int LAYER_SIZE = 4;
	private double[] w = new double[WEIGHTS];
	private char lastMove = 'X';
	private int ponctuation = 0;

	// Populacao inicial randomizada
	public NeuralNetwork() {
		// Pesos Entrada:
		// E - 00 a 04 --> w10, w11, w12, w13, w14
		// C - 05 a 09
		// D - 10 a 14
		// B - 15 a 19

		// Pesos Saida:
		// E - 20 a 24 --> w50, w51, w52, w53, w54
		// C - 25 a 29
		// D - 30 a 34
		// B - 35 a 39
		for (int i = 0; i < WEIGHTS; i++) {
			w[i] = 2 * (Util.rand.nextDouble() - 0.5);
		}
	}
	
	// Populacao passada por parametro
	public NeuralNetwork(double[] w) { 
		for (int i = 0; i < WEIGHTS; i++) {
			this.w[i] = w[i];
		}
	}
	
	public double getW(int pos) {
		return w[pos];
	}
	
	public double[] getWs() {
		return w;
	}
	
	public char getDirection(int xe, int xc, int xd, int xb) {
		// Entradas: xe - Esquerda, xc - Cima, xd - Direita, xb - Baixo
		// Valores: 1 - Parede, 2 - Buraco, 3 - Saco, 4 - Célula Livre, 5 - Porta

		// Valores Camada Intemediaria
		double[] v = new double[LAYER_SIZE];
		for (int i = 0; i < LAYER_SIZE; i++) {
			int j = i * LAYER_SIZE;
			v[i] = Math.tanh(w[j] + xe * w[j + 1] + xc * w[j + 2] + xd * w[j + 3] + xb * w[j + 4]);
		}

		// Valores Camada Saida
		double[] y = new double[LAYER_SIZE];
		for (int i = 0; i < LAYER_SIZE; i++) {
			int j = (i * LAYER_SIZE) + (WEIGHTS / 2);
			y[i] = w[j] + v[0] * w[j + 1] + v[1] * w[j + 2] + v[2] * w[j + 3] + v[3] * w[j + 4];
		}

		double ye = y[0], yc = y[1], yd = y[2], yb = y[3];
		System.out.println("yE " + ye);
		System.out.println("yC " + yc);
		System.out.println("yD " + yd);
		System.out.println("yB " + yb);
		Program.sleep(2);

		switch (lastMove) {
		case 'E':
			lastMove = greater(ye, yc, Double.NEGATIVE_INFINITY, yb);
			break;
		case 'C':
			lastMove = greater(ye, yc, yd, Double.NEGATIVE_INFINITY);
			break;
		case 'D':
			lastMove = greater(Double.NEGATIVE_INFINITY, yc, yd, yb);
			break;
		case 'B':
			lastMove = greater(ye, Double.NEGATIVE_INFINITY, yd, yb);
			break;
		default:
			lastMove = greater(ye, yc, yd, yb);
		}

		return lastMove;
	}

	public void addPoints(int points) {
		ponctuation += points;
	}

	public int getPoints() {
		return ponctuation;
	}
	

	public void resetPoints() {
		ponctuation = 0;
	}

	private char greater(double ye, double yc, double yd, double yb) {
		// Saidas: E - Esquerda
		if (ye > yc && ye > yd && ye > yb) {
			return 'E';
		}
		// C - Cima
		else if (yc > yd && yc > yb) {
			return 'C';
		}
		// D - Direita
		else if (yd > yb) {
			return 'D';
		}
		// B - Baixo
		else {
			return 'B';
		}
	}

}
