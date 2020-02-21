package tps.tp4.carcassonne;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import sun.font.CreatedFontTracker;

public class ScorePanel extends JPanel {
	private static int idxPlayer = 0;
	private Player[] players;
	private static final long serialVersionUID = 1L;
	private JPanel[] playersPanel;
	private JLabel[] pointsLabel, playersMarker;
	private JLabel[] turnsLabel, playersLabel;
	ImageIcon hammer = new ImageIcon(new ImageIcon(this.getClass().getResource("img/ha.png")).getImage()
			.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH));
	ImageIcon next = new ImageIcon(new ImageIcon(this.getClass().getResource("img/time1.png")).getImage()
			.getScaledInstance(50, 55, java.awt.Image.SCALE_SMOOTH));


	public ScorePanel(Player[] players) {

		this.players = players;
		
		createLabelsTurns();

		createLabelsPlayers();

		setFirstPlayer();
		
		setLayout(new GridLayout(6, 1));

		createPlayersPanel();

		createPlayers();

		setVisible(true);
	}

	private void createLabelsTurns() {
		// create labels for all player turn and each icon
		turnsLabel = new JLabel[players.length];
		for (int i = 0; i < turnsLabel.length; i++) {
			turnsLabel[i] = new JLabel();
			turnsLabel[i].setIcon(hammer);
			turnsLabel[i].setVisible(false);
		}
	}

	private void createLabelsPlayers() {
		// create labels for all players
		playersLabel = new JLabel[players.length];
		for (int i = 0; i < playersLabel.length; i++) {
			playersLabel[i] = new JLabel();
		}
	}

	private void createPlayersPanel() {
		playersPanel = new JPanel[players.length];
		for (int i = 0; i < players.length; i++) {
			playersPanel[i] = new JPanel();
			playersPanel[i].setOpaque(false);
			playersPanel[i].setLayout(new BorderLayout());
			playersPanel[i].add(turnsLabel[i], BorderLayout.CENTER);
			playersPanel[i].add(playersLabel[i], BorderLayout.EAST);
			add(playersPanel[i]);
		}
	}

	private void createPlayers() {
		playersMarker = new JLabel[players.length];
		pointsLabel = new JLabel[players.length];
		
		for (int i = 0; i < players.length; i++) {

			ImageIcon iconPalyer = new ImageIcon(new ImageIcon(this.getClass().getResource("tiles/Markers/marker"+i+".png"))
					.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
			
			JLabel labelName = new JLabel(players[i].getName());
			labelName.setHorizontalAlignment(JLabel.LEFT);
			labelName.setFont(new Font("Limoges", Font.BOLD, 25));
			labelName.setForeground(Color.WHITE);
			playersPanel[i].add(labelName, BorderLayout.NORTH);
			JLabel labelIcon = new JLabel();
			labelIcon.setIcon(iconPalyer);
			
			JPanel panelWestPlayer = new JPanel(new GridLayout(2, 2));
			panelWestPlayer.setOpaque(false);
			panelWestPlayer.add(labelIcon);

			playersMarker[i] = new JLabel("" + players[i].getNumMarkers());
			panelWestPlayer.add(playersMarker[i]);

			JLabel labelIconPoints = new JLabel();
			ImageIcon pointsIcon1 = new ImageIcon(new ImageIcon(this.getClass().getResource("img/points.png"))
					.getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH));
			labelIconPoints.setIcon(pointsIcon1);
			panelWestPlayer.add(labelIconPoints);
			pointsLabel[i] = new JLabel();
			pointsLabel[i].setText("" + players[i].getPoints());
			panelWestPlayer.add(pointsLabel[i]);
			playersPanel[i].add(panelWestPlayer, BorderLayout.WEST);
		}
	}
	public void setFirstPlayer() {
		turnsLabel[0].setVisible(true);
		playersLabel[0].setVisible(true);
	}
	
	public void setnextPlayer() {
		// hide the current player
		turnsLabel[idxPlayer].setVisible(false);
		playersLabel[idxPlayer].setVisible(false);
		// increment the player
		if (idxPlayer == players.length - 1)
			idxPlayer = 0;
		else
			idxPlayer++;
		// show the current player
		turnsLabel[idxPlayer].setVisible(true);
		playersLabel[idxPlayer].setVisible(true);

	}

	public Player getCurrentPlayer() {
		return players[idxPlayer];
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < pointsLabel.length; i++) {
			pointsLabel[i].setText(" " + players[i].getPoints());
		}
		repaint();
	}

}
