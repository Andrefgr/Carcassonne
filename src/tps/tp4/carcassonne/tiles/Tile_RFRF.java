package tps.tp4.carcassonne.tiles;

import java.util.Arrays;

import tps.tp4.carcassonne.Side;
import tps.tp4.carcassonne.Tile;
import tps.tp4.carcassonne.TileCharacteristics;

public class Tile_RFRF extends Tile {
	private static final long serialVersionUID = 1L;

	public Tile_RFRF() {
		super(0, 0);
		setTileCharacteristics();
	}

	@Override
	public boolean isEmptyTile() {
		// TODO Auto-generated method stub
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
			tileCharacteristics[0] = TileCharacteristics.Road;
			tileCharacteristics[1] = TileCharacteristics.Field;
			tileCharacteristics[2] = TileCharacteristics.Road;
			tileCharacteristics[3] = TileCharacteristics.Field;
	}
	// retornar instancias da classe
	public static Tile_RFRF[] getPecas() {
		return new Tile_RFRF[] { new Tile_RFRF(), new Tile_RFRF(), new Tile_RFRF(), new Tile_RFRF(), new Tile_RFRF(),
				new Tile_RFRF(), new Tile_RFRF(), new Tile_RFRF() };
//		return new Tile_RFRF[] { new Tile_RFRF()};
	}

	private static String FILEPATH = "Tile21_rfrf_x8.jpg";

	public String getFilePath() {
		return FILEPATH;
	}

	@Override
	public boolean checkTileCharacteristics(TileCharacteristics characteristics) {
		if(characteristics.equals(TileCharacteristics.Castle)) return false;
		else if(characteristics.equals(TileCharacteristics.Field)){
			return true;
		}
		return true;
	}
	@Override
	public int getNumCharacteristics(TileCharacteristics characteristics) {
		if(characteristics.equals(TileCharacteristics.Castle)) return 0;
		else if(characteristics.equals(TileCharacteristics.Road)) return 2;
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
