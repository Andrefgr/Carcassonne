package tps.tp4.carcassonne.tiles;

import java.util.Arrays;

import tps.tp4.carcassonne.Side;
import tps.tp4.carcassonne.Tile;
import tps.tp4.carcassonne.TileCharacteristics;

public class Tile_CRFR extends Tile {
	private static final long serialVersionUID = 1L;

	private static String FILEPATH = "Tile20_crfr_x4.jpg";
	private TileCharacteristics[] tileCharacteristics = new TileCharacteristics[4];

	public Tile_CRFR() {
		super(0, 0);
		setTileCharacteristics();
	}

	@Override
	public boolean isEmptyTile() {
		return false;
	}

	@Override
	public TileCharacteristics getTileCharacteristics(Side side) {
		return tileCharacteristics[side.ordinal()];
	}
	// rodar as caracteristicas da pe�a depois da pe�a ser rodada
	@Override
	public void rotateTileCharacteristics() {
		TileCharacteristics[] rotateTiles = new TileCharacteristics[tileCharacteristics.length];
        TileCharacteristics ultimo = tileCharacteristics[tileCharacteristics.length - 1];
        System.arraycopy (tileCharacteristics, 0, rotateTiles, 1, tileCharacteristics.length - 1);
        rotateTiles [0] = ultimo;
        tileCharacteristics = Arrays.copyOf(rotateTiles, tileCharacteristics.length);
	}

	@Override
	public void setTileCharacteristics() {
			tileCharacteristics[0] = TileCharacteristics.Castle;
			tileCharacteristics[1] = TileCharacteristics.Road;
			tileCharacteristics[2] = TileCharacteristics.Field;
			tileCharacteristics[3] = TileCharacteristics.Road;
	}

	// retornar instancias da classe
	public static Tile_CRFR[] getPecas() {
		return new Tile_CRFR[] { new Tile_CRFR(), new Tile_CRFR(), new Tile_CRFR(), new Tile_CRFR() };
	}
	
	public String getFilePath() {
		return FILEPATH;
	}

	@Override
	public boolean checkTileCharacteristics(TileCharacteristics characteristics) {
		if(characteristics.equals(TileCharacteristics.Castle)) return true;
		else if(characteristics.equals(TileCharacteristics.Field)){
			return true;
		}
		return false;
	}
	@Override
	public int getNumCharacteristics(TileCharacteristics characteristics) {
		if(characteristics.equals(TileCharacteristics.Castle)) return 1;
		else if (characteristics.equals(TileCharacteristics.Road)) return 2;
		return 1;
	}
	@Override
	public String getSpecialCharacteristic() {
		return "none";
	}

	@Override
	public String getSpecialPoints() {
		return "none";
	}

}