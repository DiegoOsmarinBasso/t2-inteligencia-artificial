package ai;

import java.util.Random;

public class NeuralNetwork {

	private static Random rand = new Random();

	// Pesos Entrada
	private double w1e, w2e, w3e, w4e;
	private double w1c, w2c, w3c, w4c;
	private double w1d, w2d, w3d, w4d;
	private double w1b, w2b, w3b, w4b;

	// Pesos Camada Intemediaria
	private double w1, w2, w3, w4;
	private double we1, wc1, wd1, wb1;
	private double we2, wc2, wd2, wb2;
	private double we3, wc3, wd3, wb3;
	private double we4, wc4, wd4, wb4;

	// Pesos saidas
	private double we, wc, wd, wb;

	private int ponctuation = 0;

	public void randomInitialize() {
		w1e = rand.nextDouble();
		w2e = rand.nextDouble();
		w3e = rand.nextDouble();
		w4e = rand.nextDouble();

		w1c = rand.nextDouble();
		w2c = rand.nextDouble();
		w3c = rand.nextDouble();
		w4c = rand.nextDouble();

		w1d = rand.nextDouble();
		w2d = rand.nextDouble();
		w3d = rand.nextDouble();
		w4d = rand.nextDouble();

		w1b = rand.nextDouble();
		w2b = rand.nextDouble();
		w3b = rand.nextDouble();
		w4b = rand.nextDouble();

		w1 = rand.nextDouble();
		w2 = rand.nextDouble();
		w3 = rand.nextDouble();
		w4 = rand.nextDouble();

		we1 = rand.nextDouble();
		wc1 = rand.nextDouble();
		wd1 = rand.nextDouble();
		wb1 = rand.nextDouble();

		we2 = rand.nextDouble();
		wc2 = rand.nextDouble();
		wd2 = rand.nextDouble();
		wb2 = rand.nextDouble();

		we3 = rand.nextDouble();
		wc3 = rand.nextDouble();
		wd3 = rand.nextDouble();
		wb3 = rand.nextDouble();

		we4 = rand.nextDouble();
		wc4 = rand.nextDouble();
		wd4 = rand.nextDouble();
		wb4 = rand.nextDouble();

		we = rand.nextDouble();
		wc = rand.nextDouble();
		wd = rand.nextDouble();
		wb = rand.nextDouble();
	}

	public char getDirection(int xe, int xc, int xd, int xb) {
		// Entradas: xe - Esquerda, xc - Cima, xd - Direita, xb - Baixo
		// Valores: 1 - Parede, 2 - Buraco, 3 - Saco, 4 - Célula Livre, 5 - Porta

		// Valores Camada Intemediaria
		double v1 = w1 + xe * w1e + xc * w1c + xd * w1d + xb * w1b;
		double v2 = w2 + xe * w2e + xc * w2c + xd * w2d + xb * w2b;
		double v3 = w3 + xe * w3e + xc * w3c + xd * w3d + xb * w3b;
		double v4 = w4 + xe * w4e + xc * w4c + xd * w4d + xb * w4b;

		// Valores Camada Saida
		double ye = we + v1 * we1 + v2 * we2 + v3 * we3 + v4 * we4;
		double yc = wc + v1 * wc1 + v2 * wc2 + v3 * wc3 + v4 * wc4;
		double yd = wd + v1 * wd1 + v2 * wd2 + v3 * wd3 + v4 * wd4;
		double yb = wb + v1 * wb1 + v2 * wb2 + v3 * wb3 + v4 * wb4;

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

	public void addPoints(int points) {
		ponctuation += points;
	}

	public int getPoints() {
		return ponctuation;
	}

}
