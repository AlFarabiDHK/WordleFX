package view;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import controller.WordleController;
import controller.WordleController.CorrectLengthException;
import controller.WordleController.NotInDictionaryException;
import controller.WordleController.OnlyLettersException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

public class WordleGUIView extends Application implements Observer{

	/* Constants for the scene */
	private static final int SCENE_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 10;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 75;
	private static final int LETTER_SQUARE_SIZE = 90;
	private static final int LETTER_BORDER_WIDTH = 2;
	
	private WordleModel model;
	private WordleController controller;
	
	@Override
	public void start(Stage stage) {
		model = new WordleModel();
		controller = new WordleController(model);
		Character[] guessedChar = controller.allChar();
		String[] keyboardTop = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
		String[] keyBoardMid = {"A", "S", "D", "F", "G", "H", "J","K","L"};
		String[] keyBoardBottom = { "Z","X","C","V","B","N","M"};
		int len = controller.getLen();
		int attempt = controller.getAllowedNumberOfGuesses();
		//add labels 
		
		Label[][] arrayOfLabels = new Label[attempt][len];
		
		for(int i = 0; i < attempt; i++) {
			for(int j = 0; j < len; j++) {
				arrayOfLabels[i][j] = new Label();
				
			}
		}
		
		
		VBox root = new VBox();
		Scene scene = new Scene(root, SCENE_SIZE, SCENE_SIZE);
		stage.setTitle("Wordle");
		Image icon = new Image("icons8-w-96.png");
		stage.getIcons().add(icon);
		String guess;
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			ArrayList<String> stack = new ArrayList<String>();
			
			String guess;
			
			@Override
			public void handle(KeyEvent ke) {
				
				if (ke.getCode().equals(KeyCode.DELETE) || ke.getCode().equals(KeyCode.BACK_SPACE)) {
					if(!stack.isEmpty())
						stack.remove(stack.size()-1);
				}
				
				else if(ke.getCode().equals(KeyCode.ENTER)) {
					
					guess = join(stack);
					System.out.println(guess);
					try {
						controller.makeGuess(guess);
					}
					catch (OnlyLettersException e) {
						String alertString = e.toString();
						Alert alert = new Alert(AlertType.INFORMATION,alertString, ButtonType.CLOSE);
						alert.showAndWait();
					}
					catch(NotInDictionaryException e) {
						String alertString = e.toString();
						Alert alert = new Alert(AlertType.INFORMATION,alertString, ButtonType.CLOSE);
						alert.showAndWait();
					}
					catch(CorrectLengthException e) {
						String alertString = e.toString();
						Alert alert = new Alert(AlertType.INFORMATION,alertString, ButtonType.CLOSE);
						alert.showAndWait();
						
					}
				}
				
				else {
					stack.add(ke.getCode().getName());
				}
				
				System.out.println(stack);
				
				
			}
			
			
			
		});
		
		
		
		

		/*
		
		
		GridPane gridTop = new GridPane();
		GridPane gridBottom = new GridPane();
		
		
		gridTop.add(label, 0, 0);
		
		scene.setOnKeyPressed(null);
		
		*/
		Label label = new Label();
		label.setText(guess);
		root.getChildren().add(label);
		
		stage.setScene(scene);
		stage.show();
		
		//String alertString = "Good game! The word was " + controller.getAnswer() + ".";
		//Alert alert = new Alert(AlertType.INFORMATION,alertString, ButtonType.CLOSE);
		//alert.showAndWait();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	private String join(ArrayList<String> s) {
		String result = "";
		for (String elem:s) {
			result += elem;
		}
		return result;
	}

}
