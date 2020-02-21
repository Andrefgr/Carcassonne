package tps.tp4.carcassonne;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

/**
 * 
 * A classe abstrata Place permite criar o tabuleiro como um array de c�lulas
 * desta classe. Deve conter o seu x e y no tabuleiro, definidos no seu
 * construtor e ter m�todos para os obter. Deve ter um m�todo abstrato
 * isEmptyTile(), que devolver� true caso seja um EmptyTile.
 * 
 * @author Andre ROG
 *
 */
public abstract  class Place extends JLabel {
	private static final long serialVersionUID = 1L;

	private int px, py;

	public Place(int x, int y) {
//		super(text);
		setBoardX(x);
		setBoardY(y);
		this.setOpaque(true);
		this.setHorizontalAlignment(JLabel.CENTER);
		
	}
	
	public int getBoardX() {
		return px;
	}

	public int getBoardY() {
		return py;
	}

	public void setBoardX(int x) {
		this.px = x;
	}

	public void setBoardY(int y) {
		this.py = y;
	}
	private Place getPlace(){
		return this;
	}
	public abstract boolean isEmptyTile();
}
