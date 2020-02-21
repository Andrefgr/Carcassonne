package tps.tp4.carcassonne.tiles;

public class Tile_CCRC_2 extends Tile_CCRC {
	private static final long serialVersionUID = 1L;

	public Tile_CCRC_2() {
	}

	// retornar instancia da classe
	public static Tile_CCRC_2[] getPecas() {
		return new Tile_CCRC_2[] { new Tile_CCRC_2(), new Tile_CCRC_2() };
	}

	private static String FILEPATH = "Tile07_ccrc_2_x2.jpg";

	public String getFilePath() {
		return FILEPATH;
	}
	@Override
	public String getSpecialCharacteristic() {
		return "RoadEnd";
	}
	@Override
	public String getSpecialPoints() {
		return "2";
	}
}
