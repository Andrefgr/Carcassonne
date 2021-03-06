package tps.tp4.carcassonne.tiles;

import java.util.Arrays;

import tps.tp4.carcassonne.Side;
import tps.tp4.carcassonne.Tile;
import tps.tp4.carcassonne.TileCharacteristics;

public class Tile_FCFC extends Tile {
	private static final long serialVersionUID = 1L;

	public Tile_FCFC() {
		super(0, 0);
		setTileCharacteristics();
	}

	@Override
	public boolean isEmptyTile() {
		return false;
	}

	private TileCharacteristics[] tileCharacteristics = new TileCharacteristics[4];

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
			tileCharacteristics[0] = TileCharacteristics.Field;
			tileCharacteristics[1] = TileCharacteristics.Castle;
			tileCharacteristics[2] = TileCharacteristics.Field;
			tileCharacteristics[3] = TileCharacteristics.Castle;
	}

	// retornar instancias da classe
	public static Tile_FCFC[] getPecas() {
		return new Tile_FCFC[] { new Tile_FCFC() };
	}

	private static String FILEPATH = "Tile12_fcfc_x1.jpg";

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
		if(characteristics.equals(TileCharacteristics.Castle)) return 2;
		else if(characteristics.equals(TileCharacteristics.Road)) return 0;
		return 2;
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
