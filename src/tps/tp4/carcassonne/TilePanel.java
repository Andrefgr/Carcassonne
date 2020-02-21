package tps.tp4.carcassonne;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class TilePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private Game game;
	// public ArrayList<Tile> listTiles;
	private JLabel label;
	private JLabel labelText;

	public TilePanel(Game game, ArrayList<Tile> listTiles) {
		this.game = game;
		setLayout(new FlowLayout());
		label = new JLabel();
		label.setPreferredSize(new Dimension(120, 120));
		label.setHorizontalAlignment(JLabel.LEFT);
		add(label);
  
		labelText = new JLabel();
		labelText.setText("<html> Remaning  <br>   " + listTiles.size() + "  <br>  Tiles</html>");
		labelText.setOpaque(false);
		labelText.setHorizontalAlignment(JLabel.RIGHT);
		labelText.setFont(new Font("STENCIL", Font.BOLD, 25));
		add(labelText);
		JLabel muteLabel = new JLabel();
		ImageIcon mute = new ImageIcon(
				new ImageIcon(this.getClass().getResource("img/mute.png")).getImage()
						.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
		
		muteLabel.setIcon(mute);
		muteLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseExited(e);
				muteLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				Game.MuteMusic();
				super.mousePressed(e);
			}
		});
		add(muteLabel);
	}

	@Override
	public void run() {
		System.out.println("run");
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		RotatedIcon iconImage;
		iconImage = game.listTiles.get(0).getRotateImage();
		label.setIcon(iconImage);
		label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,5));
		labelText.setText("<html> Remaning<br>   " + game.listTiles.size()+"  <br>  Tiles</html>");
		repaint();
	}
}
