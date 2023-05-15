package appli.interfaces;

import java.awt.Color;

import tetris.Tetrominos;

public interface ITetronimos {
    public static final ITetronimos NON_FORME = new Tetrominos(new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, new Color(0, 0, 0));


    public Color getColor();
    public int[][] getCoords();
    ITetronimos[] getValues();
    
    public static ITetronimos getNonForme() {
        return NON_FORME;
    }

}
