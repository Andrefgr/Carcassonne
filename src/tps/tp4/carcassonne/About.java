package tps.tp4.carcassonne;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class About extends JFrame {
	private JLabel background;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					About frame = new About();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public About(Game game) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Carcassonne");
		setSize(1249, 725);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		background = new JLabel(new ImageIcon(this.getClass().getResource("img/about.png")));
		background.setLayout(new FlowLayout());
		getContentPane().add(background);

		JLabel home = new JLabel();

		ImageIcon castle = new ImageIcon(new ImageIcon(this.getClass().getResource("img/home.png")).getImage()
				.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH));
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
				game.setVisible(true);
			}

		});
		background.add(home);
		setVisible(true);
	}

}
