package plugins;

import java.awt.Color;

import appli.interfaces.IForme;
import appli.interfaces.ITetronimos;
import tetris.Forme;

public class FormePlugins extends Forme implements IForme {

	public FormePlugins(ITetronimos tetronimos) {
		super(tetronimos);
	}
}
