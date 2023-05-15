package plugins;

import java.awt.Color;
import java.util.Random;

import appli.interfaces.ITetronimos;
import tetris.Tetrominos;

public class TetronimosPlugins implements ITetronimos{
	 public static final ITetronimos NON_FORME = new TetronimosPlugins(new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, new Color(0, 0, 0));
	 public static final ITetronimos RAND_FORME1 = new TetronimosPlugins(new int[][] { { -2, 0 },{ -1, 0 }, { 0, 1 }, { 0, 2 }, }, new Color(204, 0, 204));
	 public static final ITetronimos RAND_FORME2 = new TetronimosPlugins(new int[][] { { -1, 2 },{ 0, 0 }, { 0, -1 }, { 0, 1 }, }, new Color(0, 102, 204));
	 public static final ITetronimos RAND_FORME3 = new TetronimosPlugins(new int[][] { { -1, 1 },{ 0, 1 }, { 0, -1 }, { 0, -2 }, }, new Color(204, 102, 0));
  
	 private int[][] coords;
	 private Color color;
	
	public TetronimosPlugins() {
		ITetronimos[] formes = getValues();
		Random random = new Random();
		int randomIndex = random.nextInt(formes.length);
		ITetronimos formeAleatoire = formes[randomIndex];
		this.coords = formeAleatoire.getCoords();
		this.color = formeAleatoire.getColor();
	}
	    
	public TetronimosPlugins(int[][] coords, Color c) {
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
		return new ITetronimos[] { NON_FORME, RAND_FORME1, RAND_FORME2, RAND_FORME3 };
	}
}
