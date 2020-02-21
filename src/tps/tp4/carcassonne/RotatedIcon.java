package tps.tp4.carcassonne;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class RotatedIcon extends ImageIcon implements Icon {
	public enum Rotate {
		DOWN, UP, UPSIDE_DOWN, ABOUT_CENTER;
	}

	private Icon icon;

	private Rotate rotate;

	private double degrees;

	/**
	 * Convenience constructor to create a RotatedIcon that is rotated DOWN.
	 *
	 * @param icon
	 *            the Icon to rotate
	 */
//	public RotatedIcon(Icon icon) {
//		this(icon, Rotate.UP);
//	}

	/**
	 * Create a RotatedIcon
	 *
	 * @param icon
	 *            the Icon to rotate
	 * @param rotate
	 *            the direction of rotation
	 */
	public RotatedIcon(Icon icon, Rotate rotate) {
		this.icon = icon;
		this.rotate = rotate;
	}


	/**
	 * Create a RotatedIcon. The icon will rotate about its center. This
	 * constructor will automatically set the Rotate enum to ABOUT_CENTER.
	 *
	 * @param icon
	 *            the Icon to rotate
	 * @param degrees
	 *            the degrees of rotation
	 * @param circularIcon
	 *            treat the icon as circular so its size doesn't change
	 */
	public RotatedIcon(Icon icon, double degrees) {
		this(icon, Rotate.ABOUT_CENTER);
		setDegrees(degrees);
	}

	/**
	 * Gets the Icon to be rotated
	 *
	 * @return the Icon to be rotated
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * Gets the Rotate enum which indicates the direction of rotation
	 *
	 * @return the Rotate enum
	 */
	public Rotate getRotate() {
		return rotate;
	}

	/**
	 * Gets the degrees of rotation. Only used for Rotate.ABOUT_CENTER.
	 *
	 * @return the degrees of rotation
	 */
	public double getDegrees() {
		return degrees;
	}

	/**
	 * Set the degrees of rotation. Only used for Rotate.ABOUT_CENTER. This
	 * method only sets the degress of rotation, it will not cause the Icon to
	 * be repainted. You must invoke repaint() on any component using this icon
	 * for it to be repainted.
	 *
	 * @param degrees
	 *            the degrees of rotation
	 */
	public void setDegrees(double degrees) {
		this.degrees = degrees;
	}


	//
	// Implement the Icon Interface
	//

	/**
	 * Gets the width of this icon.
	 *
	 * @return the width of the icon in pixels.
	 */
	@Override
	public int getIconWidth() {
		return 120;
	}

	/**
	 * Gets the height of this icon.
	 *
	 * @return the height of the icon in pixels.
	 */
	@Override
	public int getIconHeight() {
		return 120;
	}

	/**
	 * Paint the icons of this compound icon at the specified location
	 *
	 * @param c
	 *            The component on which the icon is painted
	 * @param g
	 *            the graphics context
	 * @param x
	 *            the X coordinate of the icon's top-left corner
	 * @param y
	 *            the Y coordinate of the icon's top-left corner
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;

		int cWidth = icon.getIconWidth() / 2;
		int cHeight = icon.getIconHeight() / 2;

		if (rotate == Rotate.ABOUT_CENTER) {
			g2.translate((getIconWidth() - icon.getIconWidth()) / 2, (getIconHeight() - icon.getIconHeight()) / 2);
			g2.rotate(Math.toRadians(degrees), x + cWidth, y + cHeight);
			icon.paintIcon(c, g2, x, y);
		}

		g2.dispose();
	}
}
