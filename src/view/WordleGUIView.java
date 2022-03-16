package view;


import controller.WordleController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.WordleModel;

public class WordleGUIView extends Application {

	/* Constants for the scene */
	private static final int SCENE_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 10;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 75;
	private static final int LETTER_SQUARE_SIZE = 90;
	private static final int LETTER_BORDER_WIDTH = 2;
	
	private WordleModel model = new WordleModel();
	private WordleController controller = new WordleController(model);

	@Override
	public void start(Stage stage) {
		VBox v = new VBox();
		Scene scene = new Scene(v, SCENE_SIZE, SCENE_SIZE);
		stage.setTitle("Wordle");
		Image icon = new Image("icons8-w-96.png");
		stage.getIcons().add(icon);
		Text txt = new Text();
		txt.setText("Wordle");
		txt.setX(SCENE_SIZE/2);
		txt.setY(SCENE_SIZE/2);
		
		v.getChildren().add(txt);
		;
		/*
		KeyEvent ke = new KeyEvent(null, null, null, null, false, false, false, false);
		if (ke.getCode().equals(KeyCode.DELETE) || ke.getCode().equals(KeyCode.BACK_SPACE)) {
			
		}
		
		else if(ke.getCode().equals(KeyCode.ENTER)) {
			
		}
		
		else {
			input = ke.getCode().getName();
		}
		
		
		GridPane gridTop = new GridPane();
		GridPane gridBottom = new GridPane();
		String[] keyboard = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
								"A", "S", "D", "F", "G", "H", "J","K","L",
								 "Z","X","C","V","B","N","M"
								};
		Label label = new Label("yay");
		gridTop.add(label, 0, 0);
		
		scene.setOnKeyPressed(null);
		
		*/
		stage.setScene(scene);
		stage.show();
	}

}
