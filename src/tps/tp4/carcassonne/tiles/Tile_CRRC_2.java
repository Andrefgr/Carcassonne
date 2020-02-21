package tps.tp4.carcassonne.tiles;

public class Tile_CRRC_2 extends Tile_CRRC {
	private static final long serialVersionUID = 1L;

	public Tile_CRRC_2() {
	}

	// retornar instancias da classe
	public static Tile_CRRC_2[] getPecas() {
		return new Tile_CRRC_2[] { new Tile_CRRC_2(), new Tile_CRRC_2() };
	}
	private static String FILEPATH = "Tile11_crrc_2_x2.jpg";

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
