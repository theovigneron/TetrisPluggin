package plugins;

import appli.interfaces.ITetrisDisplay;
import appli.interfaces.ITetrisGame;

public class TetrisGame implements ITetrisGame{

	ITetrisDisplay tetrisDisplay;

	public TetrisGame(ITetrisDisplay tetrisDisplay) {
		this.tetrisDisplay = tetrisDisplay;
	}
}
