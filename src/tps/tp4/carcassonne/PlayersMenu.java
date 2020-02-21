package tps.tp4.carcassonne;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class PlayersMenu extends JFrame{
	private static final long serialVersionUID = 1L;
	private Player[] players;
	private JTextField textField1, textField2, textField3, textField4, textField5;
	private BufferedImage cursorImage, cursorClicked;
	private Toolkit kit;

	public static void main(String[] args) {
		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// new PlayersMenu();
		// }
		// });
	}

	public PlayersMenu(Stage menuStage) {
		
		AudioClip click = new AudioClip(this.getClass().getResource("music/click.mp3").toString());
		
		try {
			cursorImage = ImageIO.read(getClass().getResource("img/ha.png"));
			cursorClicked = ImageIO.read(getClass().getResource("img/haa.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setResizable(false);
		setTitle("Carcassonne");
		setSize(600, 505);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		changeCursor(cursorImage);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				changeCursor(cursorClicked);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				changeCursor(cursorImage);
			}
		});
		// add background image
		JLabel background = new JLabel(new ImageIcon(this.getClass().getResource("img/menu2.jpg")));
		background.setLayout(new FlowLayout());
		getContentPane().add(background);

		JLabel labelTilte = new JLabel("DEFINE PLAYERS");
		labelTilte.setFont(new Font("Limoges", Font.BOLD, 50));
		background.add(labelTilte, BorderLayout.NORTH);
		// add the panel to put it on the first image label
		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.setOpaque(false);
		background.add(panel1);

		textField1 = new JTextField(15);
		Font fontTextField = new Font("Limoges", Font.PLAIN, 20);
		textField1.setFont(fontTextField);
		// textField1.setSize(10, 10);
		textField1.setEnabled(false);

		textField2 = new JTextField(15);
		textField2.setFont(fontTextField);
		textField2.setEnabled(false);

		textField3 = new JTextField(15);
		textField3.setFont(fontTextField);
		textField3.setEnabled(false);

		textField4 = new JTextField(15);
		textField4.setFont(fontTextField);
		textField4.setEnabled(false);

		textField5 = new JTextField(15);
		textField5.setFont(fontTextField);
		textField5.setEnabled(false);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon image1 = new ImageIcon(
				new ImageIcon(this.getClass().getResource("tiles/Markers/1.png")).getImage().getScaledInstance(65, 65, java.awt.Image.SCALE_SMOOTH));
		JLabel label1 = new JLabel();
		label1.setIcon(image1);
		label1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel1.add(label1, constraints);
		constraints.gridy++;
		label1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				click.play();
				textField1.setEnabled(true);
			}
		});

		//
		ImageIcon image2 = new ImageIcon(
				new ImageIcon(this.getClass().getResource("tiles/Markers/2.png")).getImage().getScaledInstance(65, 65, java.awt.Image.SCALE_SMOOTH));
		JLabel label2 = new JLabel();
		label2.setIcon(image2);
		label2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				click.play();
				textField2.setEnabled(true);
			}
		});
		panel1.add(label2, constraints);
		constraints.gridy++;

		ImageIcon image3 = new ImageIcon(
				new ImageIcon(this.getClass().getResource("tiles/Markers/3.png")).getImage().getScaledInstance(65, 65, java.awt.Image.SCALE_SMOOTH));
		JLabel label3 = new JLabel();
		label3.setIcon(image3);
		label3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				click.play();
				textField3.setEnabled(true);
			}
		});
		panel1.add(label3, constraints);
		constraints.gridy++;

		ImageIcon image4 = new ImageIcon(
				new ImageIcon(this.getClass().getResource("tiles/Markers/4.png")).getImage().getScaledInstance(65, 65, java.awt.Image.SCALE_SMOOTH));
		JLabel label4 = new JLabel();
		label4.setIcon(image4);
		label4.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				click.play();
				textField4.setEnabled(true);
			}
		});
		panel1.add(label4, constraints);
		constraints.gridy++;

		ImageIcon image5 = new ImageIcon(
				new ImageIcon(this.getClass().getResource("tiles/Markers/5.png")).getImage().getScaledInstance(65, 65, java.awt.Image.SCALE_SMOOTH));
		JLabel label5 = new JLabel();
		label5.setIcon(image5);
		label5.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				click.play();
				textField5.setEnabled(true);
			}
		});
		panel1.add(label5, constraints);

		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.ipady = 5;
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel1.add(textField1, constraints);
		constraints.gridy++;
		panel1.add(textField2, constraints);
		constraints.gridy++;
		panel1.add(textField3, constraints);
		constraints.gridy++;
		panel1.add(textField4, constraints);
		constraints.gridy++;
		panel1.add(textField5, constraints);
		constraints.gridy++;

		JLabel start = new JLabel();
		ImageIcon clickStart = new ImageIcon(
				new ImageIcon(getClass().getResource("img/startw.png")).getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH));
		start.setIcon(clickStart);
		start.setCursor(new Cursor(Cursor.HAND_CURSOR));
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				click.play();
				Main.sound.stop();
				setVisible(false);
				new Game(getPlayers(), menuStage);
			}
		});
		constraints.insets = new Insets(0, 60, 100, 0);
		panel1.add(start, constraints);
		setVisible(true);
	}

	public Player[] getPlayers() {
		int numPlayers = 0;
		String[] getPlayersNames = new String[5];
		getPlayersNames[0] = textField1.getText();
		getPlayersNames[1] = textField2.getText();
		getPlayersNames[2] = textField3.getText();
		getPlayersNames[3] = textField4.getText();
		getPlayersNames[4] = textField5.getText();

		for (int i = 0; i < getPlayersNames.length; i++) {
			System.out.println(getPlayersNames[i]);
			if (!getPlayersNames[i].equals(""))
				numPlayers++;
		}
		players = new Player[numPlayers];
		int idx = 0;
		for (int i = 0; i < getPlayersNames.length; i++) {
			if (!getPlayersNames[i].equals(""))
				players[idx++] = new Player(getPlayersNames[i]);
		}
		return players;
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
			Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "CustomCursor");

			setCursor(cursor);

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
