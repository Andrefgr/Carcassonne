package tps.tp4.carcassonne.tiles;

public class Tile_FCFC_2 extends Tile_FCFC {
	private static final long serialVersionUID = 1L;

	public Tile_FCFC_2() {
	}

	// retornar instancias da classe
	public static Tile_FCFC_2[] getPecas() {
		return new Tile_FCFC_2[] { new Tile_FCFC_2(), new Tile_FCFC_2() };
	}

	private static String FILEPATH = "Tile13_fcfc_2_x2.jpg";

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
