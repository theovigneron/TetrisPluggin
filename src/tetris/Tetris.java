package tetris;

import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import appli.interfaces.ITableau;
import appli.interfaces.ITetrisGame;


public class Tetris extends JFrame implements ITetrisGame {

	private ITableau tab;
	// D�claration d'une �tiquette pour le score
	private JLabel barreDeScore;

	public Tetris(ITableau plateau) {
		this.tab = plateau;
		// Cr�ation et ajout du plateau de jeu
		add((Component) plateau);
	}

	// M�thode pour r�cup�rer la barre de score
	public JLabel AfficheBarreDeScore() {
		return barreDeScore;
	}
	
	public void startGame() {
		// Lancement du jeu
		this.tab.start();
		// D�finition de la taille de la fen�tre
		setSize(200, 400);
		// D�finition du titre de la fen�tre
		setTitle("Mon tetris");
		// Fermeture de la fen�tre � la sortie
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// Positionnement de la fen�tre au centre de l'�cran
		setLocationRelativeTo(null);
		// Rendre la fen�tre visible
		setVisible(true);
	}

}
