package tps.tp4.carcassonne.tiles;

import java.util.Arrays;

import tps.tp4.carcassonne.Side;
import tps.tp4.carcassonne.Tile;
import tps.tp4.carcassonne.TileCharacteristics;

public class Tile_RRRR_NC extends Tile {
	private static final long serialVersionUID = 1L;

	public Tile_RRRR_NC() {
		super(0, 0);
		setTileCharacteristics();
	}

	@Override
	public boolean isEmptyTile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void rotateTile() {
		// TODO Auto-generated method stub
	}

	private TileCharacteristics[] tileCharacteristics = new TileCharacteristics[4];

	@Override
	public TileCharacteristics getTileCharacteristics(Side side) {
		return tileCharacteristics[side.ordinal()];
	}
	// rodar as caracteristicas da peça depois da peça ser rodada
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
			tileCharacteristics[1] = TileCharacteristics.Road;
			tileCharacteristics[2] = TileCharacteristics.Road;
			tileCharacteristics[3] = TileCharacteristics.Road;
	}

	// retornar instancias da classe
	public static Tile_RRRR_NC getPecas() {
		return new Tile_RRRR_NC();
	}

	private static String FILEPATH = "Tile24_rrrr_nc_x1.jpg";

	public String getFilePath() {
		return FILEPATH;
	}
	@Override
	public boolean checkTileCharacteristics(TileCharacteristics characteristics) {
		if(characteristics.equals(TileCharacteristics.Castle)) return false;
		else if(characteristics.equals(TileCharacteristics.Field)){
			return false;
		}
		return true;
	}
	public int getNumCharacteristics(TileCharacteristics characteristics){
		if (characteristics.equals(TileCharacteristics.Road)) return 4;
		return 0;
		
	}

	@Override
	public String getSpecialCharacteristic() {
		return "nc";
	}

	@Override
	public String getSpecialPoints() {
		return "none";
	}
}
