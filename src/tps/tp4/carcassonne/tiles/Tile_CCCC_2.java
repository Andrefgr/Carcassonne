package tps.tp4.carcassonne.tiles;

import java.util.Arrays;

import tps.tp4.carcassonne.Side;
import tps.tp4.carcassonne.Tile;
import tps.tp4.carcassonne.TileCharacteristics;

public class Tile_CCCC_2 extends Tile {
	private static final long serialVersionUID = 1L;
	private static String FILEPATH = "Tile03_cccc_2_x1.jpg";
	private TileCharacteristics tileCharacteristics[] = new TileCharacteristics[4];

	public Tile_CCCC_2() {
		super(0, 0);
		setTileCharacteristics();
	}

	@Override
	public boolean isEmptyTile() {
		return false;
	}

	// retornar instancia da classe
	public static Tile_CCCC_2 getPecas() {
		return new Tile_CCCC_2();
	}
	// retornar as caracteristicas da peça num array
	@Override
	public TileCharacteristics getTileCharacteristics(Side side) {
		return tileCharacteristics[side.ordinal()];
	}
	//retornar o path da imagem correspondente a esta imagem
	public String getFilePath() {
		return FILEPATH;
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
			tileCharacteristics[0] = TileCharacteristics.Castle;
			tileCharacteristics[1] = TileCharacteristics.Castle;
			tileCharacteristics[2] = TileCharacteristics.Castle;
			tileCharacteristics[3] = TileCharacteristics.Castle;
	}

	@Override
	public boolean checkTileCharacteristics(TileCharacteristics characteristics) {
		if(characteristics.equals(TileCharacteristics.Castle)) return true;
		else if(characteristics.equals(TileCharacteristics.Field)){
			return false;
		}
		return false;
	}

	@Override
	public int getNumCharacteristics(TileCharacteristics characteristics) {
		if(characteristics.equals(TileCharacteristics.Castle)) return 4;
		return 0;
	}

	@Override
	public String getSpecialCharacteristic() {
		return "none";
	}
	@Override
	public String getSpecialPoints() {
		return "2";
	}

	


}
