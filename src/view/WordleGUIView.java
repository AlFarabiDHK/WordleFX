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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
	private static final int LETTER_BORDER_RADIUS = 7;
	
	private WordleModel model;
	private WordleController controller;
	private String[] keyboardTop = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
	private String[] keyBoardMid = {"A", "S", "D", "F", "G", "H", "J","K","L"};
	private String[] keyBoardBottom = { "Z","X","C","V","B","N","M"};
	private static Background background;
	private VBox root;
	private Scene scene;
	private Image icon;
	private static Label[][] arrayOfLabels;
	private GridPane gridTop;
	private static int len;
	private static int attempt;
	private static String labelStyleDefault;
	
	@Override
	public void start(Stage stage) {
		model = new WordleModel();
		controller = new WordleController(model);
		Character[] guessedChar = controller.allChar();
		
		len = controller.getLen();
		attempt = controller.getAllowedNumberOfGuesses();
		labelStyleDefault = "-fx-border-color: black;"
				+ "-fx-border-width: "
				+ Integer.toString(LETTER_BORDER_WIDTH)
				+ "; -fx-border-style: solid;"
				+ "-fx-border-radius:" + Integer.toString(LETTER_BORDER_RADIUS)
				+ ";-fx-margin:" + Integer.toString(GRID_GAP) + ";"
				+ "-fx-margin: 10px;";
		//add labels 
		
		root = new VBox();
		scene = new Scene(root, SCENE_SIZE, SCENE_SIZE);
		stage.setTitle("Wordle");
		icon = new Image("icons8-w-96.png");
		stage.getIcons().add(icon);
		arrayOfLabels = new Label[attempt][len];
		gridTop = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.getChildren().add(gridTop);
		
		for(int i = 0; i < attempt; i++) {
			for(int j = 0; j < len; j++) {
				arrayOfLabels[i][j] = new Label();
				arrayOfLabels[i][j].setMinHeight(LETTER_SQUARE_SIZE);
				arrayOfLabels[i][j].setMinWidth(LETTER_SQUARE_SIZE);
				arrayOfLabels[i][j].setTextFill(Color.BLACK);
				arrayOfLabels[i][j].setLineSpacing(GRID_GAP);
				arrayOfLabels[i][j].setFont(Font.font("Arial", LETTER_FONT_SIZE));
				arrayOfLabels[i][j].setStyle(labelStyleDefault);
				arrayOfLabels[i][j].setAlignment(Pos.CENTER);
				//arrayOfLabels[i][j]
				gridTop.add(arrayOfLabels[i][j], j, i);
			}
		}
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			ArrayList<String> stack = new ArrayList<String>();
			
			String guess;
			int i = 0;
			int j = 0;
			
			@Override
			public void handle(KeyEvent ke) {
				
				if (ke.getCode().equals(KeyCode.DELETE) || ke.getCode().equals(KeyCode.BACK_SPACE)) {
					if(!stack.isEmpty()) {
						stack.remove(stack.size()-1);
						j--;
						WordleGUIView.arrayOfLabels[i][j].setText("");
						
					}
				}
				
				else if(ke.getCode().equals(KeyCode.ENTER)) {
					
					guess = join(stack);
					System.out.println(guess);
					try {
						controller.makeGuess(guess);
						stack.clear();
						j = 0;
						i++;
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
					if (j < WordleGUIView.len) {
						String tempLetter = ke.getCode().getName();
						stack.add(tempLetter);
					
						WordleGUIView.arrayOfLabels[i][j].setText(tempLetter);
						j++;
					}
					
				}
				
				System.out.println(stack);
				
				
			}
			
			
			
		});
		
		
		stage.setScene(scene);
		stage.show();
		
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
		//String alertString = "Good game! The word was " + controller.getAnswer() + ".";
		//Alert alert = new Alert(AlertType.INFORMATION,alertString, ButtonType.CLOSE);
		//alert.showAndWait();
		
	}
	
	private String join(ArrayList<String> s) {
		String result = "";
		for (String elem:s) {
			result += elem;
		}
		return result;
	}

}
