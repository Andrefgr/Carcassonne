package tps.tp4.carcassonne;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.SwingConstants;

public class Options extends JFrame {
	private BufferedImage cursorImage, cursorClicked;
	private Toolkit kit;
	private JLabel background;
	private ImageIcon[] rules = new ImageIcon[7];
	
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Options frame = new Options();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * Create the frame.
	 */
	public Options(Stage primary) {
		rules[0] = new ImageIcon(this.getClass().getResource("img/rule1.jpg"));
		rules[1] = new ImageIcon(this.getClass().getResource("img/rule2.jpg"));
		rules[2] = new ImageIcon(this.getClass().getResource("img/rule3.jpg"));
		rules[3] = new ImageIcon(this.getClass().getResource("img/rule4.jpg"));
		rules[4] = new ImageIcon(this.getClass().getResource("img/rule5.jpg"));
		rules[5] = new ImageIcon(this.getClass().getResource("img/rule6.jpg"));
		rules[6] = new ImageIcon(this.getClass().getResource("img/keys.png"));
		
		try {
			cursorImage = ImageIO.read(getClass().getResource("img/ha.png"));
			cursorClicked = ImageIO.read(getClass().getResource("img/haa.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Carcassonne");
		setSize(611, 940);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		changeCursor(cursorImage);
		// adicionar a imagem de fundo
		background = new JLabel(new ImageIcon(this.getClass().getResource("img/rule1.jpg")));
		background.setLayout(new FlowLayout());
		getContentPane().add(background);
		
		JLabel home = new JLabel();
		
		ImageIcon castle = new ImageIcon(
				new ImageIcon(this.getClass().getResource("img/home.png")).getImage().getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH));
		home.setIcon(castle);
		home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				home.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				setVisible(false);
				Platform.runLater(() -> {
	                primary.sizeToScene();
	                primary.show();
	            });
//				Application.launch(Main.class);
			}
			
		});
		background.add(home);
		
		
		
		JLabel leftArrow = new JLabel();
		ImageIcon left = new ImageIcon(
				new ImageIcon(this.getClass().getResource("img/l2.png")).getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH));
		leftArrow.setIcon(left);
		
		leftArrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				leftArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				leftRule();
			}
			
		});
		background.add(leftArrow, SwingConstants.CENTER);
		
		JLabel rightArrow = new JLabel();
		rightArrow.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon right = new ImageIcon(
				new ImageIcon(this.getClass().getResource("img/l22.png")).getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH));
		rightArrow.setIcon(right);
		rightArrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				rightArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				rightArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
				rightRule();
				
			}
			
		});
		background.add(rightArrow);
		
		
		setVisible(true);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		background.setIcon(rules[0]);
		repaint();
	}
	public void rightRule(){
		ImageIcon[] b = new ImageIcon[rules.length];
		int idx = 0;
		for (int i = 0; i < rules.length; i++) {
			if(i+1 == rules.length) b[idx++] = rules[0]; 
			else b[idx++] = rules[i+1]; 
		}
		rules = b;
	}
	public void leftRule(){
		
		ImageIcon b = rules[rules.length-1];
		System.arraycopy(rules, 0, rules, 1, rules.length-1 );
		rules[0] = b;
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

	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		
	}
}
