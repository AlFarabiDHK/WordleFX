package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class WordleGUIView extends Application {

	/* Constants for the scene */
	private static final int SCENE_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 10;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 75;
	private static final int LETTER_SQUARE_SIZE = 90;
	private static final int LETTER_BORDER_WIDTH = 2;

	@Override
	public void start(Stage stage) {
		BorderPane window = new BorderPane();
		Scene scene = new Scene(window, SCENE_SIZE, SCENE_SIZE);
		stage.setTitle("Wordle");
		stage.setScene(scene);
		stage.show();
	}

}
