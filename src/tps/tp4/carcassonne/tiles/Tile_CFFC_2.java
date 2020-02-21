package tps.tp4.carcassonne.tiles;

public class Tile_CFFC_2 extends Tile_CFFC {
	private static final long serialVersionUID = 1L;

	public Tile_CFFC_2() {
		super();
	}

	// retornar instancias da classe
	public static Tile_CFFC_2[] getPecas() {
		return new Tile_CFFC_2[] { new Tile_CFFC_2(), new Tile_CFFC_2() };
	}

	private static String FILEPATH = "Tile09_cffc_2_x2.jpg";

	public String getFilePath() {
		return FILEPATH;
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
