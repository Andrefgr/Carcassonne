package tps.tp4.carcassonne;

import java.awt.Color;

/**
 * The Player class must contain information about a player, such as yours.   
 * name, your color, your markers, your points, etc.
 * 
 * @author Andre ROG
 *
 */
public class Player {
	private String name;
	private Color color;
	private int numMarkers = 7;
	private int points;

	Player(String name) {
		this.name = name;
		points = 0;
	}

	public String getName() {
		return name;
	}

	private Color getColor() {
		return color;
	}
	private void setColor(Color color){
		this.color = color;
	}

	public void setPoints(int points) {
		this.points += points;
	}

	public int getPoints() {
		return points;
	}

	public int getNumMarkers() {
		return numMarkers;
	}

	public void setNumMarkers(int numMarkers) {
		this.numMarkers = numMarkers;
	}
}
