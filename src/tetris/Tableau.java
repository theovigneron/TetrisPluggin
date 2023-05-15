package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import appli.interfaces.IForme;
import appli.interfaces.ITableau;
import appli.interfaces.ITetronimos;

public class Tableau extends JPanel implements ActionListener, KeyListener,ITableau {

	// D�finition des dimensions du plateau de jeu
	protected static final int LongeurTableau = 10;
	protected static final int HauteurTableau = 22;

	protected Timer timer; // Timer utilis� pour faire tomber les pi�ces
	protected boolean enChute = false; // Indicateur de fin de chute de la pi�ce
	protected boolean enCour = false; // Indicateur de d�but de jeu
	protected boolean enPause = false; // Indicateur de pause du jeu
	protected int score = 0; // Compteur du nombre de lignes supprim�es
	protected int curX = 0; // Position X de la pi�ce actuelle
	protected int curY = 0; // Position Y de la pi�ce actuelle
	protected JLabel barreDeScore; // Barre de score
	protected IForme pieceActuelle; // Pi�ce actuelle
	protected ITetronimos[] board; // Tableau repr�sentant le plateau de jeu

	public Tableau(IForme forme) {
		
		barreDeScore = new JLabel("0");
		// Ajout de l'�tiquette en bas de la fen�tre
		add(barreDeScore, BorderLayout.SOUTH);
		setFocusable(true); // Rend le composant focusable, permet de faire des actions dessus
		pieceActuelle = forme; // Cr�e une nouvelle pi�ce
		timer = new Timer(400, this); // Initialise le timer pour faire tomber les pi�ces
		board = new ITetronimos[LongeurTableau * HauteurTableau]; // Initialise le plateau de jeu
		clearBoard(); // Vide le plateau de jeu
		addKeyListener(this); // Ajoute un �couteur de touches
	}

	// Calcule la largeur des cases
	public int squareWidth() {
		return (int) getSize().getWidth() / LongeurTableau;
	}

	// Calcule la hauteur des cases
		public int squareHeight() {
		return (int) getSize().getHeight() / HauteurTableau;
	}

	// Retourne la forme de la pi�ce � la position sp�cifi�e
	public ITetronimos formeAt(int x, int y) {
		return board[y * LongeurTableau + x];
	}

	// Vide le plateau de jeu
	public void clearBoard() {
		for (int i = 0; i < HauteurTableau * LongeurTableau; i++) {
			board[i] = ITetronimos.getNonForme();
		}
	}
	
	// Cette fonction est appel�e lorsque la pi�ce atteint le bas du plateau ou touche une autre pi�ce
	public void pieceDropped() {
	    // On parcourt les 4 carr�s de la pi�ce en cours
	    for (int i = 0; i < 4; i++) {
	        // On calcule les coordonn�es x et y du carr� en question
	        int x = curX + pieceActuelle.x(i);
	        int y = curY - pieceActuelle.y(i);
	        // On place le carr� dans le plateau
	        board[y * LongeurTableau + x] = pieceActuelle.getForme();
	    }
	    // On v�rifie s'il y a des lignes pleines � supprimer
	    SuppLigne();
	    // Si la chute est termin�e (la pi�ce ne peut plus descendre), on en g�n�re une nouvelle
	    if (!enChute) {
	        nouvellePiece();
	    }
	}

	// Cette fonction g�n�re une nouvelle pi�ce al�atoire
	public void nouvellePiece() {
	    pieceActuelle.formeRandom(); // On d�finit la forme de la nouvelle pi�ce
	    curX = LongeurTableau / 2 + 1; // On place la pi�ce au centre de la premi�re ligne
	    curY = HauteurTableau - 1 + pieceActuelle.minY(); // On place la pi�ce tout en haut du plateau
	    // Si la nouvelle pi�ce ne peut pas �tre plac�e correctement (ie si elle touche une autre pi�ce), c'est la fin de la partie
	    if (!Mouvement(pieceActuelle, curX, curY - 1)) {
	        pieceActuelle.fabriqueForme(ITetronimos.getNonForme()); // On indique que la pi�ce n'a pas de forme
	        timer.stop(); // On arr�te le timer
	        enCour = false; // On indique que la partie est termin�e
	        barreDeScore.setText("Game Over"); // On affiche un message de fin de partie
	    }
	}

	// Cette fonction fait descendre la pi�ce d'une ligne
	public void oneLineDown() {
	    // Si la pi�ce ne peut pas descendre, c'est qu'elle est arriv�e en bas, donc on appelle la fonction pieceDropped
	    if (!Mouvement(pieceActuelle, curX, curY - 1)) {
	        pieceDropped();
	    }
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		// V�rifie si la pi�ce en cours est tomb�e au fond du plateau
		if (enChute) {
			// Si oui, on cr�e une nouvelle pi�ce
			enChute = false;
			nouvellePiece();
		} else {
			// Sinon, on d�place la pi�ce d'une ligne vers le bas
			oneLineDown();
		}
	} 

	// Dessine un carr� � la position (x, y) avec la forme donn�e
	public void drawSquare(Graphics g, int x, int y, ITetronimos forme) {
		Color color = forme.getColor();
		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);
		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - HauteurTableau * squareHeight();
		
		// Dessine les carr�s du plateau
		for (int i = 0; i < HauteurTableau; i++) {
			for (int j = 0; j < LongeurTableau; ++j) {
				ITetronimos forme = formeAt(j, HauteurTableau - i - 1);
				if (forme != ITetronimos.getNonForme()) {
					drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), forme);
				}
			}
		}
		
		// Dessine la pi�ce en cours
		if (pieceActuelle.getForme() != ITetronimos.getNonForme()) {
			for (int i = 0; i < 4; ++i) {
				int x = curX + pieceActuelle.x(i);
				int y = curY - pieceActuelle.y(i);
				drawSquare(g, x * squareWidth(), boardTop + (HauteurTableau - y - 1) * squareHeight(), pieceActuelle.getForme());
			}
		}
	}
	
	public void start() {
	    // Si le jeu est en pause, on ne fait rien
	    if (enPause)
	        return;
	    
	    // Le jeu est d�marr�, on r�initialise les variables
	    enCour = true;
	    enChute = false;
	    score = 0;
	    
	    // On efface le plateau et on ajoute une nouvelle pi�ce
	    clearBoard();
	    nouvellePiece();
	    
	    // On lance le timer pour faire tomber les pi�ces
	    timer.start();
	}

	public void pause() {
	    // Si le jeu n'est pas encore d�marr�, on ne fait rien
	    if (!enCour)
	        return;
	    
	    // On inverse le statut de la pause
	    enPause = !enPause;
	    
	    // Si le jeu est en pause, on arr�te le timer et on affiche le message
	    if (enPause) {
	        timer.stop();
	        barreDeScore.setText("Paused");
	    } else {
	        // Si le jeu est en marche, on reprend le timer et on affiche le nombre de lignes supprim�es
	        timer.start();
	        barreDeScore.setText(String.valueOf(score));
	    }
	    
	    // On redessine le plateau
	    repaint();
	}

	public boolean Mouvement(IForme pieceActuelle2, int newX, int newY) {
	    // On v�rifie si la nouvelle position de la pi�ce est valide
	    for (int i = 0; i < 4; ++i) {
	        int x = newX + pieceActuelle2.x(i);
	        int y = newY - pieceActuelle2.y(i);
	        
	        // Si la nouvelle position est en dehors du plateau, on retourne false
	        if (x < 0 || x >= LongeurTableau || y < 0 || y >= HauteurTableau)
	            return false;
	        
	        // Si la case est d�j� occup�e par une autre pi�ce, on retourne false
	        if (formeAt(x, y) != ITetronimos.getNonForme())
	            return false;
	    }
	    // La nouvelle position est valide, on met � jour la position de la pi�ce courante et on redessine le plateau
	    pieceActuelle = pieceActuelle2;
	    curX = newX;
	    curY = newY;
	    repaint();
	    
	    return true;
	}
	
	public void SuppLigne() {
	    int NumLigneComplete = 0;

	    // Parcourt toutes les lignes du tableau de bas en haut
	    for (int i = HauteurTableau - 1; i >= 0; --i) {
	        boolean LigneComplete = true;

	        // V�rifie si la ligne est compl�te en parcourant toutes les colonnes
	        for (int j = 0; j < LongeurTableau; ++j) {
	            if (formeAt(j, i) == ITetronimos.getNonForme()) {
	                LigneComplete = false;
	                break;
	            }
	        }

	        // Si la ligne est compl�te, incr�mente le nombre de lignes compl�tes
	        // D�cale toutes les lignes au-dessus de la ligne compl�te d'une case vers le bas
	        if (LigneComplete) {
	            ++NumLigneComplete;

	            for (int k = i; k < HauteurTableau - 1; ++k) {
	                for (int j = 0; j < LongeurTableau; ++j) {
	                    board[k * LongeurTableau + j] = formeAt(j, k + 1);
	                }
	            }
	        }

	        // Si au moins une ligne a �t� compl�t�e, met � jour le score, affiche le score, red�finit la pi�ce actuelle
	        if (NumLigneComplete > 0) {
	            score += NumLigneComplete;
	            barreDeScore.setText(String.valueOf(score));
	            enChute = true;
	            pieceActuelle.fabriqueForme(ITetronimos.getNonForme());
	            repaint();
	        }
	    }
	}

	// D�place la pi�ce actuelle vers le bas jusqu'� ce qu'elle atteigne le bas du tableau ou rencontre une autre pi�ce
	public void ChuteEnBas() {
	    int newY = curY;

	    while (newY > 0) {
	        if (!Mouvement(pieceActuelle, curX, newY - 1))
	            break;

	        --newY;
	    }
	    pieceDropped();
	}

	//Action en fonction des saisie clavier
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
			Mouvement(pieceActuelle, curX - 1, curY);
			break;
			case KeyEvent.VK_RIGHT:
			Mouvement(pieceActuelle, curX + 1, curY);
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
	@Override
    public void keyReleased(KeyEvent e) {
        //pas de traitement au rel�chement d'une touche 
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //pas de traitement lors de la frappe d'une touche
    }
}