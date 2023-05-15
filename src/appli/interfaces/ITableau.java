package appli.interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.Timer;

import tetris.Forme;
import tetris.Tetris;

public interface ITableau {

	public int squareWidth();
	public int squareHeight();
	public ITetronimos formeAt(int x, int y);
	public void clearBoard();
	public void pieceDropped();
	public void nouvellePiece() ;
	public void oneLineDown();
	public void actionPerformed(ActionEvent ae);
	public void drawSquare(Graphics g, int x, int y, ITetronimos forme);
	public void paint(Graphics g);
	public void start();
	public void pause();
	public boolean Mouvement(IForme pieceActuelle2, int newX, int newY);
	public void SuppLigne();
	public void ChuteEnBas();
	public void keyPressed(KeyEvent ke);
    public void keyReleased(KeyEvent e);
    public void keyTyped(KeyEvent e) ;
}
