package tps.tp4.carcassonne;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	String userDir = System.getProperty("user.dir");
	private String path = userDir + "\\src\\tps\\tp4\\carcassonne\\font\\carc.ttf";
	public static AudioClip sound;

	private Parent creatContent(Stage primary) {
		Pane root = new Pane();
		root.setPrefSize(1300, 700);
		InputStream is1 = null, is2 = null;
		try {
			is1 = Files.newInputStream(
					Paths.get(userDir + "\\src\\tps\\tp4\\carcassonne\\img\\ha.png"));
			is2 = Files.newInputStream(
					Paths.get(userDir + "\\src\\tps\\tp4\\carcassonne\\img\\haa.png"));

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Image cursor1 = new Image(is1);
		Image cursor2 = new Image(is2);
		ImageCursor cursorNormal = new ImageCursor(cursor1, 100, 100);
		ImageCursor cursorPressed = new ImageCursor(cursor2, 100, 100);
		root.setCursor(cursorNormal);
		root.setOnMousePressed(EventHandler -> {
			root.setCursor(cursorPressed);
		});
		root.setOnMouseReleased(EventHandler -> {
			root.setCursor(cursorNormal);
		});
		// load image
		try (InputStream is = Files.newInputStream(
				Paths.get(userDir + "\\src\\tps\\tp4\\carcassonne\\img\\menu.png"))) {
			ImageView img = new ImageView(new Image(is));
			img.setFitWidth(1300);
			img.setFitHeight(700);
			root.getChildren().add(img);
		} catch (IOException e) {
			System.out.println("Couldn't load image");
		}

		// titulo do menu
		Title title = new Title("M E N U");
		title.setTranslateX(450);
		title.setTranslateY(100);
		// menu
		MenuBox menu = new MenuBox(new MenuItem("New Game", primary), new MenuItem("Options", primary), new MenuItem("Exit", primary));
		menu.setTranslateX(550);
		menu.setTranslateY(250);
		root.getChildren().addAll(title, menu);
		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(creatContent(primaryStage));
		primaryStage.setTitle("Carcassonne Menu");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static class Title extends StackPane {
		public Title(String name) {
			Main main = new Main();
			Rectangle bg = new Rectangle(450, 100);
			bg.setStroke(Color.WHITE);
			bg.setStrokeWidth(2);
			// nada dentro do rectangulo
			bg.setFill(null);

			Text text = new Text(name);
			text.setFill(Color.WHITE);
			try {
				text.setFont(Font.loadFont(new FileInputStream(new File(main.userDir + "\\src\\tps\\tp4\\carcassonne\\font\\carc.ttf")), 100));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} // font("carc.ttf", FontWeight.SEMI_BOLD, 50));
			setAlignment(Pos.CENTER);
			getChildren().addAll(bg, text);
		}
	}

	// vertical layout for items
	private static class MenuBox extends VBox {
		//
		public MenuBox(MenuItem... items) {
			getChildren().add(createSeparator());
			// adicionar todos os MenuItens que estiverem em items
			for (MenuItem menuItem : items) {
				getChildren().addAll(menuItem, createSeparator());
			}
		}

		private Line createSeparator() {
			Line sep = new Line();
			sep.setEndX(250);
			sep.setStroke(Color.DARKBLUE);
			return sep;
		}
	}

	private static class MenuItem extends StackPane {
		public MenuItem(String name, Stage primary) {
			Main main = new Main();
			sound = new AudioClip(this.getClass().getResource("music/menuSound.mp3").toString());
			sound.play();
			AudioClip select = new AudioClip(this.getClass().getResource("music/menuSelect.mp3").toString());
			AudioClip click = new AudioClip(this.getClass().getResource("music/click.mp3").toString());
			// definir gradiente
			LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
					new Stop[] { new Stop(0, Color.YELLOW), new Stop(0.1, Color.BLACK), new Stop(0.9, Color.BLACK), new Stop(1, Color.YELLOW) });

			Rectangle bg = new Rectangle(250, 45);
			bg.setOpacity(0.4);

			Text text = new Text(name);
			text.setFill(Color.DARKGRAY);
			try {
				text.setFont(Font.loadFont(new FileInputStream(new File(main.userDir + "\\src\\tps\\tp4\\carcassonne\\font\\carc.ttf")), 42));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			setAlignment(Pos.CENTER);
			getChildren().addAll(bg, text);

			setOnMouseEntered(EventHandler -> {
				select.play();
				bg.setFill(gradient);
				text.setFill(Color.WHITE);
			});

			setOnMouseExited(EventHandler -> {
				bg.setFill(Color.BLACK);
				text.setFill(Color.DARKGRAY);
			});

			setOnMousePressed(EventHandler -> {
				click.play();
				bg.setFill(Color.DARKGRAY);
				if (name.equals("New Game")) {
					primary.close();
					new PlayersMenu(primary);
				}
				if (name.equals("Exit")) {
					System.exit(1);
				}
				if (name.equals("Options")) {
					Platform.setImplicitExit(false);
					new Options(primary);
				}
			});
			setOnMouseReleased(EventHandler -> {
				bg.setFill(gradient);
			});
		}
	}

	public static void main(String[] args) {
		launch();
	}

}
