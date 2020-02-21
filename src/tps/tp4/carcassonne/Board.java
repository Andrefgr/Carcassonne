package tps.tp4.carcassonne;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import tps.tp4.carcassonne.tiles.Tile_CRFR;

/**
 * Board class must contain the board itself and must itself be a JPanel   *   *
 * split grid with desired grid and have methods to: place   *   * one piece;
 * get the part at the x and y coordinates; place a marker on a   *   *
 * characteristic of a piece; remove markers from a feature on a   *   * piece
 * and the pieces that continue this feature; clean the   *   * board; and
 * others as needed.
 * 
 * @author Andre ROG
 *
 */
public class Board extends JPanel {
	private static final long serialVersionUID = 1L;
	private static int prevN = 0;
	private Dimension preferredSize = new Dimension(1000, 700);
	private ArrayList<Tile> listTiles;
	private Tile[][] tiles;
	private int lines, colums;
//	private static Player[] players;
	private Tile putTile;
	private ArrayList<Tile> closeTilesCharacteristics = new ArrayList<Tile>();

	private int pointsCastles, pointsRoads;
	private Tile tileWest, tileNorth, tileEast, tileSouth;
	private TileCharacteristics[] tileCharacteristics;
	private boolean end = false;
	private static int idxPlayer = 0;
	private int currentX = 0;
	private int currentY = 0;
	private boolean put = false;
	private Game game;

	/*
	 * 
	 */
	public Board(Game game, int lines, int columns, ArrayList<Tile> listTiles, Player[] players) {
		this.game = game;
		tiles = new Tile[lines][columns];
		pointsCastles = 0;
		pointsRoads = 0;
		this.lines = lines;
		this.colums = columns;
		this.listTiles = listTiles;
		setLayout(new GridLayout(lines, columns, 0, 0));
		// setBackground(Color.GRAY);
		setFocusable(true);
		// add all zones to place the parts
		for (int iy = 0; iy < lines; iy++) {
			for (int ix = 0; ix < columns; ix++) {
				EmptyTile emptyTile = new EmptyTile(ix, iy);
				add(emptyTile);
			}
		}

		// add initial tile
		Tile startTile = new Tile_CRFR();
		// build the start place, add it to the board
		putTile(startTile, columns / 2, lines / 2);
		tiles[columns / 2][lines / 2] = startTile;
		// mouse listener to detect scrollwheel events
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int posx = MouseInfo.getPointerInfo().getLocation().x;
				int posy = MouseInfo.getPointerInfo().getLocation().y;
				updatePreferredSize(e.getWheelRotation(), new Point(posx, posy));
			}
		});

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// rotate tile
				if (e.getKeyCode() == KeyEvent.VK_R) {
					game.listTiles.get(0).rotateTile();
				}
				// remove last tile
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					game.listTiles.add(0, (Tile) getComponent(currentY * colums + currentX));
					removePlace(currentX, currentY);
					cleanAllPLaces();
					addEmptyTile(new EmptyTile(currentX, currentY), currentX, currentY);
				}
			}
		});
		// insert tile
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					// if the player inserted in empty tile and the game is not over
					if (getComponentAt(getMousePosition()) instanceof EmptyTile && !end) {
						EmptyTile emptyTile = (EmptyTile) getComponentAt(getMousePosition());
						
						if (emptyTile.getPut()) {
							Tile putTile = game.listTiles.get(0);
							currentX = emptyTile.getBoardX();
							currentY = emptyTile.getBoardY();
							putTile(putTile, emptyTile.getBoardX(), emptyTile.getBoardY());
							// if was the last tile the game is ended
							if (listTiles.size() == 1) {
								end = true;
							}
							// if wasn't the last tile the game doesn't end
							if (listTiles.size() > 1) {
								game.listTiles.remove(0);
							}
							// Check the points for the closed castles and roads
							// Castles
							if (putTile.checkTileCharacteristics(TileCharacteristics.Castle)) {
								pointsCastles = checkTerminatedCharacteristics(putTile, TileCharacteristics.Castle,
										emptyTile.getBoardY() * colums + emptyTile.getBoardX(), 2);
							}
							// Roads
							if (putTile.checkTileCharacteristics(TileCharacteristics.Road)) {
								pointsRoads = checkTerminatedCharacteristics(putTile, TileCharacteristics.Road,
										emptyTile.getBoardY() * colums + emptyTile.getBoardX(), 1);
							}
							// print the points maded
							System.out.println("c: " + pointsCastles + " - r:  " + pointsRoads);
							// add points to the player
							setPointsPlayer(pointsCastles + pointsRoads);
							// next player's turn
							nextPlayer();
							// reset points 
							pointsCastles = 0;
							pointsRoads = 0;
						}
					}
				}
			}
		});
	}
	//check for Terminated Characteristics
	private int checkTerminatedCharacteristics(Tile tile, TileCharacteristics characteristic, int idx, int points) {
		int check = 0;
		if (tile.getTileCharacteristics(Side.North).equals(characteristic)) {
			if (idx - colums > 0 && getComponent(idx - colums) instanceof Tile) {
				tileNorth = (Tile) getComponent(idx - colums);
				if (tileNorth.getPointsSide(Side.South) == 0) {
					tileNorth.setPointsSide(Side.South, points);
					if (tileNorth.getSpecialCharacteristic().equals("nc")) {
						if (characteristic.equals(TileCharacteristics.Castle))
							tile.setPointsCharacteristic(characteristic, points);
						else
							tile.setPointsCharacteristic(characteristic, points * 2);
						tile.setPointsSide(Side.North, points);
					} else {
						if (tileNorth.getSpecialPoints().equals("2")
								? tileNorth.getNumCharacteristics(characteristic) * (2 * points) > tileNorth
										.getPointsCharacteristic(characteristic)
								: tileNorth.getNumCharacteristics(characteristic) * points > tileNorth
										.getPointsCharacteristic(characteristic)) {
							check = checkNorth(tileNorth, tile, characteristic, idx - colums, points);
							if (check != 0) {
								if (characteristic.equals(TileCharacteristics.Road))
									tile.setPointsCharacteristic(characteristic, check + points);
								else
									tile.setPointsCharacteristic(characteristic, check);
								tile.setPointsSide(Side.North, points);
							} else {
								if (characteristic.equals(TileCharacteristics.Road))
									tile.setPointsSide(Side.North, 0);
								else
									tile.setPointsSide(Side.North, points);
							}
						} else {
							tile.setPointsCharacteristic(characteristic,
									tileNorth.getPointsCharacteristic(characteristic) + points);
							tile.setPointsSide(Side.North, points);
						}
					}
				} else if (characteristic.equals(TileCharacteristics.Road)
						&& !tile.getSpecialCharacteristic().equals("nc")
						&& tileNorth.getPointsCharacteristic(characteristic) != 0) {
					tile.setPointsSide(Side.North, points);
				} else {
					if (characteristic.equals(TileCharacteristics.Road)
							&& tileNorth.getPointsAllSidesWithCharacteristic(
									characteristic) < tileNorth.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.North, 0);
					else
						tile.setPointsSide(Side.North, points);
				}
			}
		}
		if (tile.getTileCharacteristics(Side.East).equals(characteristic)) {
			if (idx + 1 < getComponents().length && getComponent(idx + 1) instanceof Tile) {
				tileEast = (Tile) getComponent(idx + 1);
				if (tileEast.getPointsSide(Side.West) == 0) {
					tileEast.setPointsSide(Side.West, points);
					if (tileEast.getSpecialCharacteristic().equals("nc")) {
						if (characteristic.equals(TileCharacteristics.Castle))
							tile.setPointsCharacteristic(characteristic, points);
						else
							tile.setPointsCharacteristic(characteristic, points * 2);
						tile.setPointsSide(Side.East, points);
					} else {
						if (tileEast.getSpecialPoints().equals("2")
								? tileEast.getNumCharacteristics(characteristic) * (2 * points) > tileEast
										.getPointsCharacteristic(characteristic)
								: tileEast.getNumCharacteristics(characteristic) * points > tileEast
										.getPointsCharacteristic(characteristic)) {
							check = checkEast(tileEast, characteristic, idx + 1, points, tile);
							if (check != 0) {
								if (characteristic.equals(TileCharacteristics.Road))
									tile.setPointsCharacteristic(characteristic, check + points);
								else
									tile.setPointsCharacteristic(characteristic, check);
								tile.setPointsSide(Side.East, points);
							} else {
								if (characteristic.equals(TileCharacteristics.Road))
									tile.setPointsSide(Side.East, 0);
								else
									tile.setPointsSide(Side.East, points);
							}
						} else {
							tile.setPointsCharacteristic(characteristic,
									tileEast.getPointsCharacteristic(characteristic) + points);
							tile.setPointsSide(Side.East, points);
						}
					}
				} else if (!tile.getSpecialCharacteristic().equals("nc")
						&& tileEast.getPointsCharacteristic(characteristic) != 0
						&& characteristic.equals(TileCharacteristics.Road)) {
					tile.setPointsSide(Side.East, points);
				} else {
					if (characteristic.equals(TileCharacteristics.Road) && tileEast.getPointsAllSidesWithCharacteristic(
							characteristic) < tileEast.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.East, 0);
					else
						tile.setPointsSide(Side.East, points);
				}
			}
		}
		if (tile.getTileCharacteristics(Side.South).equals(characteristic)) {
			if (idx + colums < getComponents().length && getComponent(idx + colums) instanceof Tile) {
				tileSouth = (Tile) getComponent(idx + colums);
				if (tileSouth.getPointsSide(Side.North) == 0) {
					tileSouth.setPointsSide(Side.North, points);
					if (tileSouth.getSpecialCharacteristic().equals("nc")) {
						if (characteristic.equals(TileCharacteristics.Castle))
							tile.setPointsCharacteristic(characteristic, points);
						else
							tile.setPointsCharacteristic(characteristic, points * 2);
						tile.setPointsSide(Side.South, points);
					} else {
						if (tileSouth.getSpecialPoints().equals("2")
								? tileSouth.getNumCharacteristics(characteristic) * (2 * points) > tileSouth
										.getPointsCharacteristic(characteristic)
								: tileSouth.getNumCharacteristics(characteristic) * points > tileSouth
										.getPointsCharacteristic(characteristic)) {
							check = checkSouth(tileSouth, characteristic, idx + colums, points, tile);
							if (check != 0) {
								if (characteristic.equals(TileCharacteristics.Road))
									tile.setPointsCharacteristic(characteristic, check + points);
								else
									tile.setPointsCharacteristic(characteristic, check);
								tile.setPointsSide(Side.South, points);
							} else {
								tile.setPointsSide(Side.South, 0);
							}
						} else {
							tile.setPointsCharacteristic(characteristic,
									tileSouth.getPointsCharacteristic(characteristic) + points);
							tile.setPointsSide(Side.South, points);
						}
					}
				} else if (characteristic.equals(TileCharacteristics.Road)
						&& !tile.getSpecialCharacteristic().equals("nc")
						&& tileSouth.getPointsCharacteristic(characteristic) != 0) {
					tile.setPointsSide(Side.South, points);
				} else {
					if (characteristic.equals(TileCharacteristics.Road)
							&& tileSouth.getPointsAllSidesWithCharacteristic(
									characteristic) < tileSouth.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.South, 0);
					else
						tile.setPointsSide(Side.South, points);
				}
			}
		}
		if (tile.getTileCharacteristics(Side.West).equals(characteristic)) {
			if (idx - 1 > 0 && getComponent(idx - 1) instanceof Tile) {
				tileWest = (Tile) getComponent(idx - 1);
				if (tileWest.getPointsSide(Side.East) == 0) {
					tileWest.setPointsSide(Side.East, points);
					if (tileWest.getSpecialCharacteristic().equals("nc")) {
						if (characteristic.equals(TileCharacteristics.Castle))
							tile.setPointsCharacteristic(characteristic, points);
						else
							tile.setPointsCharacteristic(characteristic, points * 2);
						tile.setPointsSide(Side.West, points);
					} else {
						if (tileWest.getSpecialPoints().equals("2")
								? tileWest.getNumCharacteristics(characteristic) * (2 * points) > tileWest
										.getPointsCharacteristic(characteristic)
								: tileWest.getNumCharacteristics(characteristic) * points > tileWest
										.getPointsCharacteristic(characteristic)) {
							check = checkWest(tileWest, characteristic, idx - 1, points, tile);
							if (check != 0) {
								if (characteristic.equals(TileCharacteristics.Road))
									tile.setPointsCharacteristic(characteristic, check + points);
								else
									tile.setPointsCharacteristic(characteristic, check);
								tile.setPointsSide(Side.West, points);
							} else {
								tile.setPointsSide(Side.West, 0);
							}
						} else {
							tile.setPointsCharacteristic(characteristic,
									tileWest.getPointsCharacteristic(characteristic) + points);
							tile.setPointsSide(Side.West, points);
						}
					}
				} else if (characteristic.equals(TileCharacteristics.Road)
						&& !tile.getSpecialCharacteristic().equals("nc")
						&& tileWest.getPointsCharacteristic(characteristic) != 0) {
					tile.setPointsSide(Side.West, points);
				} else {
					if (characteristic.equals(TileCharacteristics.Road) && tileWest.getPointsAllSidesWithCharacteristic(
							characteristic) < tileWest.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.West, 0);
					else
						tile.setPointsSide(Side.West, points);
				}
			}
		}
		// se a peça tiver pontuacão num lado mas noutro ainda tiver zero, não
		// quero returnar os pontos da caracteristica
		if (!tile.getSpecialCharacteristic().equals("nc")) {
			// if (tile.getTileCharacteristics(Side.West).equals(characteristic)
			// && tile.getPointsSide(Side.West) == 0)
			// return 0;
			// if
			// (tile.getTileCharacteristics(Side.North).equals(characteristic)
			// && tile.getPointsSide(Side.North) == 0)
			// return 0;
			// if (tile.getTileCharacteristics(Side.East).equals(characteristic)
			// && tile.getPointsSide(Side.East) == 0)
			// return 0;
			// if
			// (tile.getTileCharacteristics(Side.South).equals(characteristic)
			// && tile.getPointsSide(Side.South) == 0)
			// return 0;
			if (tile.getNumCharacteristics(characteristic) * points > tile
					.getPointsAllSidesWithCharacteristic(characteristic))
				return 0;

		}
		if (tile.getSpecialCharacteristic().equals("nc") || tile.getSpecialCharacteristic().equals("i")
				|| characteristic.equals(TileCharacteristics.Road)
						&& tile.getSpecialCharacteristic().equals("RoadEnd")) {
			if (tile.getPointsAllSidesWithCharacteristic(characteristic) == 0)
				return 0;

		}
		if (characteristic.equals(TileCharacteristics.Castle) && tile.getPointsCharacteristic(characteristic) != 0)
			tile.setPointsCharacteristic(characteristic, points);
		if (tile.getSpecialCharacteristic().equals("nc")) {
			int numCharacteristics = tile.getNumCharacteristics(characteristic);
			numCharacteristics = Math.round((float) numCharacteristics / 2);
			if (tile.getPointsCharacteristic(characteristic) < numCharacteristics)
				return 0;
		}

		return tile.getPointsCharacteristic(characteristic);

	}

	private int checkNorth(Tile tile, Tile initialTile, TileCharacteristics characteristic, int idx, int points) {
		if (tile.getSpecialCharacteristic().equals("nc") || tile.getSpecialCharacteristic().equals("i")
				|| characteristic.equals(TileCharacteristics.Road)
						&& tile.getSpecialCharacteristic().equals("RoadEnd")) {
			tile.setPointsCharacteristic(characteristic, points);
			return tile.getPointsCharacteristic(characteristic);
		}

		if (tile.getTileCharacteristics(Side.North).equals(characteristic)) {
			if (idx - colums > 0 && getComponent(idx - colums) instanceof Tile) {
				tileNorth = (Tile) getComponent(idx - colums);
				if (characteristic.equals(TileCharacteristics.Road) && tileNorth == initialTile) {
					tile.setPointsCharacteristic(characteristic, points);
					tile.setPointsSide(Side.North, points);
					tileNorth.setPointsSide(Side.South, points);
					return points;
				}
				if (tileNorth.getPointsSide(Side.South) == 0) {
					tileNorth.setPointsSide(Side.South, points);
					if (tileNorth.getSpecialPoints().equals("2")
							? tileNorth.getNumCharacteristics(characteristic) * (2 * points) > tileNorth
									.getPointsCharacteristic(characteristic)
							: tileNorth.getNumCharacteristics(characteristic) * points > tileNorth
									.getPointsCharacteristic(characteristic)) {
						int check = checkNorth(tileNorth, initialTile, characteristic, idx - colums, points);
						if (check != 0) {
							if (characteristic.equals(TileCharacteristics.Road))
								tile.setPointsCharacteristic(characteristic, check + points);
							else
								tile.setPointsCharacteristic(characteristic, check);
							tile.setPointsSide(Side.North, points);
						} else {
							tile.setPointsSide(Side.North, 0);
						}
					} else {
						tile.setPointsCharacteristic(characteristic,
								tileNorth.getPointsCharacteristic(characteristic) + points);
						tile.setPointsSide(Side.North, points);
					}
				} else if (characteristic.equals(TileCharacteristics.Road)
						&& tileNorth.getPointsCharacteristic(characteristic) != 0) {
					tile.setPointsCharacteristic(characteristic, points);

				} else {
					if (characteristic.equals(TileCharacteristics.Road)
							&& tileNorth.getPointsAllSidesWithCharacteristic(
									characteristic) < tileNorth.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.North, 0);
					else
						tile.setPointsSide(Side.North, points);
				}
			}

		}
		if (idx + 1 < getComponents().length && getComponent(idx + 1) instanceof Tile
				&& tile.getTileCharacteristics(Side.East).equals(characteristic)) {
			tileEast = (Tile) getComponent(idx + 1);
			if (characteristic.equals(TileCharacteristics.Road) && tileEast == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.East, points);
				tileEast.setPointsSide(Side.West, points);
				return points;
			}
			if (tileEast.getPointsSide(Side.West) == 0) {
				tileEast.setPointsSide(Side.West, points);
				if (tileEast.getSpecialPoints().equals("2")
						? tileEast.getNumCharacteristics(characteristic) * (2 * points) > tileEast
								.getPointsCharacteristic(characteristic)
						: tileEast.getNumCharacteristics(characteristic) * points > tileEast
								.getPointsCharacteristic(characteristic)) {
					int check = checkEast(tileEast, characteristic, idx + 1, points, initialTile);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.East, points);
					} else {
						tile.setPointsSide(Side.East, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileEast.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.East, points);
				}
			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileEast.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.East, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileEast.getPointsAllSidesWithCharacteristic(
						characteristic) < tileEast.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.East, 0);
				else
					tile.setPointsSide(Side.East, points);
			}
		}
		if (idx - 1 > 0 && getComponent(idx - 1) instanceof Tile
				&& tile.getTileCharacteristics(Side.West).equals(characteristic)) {
			tileWest = (Tile) getComponent(idx - 1);
			if (characteristic.equals(TileCharacteristics.Road) && tileWest == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.West, points);
				tileWest.setPointsSide(Side.East, points);
				return points;
			}
			if (tileWest.getPointsSide(Side.East) == 0) {
				tileWest.setPointsSide(Side.East, points);
				if (tileWest.getSpecialPoints().equals("2")
						? tileWest.getNumCharacteristics(characteristic) * (2 * points) > tileWest
								.getPointsCharacteristic(characteristic)
						: tileWest.getNumCharacteristics(characteristic) * points > tileWest
								.getPointsCharacteristic(characteristic)) {
					int check = checkWest(tileWest, characteristic, idx - 1, points, initialTile);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.West, points);
					} else {
						tile.setPointsSide(Side.West, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileWest.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.West, points);
				}

			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileWest.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.West, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileWest.getPointsAllSidesWithCharacteristic(
						characteristic) < tileWest.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.West, 0);
				else
					tile.setPointsSide(Side.West, points);
			}
		}
		if (tile.getNumCharacteristics(characteristic) * points > tile
				.getPointsAllSidesWithCharacteristic(characteristic))
			return 0;
		if (tile.getPointsCharacteristic(characteristic) == 0)
			tile.setPointsCharacteristic(characteristic, points);

		return tile.getPointsCharacteristic(characteristic);
	}

	private int checkEast(Tile tile, TileCharacteristics characteristic, int idx, int points, Tile initialTile) {
		if (tile.getSpecialCharacteristic().equals("nc") || tile.getSpecialCharacteristic().equals("i")
				|| characteristic.equals(TileCharacteristics.Road)
						&& tile.getSpecialCharacteristic().equals("RoadEnd")) {
			tile.setPointsCharacteristic(characteristic, points);
			return points;
		}
		if (tile.getTileCharacteristics(Side.East).equals(characteristic)) {
			if (idx + 1 < getComponents().length && getComponent(idx + 1) instanceof Tile) {
				tileEast = (Tile) getComponent(idx + 1);
				if (characteristic.equals(TileCharacteristics.Road) && tileEast == initialTile) {
					tile.setPointsCharacteristic(characteristic, points);
					tile.setPointsSide(Side.East, points);
					tileEast.setPointsSide(Side.West, points);
					return points;
				}
				if (tileEast.getPointsSide(Side.West) == 0) {
					tileEast.setPointsSide(Side.West, points);
					if (tileEast.getSpecialPoints().equals("2")
							? tileEast.getNumCharacteristics(characteristic) * (2 * points) > tileEast
									.getPointsCharacteristic(characteristic)
							: tileEast.getNumCharacteristics(characteristic) * points > tileEast
									.getPointsCharacteristic(characteristic)) {
						int check = checkEast(tileEast, characteristic, idx + 1, points, initialTile);
						if (check != 0) {
							if (characteristic.equals(TileCharacteristics.Road))
								tile.setPointsCharacteristic(characteristic, check + points);
							else
								tile.setPointsCharacteristic(characteristic, check);
							tile.setPointsSide(Side.East, check);
						} else {
							tile.setPointsSide(Side.East, 0);
						}
					} else {
						tile.setPointsCharacteristic(characteristic,
								tileEast.getPointsCharacteristic(characteristic) + points);
						tile.setPointsSide(Side.East, points);
					}
				} else if (characteristic.equals(TileCharacteristics.Road)
						&& tileEast.getPointsCharacteristic(characteristic) != 0) {
					tile.setPointsSide(Side.East, points);
					tile.setPointsCharacteristic(characteristic, points);
				} else {
					if (characteristic.equals(TileCharacteristics.Road) && tileEast.getPointsAllSidesWithCharacteristic(
							characteristic) < tileEast.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.East, 0);
					else
						tile.setPointsSide(Side.East, points);
				}
			}

		}

		if (idx + colums < getComponents().length && getComponent(idx + colums) instanceof Tile
				&& tile.getTileCharacteristics(Side.South).equals(characteristic)) {
			tileSouth = (Tile) getComponent(idx + colums);
			if (characteristic.equals(TileCharacteristics.Road) && tileSouth == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.South, points);
				tileSouth.setPointsSide(Side.North, points);
				return points;
			}
			if (tileSouth.getPointsSide(Side.North) == 0) {
				tileSouth.setPointsSide(Side.North, points);
				if (tileSouth.getSpecialPoints().equals("2")
						? tileSouth.getNumCharacteristics(characteristic) * (2 * points) > tileSouth
								.getPointsCharacteristic(characteristic)
						: tileSouth.getNumCharacteristics(characteristic) * points > tileSouth
								.getPointsCharacteristic(characteristic)) {
					int check = checkSouth(tileSouth, characteristic, idx + colums, points, initialTile);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.South, points);
					} else {
						tile.setPointsSide(Side.South, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileSouth.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.South, points);
				}
			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileSouth.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.South, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileSouth.getPointsAllSidesWithCharacteristic(
						characteristic) < tileSouth.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.South, 0);
				else
					tile.setPointsSide(Side.South, points);
			}

		}
		if (idx - colums > 0 && getComponent(idx - colums) instanceof Tile
				&& tile.getTileCharacteristics(Side.North).equals(characteristic)) {
			tileNorth = (Tile) getComponent(idx - colums);
			if (characteristic.equals(TileCharacteristics.Road) && tileNorth == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.North, points);
				tileNorth.setPointsSide(Side.South, points);
				return points;
			}
			if (tileNorth.getPointsSide(Side.South) == 0) {
				tileNorth.setPointsSide(Side.South, points);
				if (tileNorth.getSpecialPoints().equals("2")
						? tileNorth.getNumCharacteristics(characteristic) * (2 * points) > tileNorth
								.getPointsCharacteristic(characteristic)
						: tileNorth.getNumCharacteristics(characteristic) * points > tileNorth
								.getPointsCharacteristic(characteristic)) {
					int check = checkNorth(tileNorth, initialTile, characteristic, idx - colums, points);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.North, points);
					} else {
						tile.setPointsSide(Side.North, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileNorth.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.North, points);
				}
			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileNorth.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.North, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileNorth.getPointsAllSidesWithCharacteristic(
						characteristic) < tileNorth.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.North, 0);
				else
					tile.setPointsSide(Side.North, points);
			}

		}
		if (tile.getNumCharacteristics(characteristic) * points > tile
				.getPointsAllSidesWithCharacteristic(characteristic))
			return 0;
		if (tile.getPointsCharacteristic(characteristic) == 0)
			tile.setPointsCharacteristic(characteristic, points);

		return tile.getPointsCharacteristic(characteristic);
	}

	private int checkSouth(Tile tile, TileCharacteristics characteristic, int idx, int points, Tile initialTile) {
		int check = 0;
		if (tile.getSpecialCharacteristic().equals("nc") || tile.getSpecialCharacteristic().equals("i")
				|| characteristic.equals(TileCharacteristics.Road)
						&& tile.getSpecialCharacteristic().equals("RoadEnd")) {
			tile.setPointsCharacteristic(characteristic, points);
			return points;
		}
		if (tile.getTileCharacteristics(Side.South).equals(characteristic)) {
			if (idx + colums < getComponents().length && getComponent(idx + colums) instanceof Tile) {
				tileSouth = (Tile) getComponent(idx + colums);
				if (characteristic.equals(TileCharacteristics.Road) && tileSouth == initialTile) {
					tile.setPointsCharacteristic(characteristic, points);
					tile.setPointsSide(Side.South, points);
					tileSouth.setPointsSide(Side.North, points);
					return points;
				}
				if (tileSouth.getPointsSide(Side.North) == 0) {
					tileSouth.setPointsSide(Side.North, points);
					if (tileSouth.getSpecialPoints().equals("2")
							? tileSouth.getNumCharacteristics(characteristic) * (2 * points) > tileSouth
									.getPointsCharacteristic(characteristic)
							: tileSouth.getNumCharacteristics(characteristic) * points > tileSouth
									.getPointsCharacteristic(characteristic)) {
						check = checkSouth(tileSouth, characteristic, idx + colums, points, initialTile);
						if (check != 0) {
							if (characteristic.equals(TileCharacteristics.Road))
								tile.setPointsCharacteristic(characteristic, check + points);
							else
								tile.setPointsCharacteristic(characteristic, check);
							tile.setPointsSide(Side.South, points);
						} else {
							tile.setPointsSide(Side.South, 0);
						}
					} else {
						tile.setPointsCharacteristic(characteristic,
								tileSouth.getPointsCharacteristic(characteristic) + points);
						tile.setPointsSide(Side.South, points);
					}
				} else if (characteristic.equals(TileCharacteristics.Road)
						&& tileSouth.getPointsCharacteristic(characteristic) != 0) {
					tile.setPointsSide(Side.South, points);
					tile.setPointsCharacteristic(characteristic, points);
				} else {
					if (characteristic.equals(TileCharacteristics.Road)
							&& tileSouth.getPointsAllSidesWithCharacteristic(
									characteristic) < tileSouth.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.South, 0);
					else
						tile.setPointsSide(Side.South, points);
				}
			}
		}
		if (idx + 1 < getComponents().length && getComponent(idx + 1) instanceof Tile
				&& tile.getTileCharacteristics(Side.East).equals(characteristic)) {
			tileEast = (Tile) getComponent(idx + 1);
			if (characteristic.equals(TileCharacteristics.Road) && tileEast == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.East, points);
				tileEast.setPointsSide(Side.West, points);
				return points;
			}
			if (tileEast.getPointsSide(Side.West) == 0) {
				tileEast.setPointsSide(Side.West, points);
				if (tileEast.getSpecialPoints().equals("2")
						? tileEast.getNumCharacteristics(characteristic) * (2 * points) > tileEast
								.getPointsCharacteristic(characteristic)
						: tileEast.getNumCharacteristics(characteristic) * points > tileEast
								.getPointsCharacteristic(characteristic)) {
					check = checkEast(tileEast, characteristic, idx + 1, points, initialTile);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.East, points);
					} else {
						tile.setPointsSide(Side.East, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileEast.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.East, points);
				}
			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileEast.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.East, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileEast.getPointsAllSidesWithCharacteristic(
						characteristic) < tileEast.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.East, 0);
				else
					tile.setPointsSide(Side.East, points);
			}
		}
		if (idx - 1 > 0 && getComponent(idx - 1) instanceof Tile
				&& tile.getTileCharacteristics(Side.West).equals(characteristic)) {
			tileWest = (Tile) getComponent(idx - 1);
			if (characteristic.equals(TileCharacteristics.Road) && tileWest == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.West, points);
				tileWest.setPointsSide(Side.East, points);
				return points;
			}
			if (tileWest.getPointsSide(Side.East) == 0) {
				tileWest.setPointsSide(Side.East, points);
				if (tileWest.getSpecialPoints().equals("2")
						? tileWest.getNumCharacteristics(characteristic) * (2 * points) > tileWest
								.getPointsCharacteristic(characteristic)
						: tileWest.getNumCharacteristics(characteristic) * points > tileWest
								.getPointsCharacteristic(characteristic)) {
					check = checkWest(tileWest, characteristic, idx - 1, points, initialTile);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.West, points);
					} else {
						tile.setPointsSide(Side.West, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileWest.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.West, points);
				}
			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileWest.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.West, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileWest.getPointsAllSidesWithCharacteristic(
						characteristic) < tileWest.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.West, 0);
				else
					tile.setPointsSide(Side.West, points);
			}
		}
		if (tile.getNumCharacteristics(characteristic) * points > tile
				.getPointsAllSidesWithCharacteristic(characteristic))
			return 0;
		if (tile.getPointsCharacteristic(characteristic) == 0)
			tile.setPointsCharacteristic(characteristic, points);
		return tile.getPointsCharacteristic(characteristic);
	}

	private int checkWest(Tile tile, TileCharacteristics characteristic, int idx, int points, Tile initialTile) {
		if (tile.getSpecialCharacteristic().equals("nc") || tile.getSpecialCharacteristic().equals("i")
				|| characteristic.equals(TileCharacteristics.Road)
						&& tile.getSpecialCharacteristic().equals("RoadEnd")) {
			tile.setPointsCharacteristic(characteristic, points);
			return points;
		}
		if (tile.getTileCharacteristics(Side.West).equals(characteristic)) {
			if (idx - 1 > 0 && getComponent(idx - 1) instanceof Tile) {
				tileWest = (Tile) getComponent(idx - 1);
				if (characteristic.equals(TileCharacteristics.Road) && tileWest == initialTile) {
					tile.setPointsCharacteristic(characteristic, points);
					tile.setPointsSide(Side.West, points);
					tileWest.setPointsSide(Side.East, points);
					return points;
				}
				if (tileWest.getPointsSide(Side.East) == 0) {
					tileWest.setPointsSide(Side.East, points);
					if (tileWest.getSpecialPoints().equals("2")
							? tileWest.getNumCharacteristics(characteristic) * (2 * points) > tileWest
									.getPointsCharacteristic(characteristic)
							: tileWest.getNumCharacteristics(characteristic) * points > tileWest
									.getPointsCharacteristic(characteristic)) {
						int check = checkWest(tileWest, characteristic, idx - 1, points, initialTile);
						if (check != 0) {
							if (characteristic.equals(TileCharacteristics.Road))
								tile.setPointsCharacteristic(characteristic, check + points);
							else
								tile.setPointsCharacteristic(characteristic, check);
							tile.setPointsSide(Side.West, points);
						} else {
							tile.setPointsSide(Side.West, 0);
						}
					} else {
						tile.setPointsCharacteristic(characteristic,
								tileWest.getPointsCharacteristic(characteristic) + points);
						tile.setPointsSide(Side.West, points);
					}
				} else if (characteristic.equals(TileCharacteristics.Road)
						&& tileWest.getPointsCharacteristic(characteristic) != 0) {
					tile.setPointsSide(Side.West, points);
					tile.setPointsCharacteristic(characteristic, points);
				} else {
					if (characteristic.equals(TileCharacteristics.Road) && tileWest.getPointsAllSidesWithCharacteristic(
							characteristic) < tileWest.getNumCharacteristics(characteristic) * points)
						tile.setPointsSide(Side.West, 0);
					else
						tile.setPointsSide(Side.West, points);
				}
			}
		}
		if (idx - colums > 0 && getComponent(idx - colums) instanceof Tile
				&& tile.getTileCharacteristics(Side.North).equals(characteristic)) {
			tileNorth = (Tile) getComponent(idx - colums);
			if (characteristic.equals(TileCharacteristics.Road) && tileNorth == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.North, points);
				tileNorth.setPointsSide(Side.South, points);
				return points;
			}
			if (tileNorth.getPointsSide(Side.South) == 0) {
				tileNorth.setPointsSide(Side.South, points);
				if (tileNorth.getSpecialPoints().equals("2")
						? tileNorth.getNumCharacteristics(characteristic) * (2 * points) > tileNorth
								.getPointsCharacteristic(characteristic)
						: tileNorth.getNumCharacteristics(characteristic) * points > tileNorth
								.getPointsCharacteristic(characteristic)) {
					int check = checkNorth(tileNorth, initialTile, characteristic, idx - colums, points);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.North, points);
					} else {
						tile.setPointsSide(Side.North, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileNorth.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.North, points);
				}
			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileNorth.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.North, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileNorth.getPointsAllSidesWithCharacteristic(
						characteristic) < tileNorth.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.North, 0);
				else
					tile.setPointsSide(Side.North, points);
			}
		}
		if (idx + colums < getComponents().length && getComponent(idx + colums) instanceof Tile
				&& tile.getTileCharacteristics(Side.South).equals(characteristic)) {
			tileSouth = (Tile) getComponent(idx + colums);
			if (characteristic.equals(TileCharacteristics.Road) && tileSouth == initialTile) {
				tile.setPointsCharacteristic(characteristic, points);
				tile.setPointsSide(Side.South, points);
				tileSouth.setPointsSide(Side.North, points);
				return points;
			}
			if (tileSouth.getPointsSide(Side.North) == 0) {
				tileSouth.setPointsSide(Side.North, points);
				if (tileSouth.getSpecialPoints().equals("2")
						? tileSouth.getNumCharacteristics(characteristic) * (2 * points) > tileSouth
								.getPointsCharacteristic(characteristic)
						: tileSouth.getNumCharacteristics(characteristic) * points > tileSouth
								.getPointsCharacteristic(characteristic)) {
					int check = checkSouth(tileSouth, characteristic, idx + colums, points, initialTile);
					if (check != 0) {
						if (characteristic.equals(TileCharacteristics.Road))
							tile.setPointsCharacteristic(characteristic, check + points);
						else
							tile.setPointsCharacteristic(characteristic, check);
						tile.setPointsSide(Side.South, points);
					} else {
						tile.setPointsSide(Side.South, 0);
					}
				} else {
					tile.setPointsCharacteristic(characteristic,
							tileSouth.getPointsCharacteristic(characteristic) + points);
					tile.setPointsSide(Side.South, points);
				}
			} else if (characteristic.equals(TileCharacteristics.Road)
					&& tileSouth.getPointsCharacteristic(characteristic) != 0) {
				tile.setPointsSide(Side.South, points);
				tile.setPointsCharacteristic(characteristic, points);
			} else {
				if (characteristic.equals(TileCharacteristics.Road) && tileSouth.getPointsAllSidesWithCharacteristic(
						characteristic) < tileSouth.getNumCharacteristics(characteristic) * points)
					tile.setPointsSide(Side.South, 0);
				else
					tile.setPointsSide(Side.South, points);
			}
		}
		if (tile.getNumCharacteristics(characteristic) * points > tile
				.getPointsAllSidesWithCharacteristic(characteristic))
			return 0;
		if (tile.getPointsCharacteristic(characteristic) == 0)
			tile.setPointsCharacteristic(characteristic, points);

		return tile.getPointsCharacteristic(characteristic);
	}

	private void nextPlayer() {
		game.getScorePanel().setnextPlayer();
	}


	private void setPointsPlayer(int points) {
		game.getScorePanel().getCurrentPlayer().setPoints(points);
	}
	// put tile on the board
	public Place putTile(Tile newTile, int x, int y) {
		Place oldPlace = removePlace(x, y);
		add(newTile, y * colums + x);
		return oldPlace;
	}
	// get the place in the board
	public Place getPlace(int x, int y) {
		return (Place) getComponent(y * colums + x);
	}
	// remove the place
	private Place removePlace(int x, int y) {
		Place place = getPlace(x, y);
		remove(y * colums + x);
		return place;
	}
	// add empty tile
	private void addEmptyTile(EmptyTile emptyTile, int x, int y) {
		add(emptyTile, y * colums + x);
	}

	/**
	 * colocar um marcador numa característica de uma peça;
	 * 
	 * @param t
	 */
	public void putMarker(Tile t) {

	}

	/**
	 * remover marcadores de uma característica numa peça e nas peças que dão
	 * continuidade a essa característica
	 * 
	 * @param t
	 */
	public void removeMarker(Tile t) {

	}

	/**
	 * limpar o tabuleiro
	 */
	public void cleanBoard() {

	}
	// zoom on the window
	private void updatePreferredSize(int n, Point p) {
		// idealmente getWheelRotation () nunca deve retornar 0. mas às vezes
		// ele retorna 0 durante a alteração da direção do zoom. Então se
		// obtemos 0 apenas inverter a direção. Por isso, se obtemos 0 apenas
		// inverter a direção. assimse obtemos 0 apenas inverter a direção.
		if (n == 0)
			n = -1 * prevN;

		double d = (double) n * 1.10;
		d = (n > 0) ? 1 / d : -d;

		int w = (int) (getWidth() * d);
		int h = (int) (getHeight() * d);
		// limitar o zoom
		if (w > 1000 && h > 600 && w < 2500 && h < 2000) {
			preferredSize.setSize(w, h);

			int offX = (int) (p.x * d) - p.x;
			int offY = (int) (p.y * d) - p.y;
			getParent().setLocation(getParent().getLocation().x - offX, getParent().getLocation().y - offY);
			// no código original, zoomPanel está sendo deslocado. aqui estamos
			// shifting containerPanel

			getParent().doLayout(); // Fazer o layout para containerPanel
			getParent().getParent().doLayout(); // Fazer o layout para jframe
			prevN = n;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return preferredSize;
	}

	private boolean SameSide(Tile tileCheck, int idx) {

		if (idx + colums < getComponents().length && getComponent(idx + colums) instanceof Tile) {
			Tile tile = (Tile) getComponent(idx + colums);
			if (!tile.getTileCharacteristics(Side.North).equals(tileCheck.getTileCharacteristics(Side.South))) {
				return false;
			}
		}

		if (idx - 1 > 0 && getComponent(idx - 1) instanceof Tile) {
			Tile tile = (Tile) getComponent(idx - 1);
			if (!tile.getTileCharacteristics(Side.East).equals(tileCheck.getTileCharacteristics(Side.West))) {
				return false;
			}

		}
		if (idx + 1 < getComponents().length && getComponent(idx + 1) instanceof Tile) {
			Tile tile = (Tile) getComponent(idx + 1);
			if (!tile.getTileCharacteristics(Side.West).equals(tileCheck.getTileCharacteristics(Side.East))) {
				return false;
			}

		}
		if (idx - colums > 0 && getComponent(idx - colums) instanceof Tile) {
			Tile tile = (Tile) getComponent(idx - colums);

			if (!tile.getTileCharacteristics(Side.South).equals(tileCheck.getTileCharacteristics(Side.North))) {
				return false;
			}

		}
		return true;
	}

	public void cleanAllPLaces() {
		for (int i = 0; i < getComponents().length; i++) {
			if (getComponent(i) instanceof EmptyTile) {
				((EmptyTile) getComponent(i)).setPut(false);
				((EmptyTile) getComponent(i)).setIcon(null);
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		boolean same1, same2, same3, same4;
		for (int i = 0; i < getComponents().length; i++) {
			if (getComponent(i) instanceof Tile) {
				if (i >= colums && getComponent(i - colums) instanceof EmptyTile) {
					same1 = SameSide(listTiles.get(0), i - colums);
					if (same1) {

						((EmptyTile) getComponent(i - colums)).setPut(true);
						// ((EmptyTile) getComponent(i -
						// colums)).setBackColor(Color.WHITE);
						((EmptyTile) getComponent(i - colums))
								.setIcon(new ImageIcon(getClass().getResource("img/ba.jpg")));
						// .setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,
						// 5));
					} else {
						((EmptyTile) getComponent(i - colums)).setPut(false);
						// ((EmptyTile) getComponent(i -
						// colums)).setBackColor(new Color(0, 140, 0));
						((EmptyTile) getComponent(i - colums))
								// .setBorder(new EmptyBorder(0, 0, 0, 0));
								.setIcon(null);
					}
				}
				if (i < getComponents().length - 1 && getComponent(i + 1) instanceof EmptyTile) {
					same2 = SameSide(listTiles.get(0), i + 1);
					if (same2) {
						((EmptyTile) getComponent(i + 1)).setPut(true);
						// ((EmptyTile) getComponent(i +
						// 1)).setBackColor(Color.WHITE);
						((EmptyTile) getComponent(i + 1)).setIcon(new ImageIcon(getClass().getResource("img/ba.jpg")));
						// .setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,
						// 5));

					} else {
						((EmptyTile) getComponent(i + 1)).setPut(false);
						// ((EmptyTile) getComponent(i + 1)).setBackColor(new
						// Color(0, 140, 0));
						((EmptyTile) getComponent(i + 1)).setIcon(null);
						// .setBorder(new EmptyBorder(0, 0, 0, 0));
					}
				}
				if (i <= getComponents().length - 1 - colums && getComponent(i + colums) instanceof EmptyTile) {
					same3 = SameSide(listTiles.get(0), i + colums);
					if (same3) {
						((EmptyTile) getComponent(i + colums)).setPut(true);
						// ((EmptyTile) getComponent(i +
						// colums)).setBackColor(Color.WHITE);
						((EmptyTile) getComponent(i + colums))
								.setIcon(new ImageIcon(getClass().getResource("img/ba.jpg")));
						// .setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,
						// 5));
					} else {
						((EmptyTile) getComponent(i + colums)).setPut(false);
						// ((EmptyTile) getComponent(i +
						// colums)).setBackColor(new Color(0, 140, 0));
						((EmptyTile) getComponent(i + colums)).setIcon(null);
						// setBorder(new EmptyBorder(0, 0, 0, 0));
					}
				}
				if (i > 0 && getComponent(i - 1) instanceof EmptyTile) {
					same4 = SameSide(listTiles.get(0), i - 1);
					if (same4) {
						((EmptyTile) getComponent(i - 1)).setPut(true);
						// ((EmptyTile) getComponent(i -
						// 1)).setBackColor(Color.WHITE);
						((EmptyTile) getComponent(i - 1)).setIcon(new ImageIcon(getClass().getResource("img/ba.jpg")));
						// .setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,
						// 5));
					} else {
						((EmptyTile) getComponent(i - 1)).setPut(false);
						// ((EmptyTile) getComponent(i - 1)).setBackColor(new
						// Color(0, 140, 0));
						((EmptyTile) getComponent(i - 1)).setIcon(null);
						// .setBorder(new EmptyBorder(0, 0, 0, 0));

					}
				}

			}

		}
		repaint();
	}
}
// if (pointsAux == 0)
// // detecao de caracteristicas terminadas
// // percorrer todas as peças no tabuleiro
// for (int i = 0; i < getComponents().length; i++)
// {
// // verificar aquelas que são mesmo peças e não
// // peças vazias
// if (getComponent(i) instanceof Tile) {
// // guardar em memoria essa peça
// tileCheck = (Tile) getComponent(i);
// // igreja
// if
// ((tileCheck.getCharacteristics(Side.South).equals(TileCharacteristics.Field)
// || tileCheck.getCharacteristics(Side.South)
// .equals(TileCharacteristics.Road))
// &&
// tileCheck.getCharacteristics(Side.West).equals(TileCharacteristics.Field)
// && tileCheck.getCharacteristics(Side.North)
// .equals(TileCharacteristics.Field)
// && tileCheck.getCharacteristics(Side.East)
// .equals(TileCharacteristics.Field)) {
// // se a caracteristica estiver fechada
// // atribuir os pontos ao jogador
// if (getComponent(i - 1) instanceof Tile &&
// getComponent(i + 1) instanceof Tile
// && getComponent(i + colums) instanceof Tile
// && getComponent(i - colums) instanceof Tile
// && getComponent(i + 1 + colums) instanceof Tile
// && getComponent(i - 1 + colums) instanceof Tile
// && getComponent(i - 1 - colums) instanceof Tile
// && getComponent(i + 1 - colums) instanceof Tile)
// {
// setPointsPlayer(9);
// }
// }
//
// }
// }