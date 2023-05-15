package appli.interfaces;

import java.awt.Color;

import tetris.Forme;

public interface IForme {
		
	public void fabriqueForme(ITetronimos forme);
	public int x(int index);
	public int y(int index);
	public ITetronimos getForme();
	public void formeRandom();
	public int minY();
	public void setX(int index, int x);
	public void setY(int index, int y);
	public IForme rotationGauche();
	public IForme rotateDroite();
	
}
