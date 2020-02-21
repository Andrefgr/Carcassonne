package tps.tp4.carcassonne;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * 
 * A classe EmptyTile, derivada de Place, destina-se a mostrar as quadrículas
 * vazias, definindo por isso a cor de fundo das mesmas.
 * 
 * @author Andre ROG
 *
 */
public class EmptyTile extends Place {

	private static final long serialVersionUID = 1L;
	private boolean put;

	EmptyTile( int x, int y) {
		super( x, y);
		put = false;
		setBackColor(new Color(217, 171, 102));
//		setIcon(new ImageIcon(getClass().getResource("img/bg.png")));
	}

	public boolean getPut() {
		return put;
	}

	public void setPut(boolean put) {
		this.put = put;
	}

	public void setBackColor(Color c) {
		this.setBackground(c);
	}

	@Override
	public boolean isEmptyTile() {
		return true;
	}

	@Override
	public String toString() {
		return "Empty";
	}

}
