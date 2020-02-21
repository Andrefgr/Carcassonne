package tps.tp4.carcassonne;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class teste {
	public static void main(String[] args) {
		teste tes = new teste();
		tes.check();
	}

	public void check() {
		int i = 0;
		String name = "tiles/Markers/marker"+i+".png";
		System.out.println(name);
		ImageIcon iconPalyer = new ImageIcon(
				new ImageIcon(this.getClass().getResource("tiles/Markers/marker" + i + ".png")).getImage()
						.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
		ImageIcon iconPalye2 = new ImageIcon(
				new ImageIcon(this.getClass().getResource(name)).getImage()
						.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
		System.out.println(name);
		File f = new File(name);
//		if (f.exists() ) {
//			System.out.println("a");
//		}
		JLabel labelIcon = new JLabel();
		labelIcon.setIcon(iconPalye2);

		if (labelIcon.getIcon() != null) {
			System.out.println("a");
		}
	}
}