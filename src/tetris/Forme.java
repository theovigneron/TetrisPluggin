package tetris;

import java.awt.Color;
import java.util.Random;

import appli.interfaces.IForme;
import appli.interfaces.ITetronimos;

public class Forme implements IForme {

	private ITetronimos pieceforme;
	private int[][] coords;
	private ITetronimos tetrominos;

	public Forme(ITetronimos tetrominos) {
		coords = new int[4][2];
		fabriqueForme(ITetronimos.getNonForme());
		this.tetrominos = tetrominos;
	}

	public void fabriqueForme(ITetronimos forme) {
		// Copie les coordonn�es de la forme donn�e dans le tableau "coords"
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; ++j) {
				coords[i][j] = forme.getCoords()[i][j];
			}
		}

		// Stocke la forme de la pi�ce actuelle
		pieceforme = forme;
	}

	public void setX(int index, int x) {
		// Modifie la coordonn�e "x" de la forme � l'index donn�
		coords[index][0] = x;
	}

	public void setY(int index, int y) {
		// Modifie la coordonn�e "y" de la forme � l'index donn�
		coords[index][1] = y;
	}

	public int x(int index) {
		// Renvoie la coordonn�e "x" de la forme � l'index donn�
		return coords[index][0];
	}

	public int y(int index) {
		// Renvoie la coordonn�e "y" de la forme � l'index donn�
		return coords[index][1];
	}

	public ITetronimos getForme() {
		// Renvoie la forme actuelle de la pi�ce
		return pieceforme;
	}

	public void formeRandom() {
		// On cr�e un objet Random pour g�n�rer un nombre al�atoire
		Random r = new Random();
		// On r�cup�re toutes les formes possibles
		ITetronimos[] values = this.tetrominos.getValues();
		// On g�n�re un nombre al�atoire entre 0 et 6 (inclus)
		int x = Math.abs(r.nextInt()) % values.length;
		x = x == 0? x+1 : x;
		// On d�finit une forme al�atoire
		fabriqueForme(values[x]);
	}

	public int minY() {
		// On d�finit la position y minimale du tetrominos
		int m = coords[0][1];
		// On it�re sur toutes les coordonn�es pour trouver la valeur minimale
		for (int i = 0; i < 4; i++) {
			m = Math.min(m, coords[i][1]);
		}
		return m;
	}

	public IForme rotationGauche() {
		// On cr�e un nouveau tetrominos
		IForme result = new Forme(this.tetrominos);
		// On d�finit la forme du nouveau tetrominos
		result.fabriqueForme(pieceforme);
		// On effectue la rotation de chaque coordonn�e vers la gauche
		for (int i = 0; i < 4; i++) {
			result.setX(i, y(i));
			result.setY(i, -x(i));
		}
		return result;
	}

	public IForme rotateDroite() {
		// On cr�e un nouveau tetrominos
		IForme result = new Forme(this.tetrominos);
		// On d�finit la forme du nouveau tetrominos
		result.fabriqueForme(pieceforme);
		// On effectue la rotation de chaque coordonn�e vers la droite
		for (int i = 0; i < 4; i++) {
			result.setX(i, -y(i));
			result.setY(i, x(i));
		}
		return result;
	}
}
