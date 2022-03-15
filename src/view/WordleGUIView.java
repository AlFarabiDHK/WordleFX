package view;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
		
		String input = "";
		/*
		KeyEvent ke = new KeyEvent(null, null, null, null, false, false, false, false);
		if (ke.getCode().equals(KeyCode.DELETE) || ke.getCode().equals(KeyCode.BACK_SPACE)) {
			
		}
		
		else if(ke.getCode().equals(KeyCode.ENTER)) {
			
		}
		
		else {
			input = ke.getCode().getName();
		}
		*/
		GridPane gridTop = new GridPane();
		GridPane gridBottom = new GridPane();
		String[] keyboardList = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
								};
		Label label = new Label(input);
		gridTop.add(label, 0, 0);
		
		scene.setOnKeyPressed(null);
		stage.setScene(scene);
		stage.show();
	}

}
