package plugins;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import appli.interfaces.IForme;
import appli.interfaces.ITableau;
import appli.interfaces.ITetronimos;
import tetris.Tableau;

public class TableauInverse extends Tableau implements ActionListener,ITableau{

	public TableauInverse(IForme forme) {
		super(forme);
		timer = new Timer(100, this); // Initialise le timer pour faire tomber les pi�ces
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
	    if (!enCour || pieceActuelle.getForme() == ITetronimos.getNonForme())
			return;
			
			int keyCode = ke.getKeyCode();
			
			if (keyCode == 'p' || keyCode == 'P')
			pause();
			
			if (enPause)
			return;
			
			switch (keyCode) {
				case KeyEvent.VK_LEFT:
				Mouvement(pieceActuelle, curX + 1, curY);
				break;
				case KeyEvent.VK_RIGHT:
				Mouvement(pieceActuelle, curX - 1, curY);
				break;
				case KeyEvent.VK_DOWN:
				Mouvement(pieceActuelle.rotateDroite(), curX, curY);
				break;
				case KeyEvent.VK_UP:
				Mouvement(pieceActuelle.rotationGauche(), curX, curY);
				break;
				case KeyEvent.VK_SPACE:
				ChuteEnBas();
				break;
				case 'd':
				case 'D':
				oneLineDown();
				break;
			}
	   
	}
		
	
}
