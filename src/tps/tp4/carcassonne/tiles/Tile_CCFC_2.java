package tps.tp4.carcassonne.tiles;

public class Tile_CCFC_2 extends Tile_CCFC {
	private static final long serialVersionUID = 1L;

	private static String FILEPATH = "Tile05_ccfc_2_x1.jpg";

	public Tile_CCFC_2() {
	}

	// retornar instancia da classe
	public static Tile_CCFC_2[] getPecas() {
		return new Tile_CCFC_2[] { new Tile_CCFC_2() };
	}
	
	public String getFilePath() {
		return FILEPATH;
	}
	@Override
	public String getSpecialCharacteristic() {
		return "FieldEnd";
	}
	@Override
	public String getSpecialPoints() {
		return "2";
	}
}
