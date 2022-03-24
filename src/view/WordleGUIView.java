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
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;

public class WordleGUIView extends Application implements Observer{

	/* Constants for the scene */
	private static final int SCENE_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 10;
	private static final int KEY_GRID_GAP = 10;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 75;
	private static final int KEYBOARD_FONT_SIZE = 15;
	private static final int LETTER_SQUARE_SIZE = 90;
	private static final int KEYBOARD_SQUARE_SIZE = 40;
	private static final int LETTER_BORDER_WIDTH = 2;
	private static final int KEYBOARD_BORDER_WIDTH = 1;
	private static final int LETTER_BORDER_RADIUS = 4;
	private static final int KEYBOARD_BORDER_RADIUS = 2;
	private static final int ALPHABET_COUNT = 26;
	private WordleModel model;
	private WordleController controller;
	private String[] keyboard = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
										"A", "S", "D", "F", "G", "H", "J","K","L",
											"Z","X","C","V","B","N","M"};
	private Character[] guessedChar;
	private Guess[] progress;
	private INDEX_RESULT[] guessedIndexResult;
	private VBox root;
	private Scene scene;
	private Image icon;
	private Background defaultBackground;
	private Background keyboardBackground;
	private Background greenBackground;
	private Background yellowBackground;
	private Background greenBackgroundBig;
	private Background yellowBackgroundBig;
	private Background darkGreyBackground;
	private Background afterEnterBackground;
	private static Label[][] arrayOfLabels;
	private static Label[] arrayOfKeyBoardLetters;
	private GridPane gridTop;
	private GridPane gridBottom;
	private static int len;
	private static int attempt;
	private static String labelStyleDefault;
	private static String keyboardStyleDefault;
	
	@Override
	public void start(Stage stage) {
		this.fieldInitialization();
		this.setters(stage);
		this.populateArrays();
		EventHandler<KeyEvent> keyboardInput = new KeyboardInputHandler();
		scene.setOnKeyPressed(keyboardInput);
		
		stage.setScene(scene);
		stage.show();
	}

	private class KeyboardInputHandler implements EventHandler<KeyEvent>{
		
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
		}
		
		
	}
	@Override
	public void update(Observable o, Object arg) {
		guessedChar = controller.allChar();
		progress = model.getProgress();
		guessedIndexResult = model.getGuessedCharacters();
		ArrayList<Character> unguessed = new ArrayList<Character>();
		ArrayList<Character> incorrect = new ArrayList<Character>();
		ArrayList<Character> correct = new ArrayList<Character>();
		ArrayList<Character> correctWrong = new ArrayList<Character>();
		this.toStringProgress(progress, controller);
		this.colorizeChar(guessedChar, guessedIndexResult,unguessed,
				incorrect, correct, correctWrong);
		//String alertString = "Good game! The word was " + controller.getAnswer() + ".";
		//Alert alert = new Alert(AlertType.INFORMATION,alertString, ButtonType.CLOSE);
		//alert.showAndWait();
		
	}
	
	private int search(Character target) {
		for(int i = 0; i < keyboard.length; i++) {
			if(keyboard[i].equals(Character.toString(target))) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void colorizeChar(Character[] guessedChar, INDEX_RESULT[] guessedIndexResult, ArrayList<Character> unguessed,
			ArrayList<Character> incorrect, ArrayList<Character> correct, ArrayList<Character> correctWrong) {
		
		for(int i = 0; i < guessedIndexResult.length; i++) {
			Character temp = guessedChar[i];
			int index = search(temp);
			if(guessedIndexResult[i] == null)
				unguessed.add(guessedChar[i]);
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.CORRECT)==0) {
				correct.add(guessedChar[i]);
				
				arrayOfKeyBoardLetters[index].setBackground(greenBackground);
			}
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX)==0) {
				correctWrong.add(guessedChar[i]);
				arrayOfKeyBoardLetters[index].setBackground(yellowBackground);
			}
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.INCORRECT)==0) {
				incorrect.add(guessedChar[i]);
				arrayOfKeyBoardLetters[index].setBackground(darkGreyBackground);
			}
			
		}
		
		if(unguessed.size() != 0) {
			System.out.println("Unguessed " + unguessed);
		}
		if(incorrect.size() != 0)
			System.out.println(INDEX_RESULT.INCORRECT.getDescription() + " " + incorrect);
		if(correct.size() != 0)
			System.out.println(INDEX_RESULT.CORRECT.getDescription() + " " + correct);
		if(correctWrong.size() != 0)
			System.out.println(INDEX_RESULT.CORRECT_WRONG_INDEX.getDescription() + " " + correctWrong);
		System.out.println();
	}
	
	public void toStringProgress(Guess[] progress, WordleController controller) {
		for(int i = 0; i < attempt; i++) {
			if(progress[i] != null) 
			{
				INDEX_RESULT[] index = progress[i].getIndices();
				String guess = progress[i].getGuess();
				
				for(int j = 0; j < len; j++) {
					if(index[j].compareTo(INDEX_RESULT.CORRECT) == 0) {
						String s = Character.toUpperCase(guess.charAt(j)) + " ";
						System.out.print(s);
						arrayOfLabels[i][j].setBackground(greenBackgroundBig);
					}
					else if (index[j].compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX) == 0) {
						String s = Character.toLowerCase(guess.charAt(j)) + " ";
						System.out.print(s);
						arrayOfLabels[i][j].setBackground(yellowBackgroundBig);
					}
					else { 
						System.out.print("_ ");
						arrayOfLabels[i][j].setBackground(afterEnterBackground);
					}
						
				}
				System.out.println();
			}
			else System.out.println("_ _ _ _ _");
		}
	}
		
	
	
	private void fieldInitialization() {
		model = new WordleModel();
		model.setAnswer("MAYBE");
		model.addObserver(this);
		controller = new WordleController(model);
		CornerRadii letterCorner = new CornerRadii(LETTER_BORDER_RADIUS);
		CornerRadii keyCorner = new CornerRadii(KEYBOARD_BORDER_RADIUS);
		Insets inset = Insets.EMPTY;
		defaultBackground = new Background(
				new BackgroundFill(Color.BLACK, letterCorner, inset));
		afterEnterBackground = new Background(
				new BackgroundFill(Color.web("#3a3a3c"), letterCorner, inset));
		greenBackground = new Background(
				new BackgroundFill(Color.web("#538d4e"), keyCorner, inset));
		yellowBackground = new Background(
				new BackgroundFill(Color.web("#b59f3b"), keyCorner, inset));
		keyboardBackground = new Background(
				new BackgroundFill(Color.web("#818384") , keyCorner, inset));
		darkGreyBackground  = new Background(
				new BackgroundFill(Color.web("#3a3a3c") , keyCorner, inset));
		greenBackgroundBig = new Background(
				new BackgroundFill(Color.web("#538d4e"), letterCorner, inset));
		yellowBackgroundBig = new Background(
				new BackgroundFill(Color.web("#b59f3b"), letterCorner, inset));
		len = controller.getLen();
		attempt = controller.getAllowedNumberOfGuesses();
		labelStyleDefault = "-fx-border-color: black;"
				+ "-fx-border-width: "
				+ Integer.toString(LETTER_BORDER_WIDTH)
				+ "; -fx-border-style: solid;"
				+ "-fx-border-color: white;"
				+ "-fx-border-radius:" + Integer.toString(LETTER_BORDER_RADIUS)
				+ ";-fx-margin:" + Integer.toString(GRID_GAP) + ";"
				+ "-fx-margin: 10px;";
		
		keyboardStyleDefault = "-fx-border-color: black;"
				+ "-fx-border-width: "
				+ Integer.toString(KEYBOARD_BORDER_WIDTH)
				+ "; -fx-border-style: solid;"
				+ "-fx-border-color: grey;"
				+ "-fx-border-radius:" + Integer.toString(KEYBOARD_BORDER_RADIUS)
				+ ";-fx-margin:" + Integer.toString(GRID_GAP) + ";"
				+ "-fx-margin: 30px;"
				+ "-fx-font-weight: bolder;";
		
		root = new VBox();
		scene = new Scene(root, SCENE_SIZE, SCENE_SIZE);
		gridTop = new GridPane();
		arrayOfLabels = new Label[attempt][len];
		gridBottom = new GridPane();
		arrayOfKeyBoardLetters = new Label[ALPHABET_COUNT];
	}
	
	private void populateArrays() {

		for(int i = 0; i < attempt; i++) {
			for(int j = 0; j < len; j++) {
				arrayOfLabels[i][j] = new Label();
				arrayOfLabels[i][j].setMinHeight(LETTER_SQUARE_SIZE);
				arrayOfLabels[i][j].setMinWidth(LETTER_SQUARE_SIZE);
				arrayOfLabels[i][j].setTextFill(Color.WHITE);
				arrayOfLabels[i][j].setLineSpacing(GRID_GAP);
				arrayOfLabels[i][j].setFont(Font.font("Arial", LETTER_FONT_SIZE));
				arrayOfLabels[i][j].setStyle(labelStyleDefault);
				arrayOfLabels[i][j].setAlignment(Pos.CENTER);
				gridTop.add(arrayOfLabels[i][j], j, i);
			}
		}

		int y_pos = 0;
		int x_pos = 0;
		for(int i = 0; i < ALPHABET_COUNT; i++) {
			arrayOfKeyBoardLetters[i] = new Label(keyboard[i]);
			
			if (i == 10) { 
				y_pos++;
				x_pos = 0;
				}
			if(i == 19) {
				y_pos++;
				x_pos = 0;
			}
			arrayOfKeyBoardLetters[i].setMinHeight(KEYBOARD_SQUARE_SIZE);
			arrayOfKeyBoardLetters[i].setMinWidth(KEYBOARD_SQUARE_SIZE);
			arrayOfKeyBoardLetters[i].setTextFill(Color.WHITE);
			arrayOfKeyBoardLetters[i].setLineSpacing(GRID_GAP);
			arrayOfKeyBoardLetters[i].setFont(Font.font("Arial", KEYBOARD_FONT_SIZE));
			arrayOfKeyBoardLetters[i].setStyle(keyboardStyleDefault);
			arrayOfKeyBoardLetters[i].setAlignment(Pos.CENTER);
			arrayOfKeyBoardLetters[i].setBackground(keyboardBackground);
			gridBottom.add(arrayOfKeyBoardLetters[i], x_pos, y_pos);
			x_pos++;
		}
		
	}
	
	private void setters(Stage stage)
	{
		root.setBackground(defaultBackground);
		
		stage.setTitle("Wordle");
		icon = new Image("icons8-w-96.png");
		stage.getIcons().add(icon);
		
		
		gridTop.setAlignment(Pos.CENTER);
		gridTop.setHgap(GRID_GAP); 
		gridTop.setVgap(GRID_GAP); 
		gridTop.setPadding(new Insets(GRID_GAP,GRID_GAP, GRID_GAP, GRID_GAP));
		root.setAlignment(Pos.CENTER);
		root.getChildren().add(gridTop);
		gridBottom.setAlignment(Pos.CENTER);
		gridBottom.setHgap(KEY_GRID_GAP); 
		gridBottom.setVgap(KEY_GRID_GAP); 
		gridBottom.setPadding(new Insets(KEY_GRID_GAP,KEY_GRID_GAP, KEY_GRID_GAP, KEY_GRID_GAP));
		root.getChildren().add(gridBottom);
		
	}
	
	private String join(ArrayList<String> s) {
		String result = "";
		for (String elem:s) {
			result += elem;
		}
		return result;
	}

}
