package tps.tp4.carcassonne;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import tps.tp4.carcassonne.tiles.*;

public class Game extends JFrame{
	// criar e adicionar a estrutura com todas as peças
	public ArrayList<Tile> listTiles = new ArrayList<Tile>();;
	private static final long serialVersionUID = 1L;
	private static AudioClip music;
	private BufferedImage cursorImage, cursorClicked;
	private Toolkit kit;
	private AudioClip click = new AudioClip(this.getClass().getResource("music/click.mp3").toString());
	private JPanel rightPanel;
	private ScorePanel scorePanel;
	private JMenuBar menuBar;
	private JMenu mnSesson;
	private Stage menuStage;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				new Game(new Player[] { new Player("a") });
//			}
//		});

	}

	public void initialize() {

		music = new AudioClip(this.getClass().getResource("music/gameMusic.mp3").toString());
		music.play();
		try {
			cursorImage = ImageIO.read(getClass().getResource("img/ha.png"));
			cursorClicked = ImageIO.read(getClass().getResource("img/haa.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		changeCursor(cursorImage);
	}

	public Game(Player[] players, Stage primary) {
		setmenuStage(primary);
		
		// criar lista com as peças e baralhalas
		setTiles();
		suffleTiles();
		setResizable(false);
		setTitle("Carcassonne");
		setSize(1300, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(217, 171, 102));
		// layout da jframe
		BoxLayout box = new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS);
		getContentPane().setLayout(box);

		// adicionar primeiro jpanel para poder fazer zoom
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(new Color(217, 171, 102));
		// panel.setBounds(0, 0, 1000, 700);
		panel.setSize(1000, 700);
		getContentPane().add(panel);
		// adicional o board
		Board board = new Board(this, 15, 20, listTiles, players);
		// board.setBackground(new Color(214, 155, 46));
		// board.setBackground(new Color(10, 255, 10));
		board.setBackground(new Color(217, 171, 102));
		board.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				changeCursor(cursorClicked);
				click.play();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				changeCursor(cursorImage);
			}
		});
		panel.add(board);

		// adicionar o panel com o scroll
		JScrollPane scrollPane = new JScrollPane(panel);
		getContentPane().add(scrollPane);

		JLabel backImage = new JLabel();
		backImage.setIcon(new ImageIcon(getClass().getResource("img/11.jpg")));
		backImage.setLayout(new FlowLayout());
		getContentPane().add(backImage);
		// adicionar o painel á direita
		rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		backImage.add(rightPanel);
		// definir o layout do painel
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		// adicionar o painel para mostrar a imagem
		TilePanel tilePanel = new TilePanel(this, listTiles);
		tilePanel.setOpaque(false);
		rightPanel.add(tilePanel);
		// adicionar o painel para mostrar os pontos e os jogadores

		scorePanel = new ScorePanel(players);
		scorePanel.setOpaque(false);
		rightPanel.add(scorePanel);

		menuBar = new JMenuBar();
		menuBar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		setJMenuBar(menuBar);

		mnSesson = new JMenu("Sesson");
		menuBar.add(mnSesson);

		JMenuItem mntmNewGame = new JMenuItem("New Game");
		mntmNewGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				music.stop();
				setVisible(false);
				new PlayersMenu(getmenuStage());
			}
		});
		mnSesson.add(mntmNewGame);

		JMenuItem endGame = new JMenuItem("End Game");
		endGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Platform.runLater(() -> {
	                primary.sizeToScene();
	                primary.show();
	            });
			}
		});
		mnSesson.add(endGame);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				System.exit(1);
			}
		});
		mnSesson.add(mntmQuit);

		JMenu mnHelp = new JMenu("Help");
		JMenuItem info = new JMenuItem("Instructions");
		info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				menuHelp();
			}
		});
		menuBar.add(mnHelp);
		mnHelp.add(info);
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				menuAbout();
			}
		});
		mnHelp.add(mntmAbout);
		// colocar a frame visivel
		setVisible(true);

		initialize();
	}

	private void setTiles() {
		// // tile01
		listTiles.addAll(Arrays.asList(Tile_FFFF_I.getPecas()));
		// // tile02
		listTiles.addAll(Arrays.asList(Tile_FFRF_I.getPecas()));
		// // tile03
		listTiles.add(Tile_CCCC_2.getPecas());
		// // tile04
		listTiles.addAll(Arrays.asList(Tile_CCFC.getPecas()));
		// // tile05
		listTiles.addAll(Arrays.asList(Tile_CCFC_2.getPecas()));
		// // tile06
		listTiles.addAll(Arrays.asList(Tile_CCRC.getPecas()));
		// // tile07
		listTiles.addAll(Arrays.asList(Tile_CCRC_2.getPecas()));
		// // tile08
		listTiles.addAll(Arrays.asList(Tile_CFFC.getPecas()));
		// // tile09
		listTiles.addAll(Arrays.asList(Tile_CFFC_2.getPecas()));
		// // tile10
		listTiles.addAll(Arrays.asList(Tile_CRRC.getPecas()));
		// // tile11
		listTiles.addAll(Arrays.asList(Tile_CRRC_2.getPecas()));
		// // tile12
		listTiles.addAll(Arrays.asList(Tile_FCFC.getPecas()));
		// // tile13
		listTiles.addAll(Arrays.asList(Tile_FCFC_2.getPecas()));
		// // tile14
		listTiles.addAll(Arrays.asList(Tile_CFFC_NC.getPecas()));
		// // tile15
		listTiles.addAll(Arrays.asList(Tile_CFCF_NC.getPecas()));
		// // tile16
		listTiles.addAll(Arrays.asList(Tile_CFFF.getPecas()));
		// // // tile17
		listTiles.addAll(Arrays.asList(Tile_CFRR.getPecas()));
		// // // tile18
		listTiles.addAll(Arrays.asList(Tile_CRRF.getPecas()));
		// // // tile19
		listTiles.addAll(Arrays.asList(Tile_CRRR_NC.getPecas()));
		// // tile20
		listTiles.addAll(Arrays.asList(Tile_CRFR.getPecas()));
		// //tile21
		listTiles.addAll(Arrays.asList(Tile_RFRF.getPecas()));
		// // tile22
		listTiles.addAll(Arrays.asList(Tile_FFRR.getPecas()));
		// // tile23
		listTiles.addAll(Arrays.asList(Tile_FRRR_NC.getPecas()));
		// // tile24
		listTiles.add(Tile_RRRR_NC.getPecas());
	}

	public void menuHelp() {
		new Help(this);
	}

	public void menuAbout() {
		new About(this);
	}

	public static void MuteMusic() {
		if (music.isPlaying())
			music.stop();
		else
			music.play();
	}

	public void suffleTiles() {
		Collections.shuffle(listTiles);
	}

	public void changeCursor(BufferedImage cursorImage) {
		try {
			kit = Toolkit.getDefaultToolkit();
			for (int i = 0; i < cursorImage.getHeight(); i++) {
				int[] rgb = cursorImage.getRGB(0, i, cursorImage.getWidth(), 1, null, 0, cursorImage.getWidth() * 4);
				for (int j = 0; j < rgb.length; j++) {
					int alpha = (rgb[j] >> 24) & 255;
					if (alpha < 128) {
						alpha = 0;
					} else {
						alpha = 255;
					}
					rgb[j] &= 0x00ffffff;
					rgb[j] = (alpha << 24) | rgb[j];
				}
				cursorImage.setRGB(0, i, cursorImage.getWidth(), 1, rgb, 0, cursorImage.getWidth() * 4);
			}
			Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0),
					"CustomCursor");

			setCursor(cursor);

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public void changeVisi(boolean visi) {
		setVisible(visi);
	}
	
	public void setmenuStage(Stage stage) {
		this.menuStage = stage;
	}
	public Stage getmenuStage() {
		return this.menuStage;
	}
	public ScorePanel getScorePanel() {
		return this.scorePanel;
	}
}
