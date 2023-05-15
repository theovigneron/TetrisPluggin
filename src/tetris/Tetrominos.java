package tetris;

import java.awt.Color;
import java.util.Random;

import appli.interfaces.ITetronimos;

public class Tetrominos implements ITetronimos {
    public static final ITetronimos NON_FORME = new Tetrominos(new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, new Color(0, 0, 0));
    public static final ITetronimos Z_FORME = new Tetrominos(new int[][] { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }, new Color(204, 102, 102));
    public static final ITetronimos S_FORME = new Tetrominos(new int[][] { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }, new Color(102, 204, 102));
    public static final ITetronimos LINE_FORME = new Tetrominos(new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }, new Color(102, 102, 204));
    public static final ITetronimos T_FORME = new Tetrominos(new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, new Color(204, 204, 102));
    public static final ITetronimos SQUARE_FORME = new Tetrominos(new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, new Color(204, 102, 204));
    public static final ITetronimos L_FORME = new Tetrominos(new int[][] { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(102, 204, 204));
    public static final ITetronimos MIRRORED_L_FORME = new Tetrominos(new int[][] { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(218, 170, 0));
    
    private int[][] coords;
    private Color color;

   
    
    public Tetrominos() {
		ITetronimos[] formes = getValues();
		Random random = new Random();
		int randomIndex = random.nextInt(formes.length);
		ITetronimos formeAleatoire = formes[randomIndex];
		this.coords = formeAleatoire.getCoords();
		this.color = formeAleatoire.getColor();
    }
    
    public Tetrominos(int[][] coords, Color c) {
        this.coords = coords;
        color = c;
    }

    public int[][] getCoords() {
        return coords;
    }

    public Color getColor() {
        return color;
    }

    public ITetronimos[] getValues() {
        return new ITetronimos[] { NON_FORME, Z_FORME, S_FORME, LINE_FORME, T_FORME, SQUARE_FORME, L_FORME, MIRRORED_L_FORME };
    }
}