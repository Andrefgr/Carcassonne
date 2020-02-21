package tps.tp4.carcassonne;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelTranslator;

public abstract class Tile extends Place {

	private static final long serialVersionUID = 1L;
	private ImageIcon img;
	private RotatedIcon picture;
	private int pointsNorthSide, pointsEastSide, pointsSouthSide, pointsWestSide, pointsRoads,
			pointsCatles, pointsFields;
	private String name;
	private ImageIcon marker = null;
	protected TileCharacteristics tileCharacteristics[];
	

	public Tile(int x, int y) {
		super(x, y);
		img = new ImageIcon(new ImageIcon(this.getClass().getResource("GameTiles/" + getFilePath()))
				.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
		picture = new RotatedIcon(img, 0);
		setIcon(picture);
		pointsNorthSide = 0;
		pointsEastSide = 0;
		pointsSouthSide = 0;
		pointsWestSide = 0;
		pointsCatles = 0;
		pointsRoads = 0;
		pointsFields = 0;
		name = this.getClass().getSimpleName();
		tileCharacteristics = new TileCharacteristics[4];

	}

	public void rotateTile() {
		RotatedIcon rotatedIcon = new RotatedIcon(picture, 90.0);
		rotateTileCharacteristics();
		setPicture(rotatedIcon);
		setIcon(rotatedIcon);
	}

	public String getName() {
		return name;
	}

	public void setMarker(ImageIcon marker) {
		this.marker = marker;
	}

	public ImageIcon getMarker() {
		return marker;
	}

	public abstract String getSpecialCharacteristic();

	public abstract int getNumCharacteristics(TileCharacteristics characteristics);

	public abstract boolean checkTileCharacteristics(TileCharacteristics characteristics);

	public abstract TileCharacteristics getTileCharacteristics(Side side);

	public TileCharacteristics getCharacteristics(Side side) {
		return getTileCharacteristics(side);
	}

	public abstract void setTileCharacteristics();

	public abstract void rotateTileCharacteristics();

	public abstract String getFilePath();

	public void setImageIc(ImageIcon image) {
		setIcon(image);
	}

	public ImageIcon getPictur() {
		return picture;
	}

	public void setPicture(RotatedIcon icon) {
		picture = icon;
	}

	public RotatedIcon getRotateImage() {
		return picture;
	}

	public void setPointsCharacteristic(TileCharacteristics characteristic, int points) {
		if (characteristic.equals(TileCharacteristics.Castle))
			if (name.charAt(name.length() - 1) == '2' && pointsCatles == 0)
				pointsCatles += points + points;
			else
				pointsCatles += points;
		if (characteristic.equals(TileCharacteristics.Road))
			pointsRoads += points;
		if (characteristic.equals(TileCharacteristics.Field))
			pointsFields += points;
	}

	public int getPointsCharacteristic(TileCharacteristics characteristic) {
		if (characteristic.equals(TileCharacteristics.Castle))
			return pointsCatles;
		if (characteristic.equals(TileCharacteristics.Road))
			return pointsRoads;

		return pointsFields;
	}

	public void setPointsSide(Side side, int points) {
		if (side.equals(Side.North))
			pointsNorthSide = points;
		if (side.equals(Side.East))
			pointsEastSide = points;
		if (side.equals(Side.South))
			pointsSouthSide = points;
		if (side.equals(Side.West))
			pointsWestSide = points;
	}

	public int getPointsSide(Side side) {
		if (side.equals(Side.North))
			return pointsNorthSide;
		if (side.equals(Side.East))
			return pointsEastSide;
		if (side.equals(Side.South))
			return pointsSouthSide;

		return pointsWestSide;
	}
	public int getPointsAllSides(){
		return getPointsSide(Side.North) + getPointsSide(Side.East) + getPointsSide(Side.South) + getPointsSide(Side.West);
	}
	public int getPointsAllSidesWithCharacteristic(TileCharacteristics characteristic){
		int count = 0;
		if(getTileCharacteristics(Side.North).equals(characteristic)) count += getPointsSide(Side.North);
		if(getTileCharacteristics(Side.East).equals(characteristic)) count += getPointsSide(Side.East);
		if(getTileCharacteristics(Side.South).equals(characteristic)) count += getPointsSide(Side.South);
		if(getTileCharacteristics(Side.West).equals(characteristic)) count += getPointsSide(Side.West);
		return count;
	}

	public abstract String getSpecialPoints();
}
