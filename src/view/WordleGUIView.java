package view;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import controller.WordleController;
import controller.WordleController.CorrectLengthException;
import controller.WordleController.NotInDictionaryException;
import controller.WordleController.OnlyLettersException;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;

/**
 * This class runs a Wordle game using a GUI made using JavaFX. 
 * @author Muhtasim Al-Farabi
 *
 */

public class WordleGUIView extends Application implements Observer{

	/* Constants for the scene */
	private static final int SCENE_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 10;
	private static final int KEY_GRID_GAP = 10;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 65;
	private static final int KEYBOARD_FONT_SIZE = 15;
	private static final int LETTER_SQUARE_SIZE = 90;
	private static final int KEYBOARD_SQUARE_SIZE = 40;
	private static final int LETTER_BORDER_WIDTH = 2;
	private static final int KEYBOARD_BORDER_WIDTH = 1;
	private static final int LETTER_BORDER_RADIUS = 4;
	private static final int KEYBOARD_BORDER_RADIUS = 2;
	private static final int ALPHABET_COUNT = 26;
	
	/* Fields related to the execution of Wordle */
	private WordleModel model;
	private WordleController controller;
	private static String[] keyboard = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
										"A", "S", "D", "F", "G", "H", "J","K","L",
											"Z","X","C","V","B","N","M"};
	private static int len;
	private static int attempt;
	private static String labelStyleDefault;
	private static String keyboardStyleDefault;
	private Character[] guessedChar;
	private Guess[] progress;
	private INDEX_RESULT[] guessedIndexResult;
	
	
	/* Different kinds of Background objects */
	private static Background defaultBackground;
	private static Background keyboardBackground;
	private static Background greenBackground;
	private static Background yellowBackground;
	private static Background greenBackgroundBig;
	private static Background yellowBackgroundBig;
	private static Background darkGreyBackground;
	private static Background afterEnterBackground;
	
	/* Arrays to hold labels */
	private static Label[][] arrayOfLabels;
	private static Label[] arrayOfKeyBoardLetters;
	
	/* Graphics objects */
	private static GridPane gridTop;
	private static GridPane gridBottom;
	private static VBox root;
	private static Scene scene;
	private static Image icon;

	/**
	 * Start method for the JavaFX scene
	 * 
	 * <p>
	 * 
	 * This method initializes all the field, sets all Node properties,
	 * and populates the arrays. The goal of this method is to initialize everything.
	 * It also uses the private keyEvent class to handle key presses from the user.
	 * @param stage a stage upon which each object will be placed
	 */
	
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
	
	/**
	 * This is a private class that handles keyboard input from the user. It handles
	 * the end animation and has a method called handle that gets called automatically.
	 * The purpose of this class is to be passed through the setOnKeyPressed method in
	 * the start method of the outer class and handle all the key press events
	 * through it. 
	 * 
	 * @author Muhtasim Al-Farabi
	 *
	 */

	private class KeyboardInputHandler implements EventHandler<KeyEvent>{
		
		private ArrayList<String> stack = new ArrayList<String>();
		private String guess;
		private int i = 0;
		private int j = 0;
		
		/**
		 * Animates the win 
		 * 
		 * <p>
		 * 
		 * This private method executes the end animation. It makes all the
		 * labels of the final/winning attempt jump up and down by 30 pixels
		 * for 250 milliseconds sequentially. 
		 * 
		 */
		
		private void animation() {
			int yJump = -30;
			int cycles = 2;
			int duration = 250;
			TranslateTransition transitionList[] = new TranslateTransition[WordleGUIView.len];
			for(int k = 0; k < WordleGUIView.len; k++) {
				transitionList[k] = new TranslateTransition(Duration.millis(duration));
				transitionList[k].setNode(WordleGUIView.arrayOfLabels[i][k]);
				transitionList[k].setByY(yJump);
				transitionList[k].setCycleCount(cycles);
				transitionList[k].setAutoReverse(true);
			}
			
			SequentialTransition seqT = new SequentialTransition(transitionList);
		     seqT.play();
		}
		

		/**
		 * Handles the KeyEvents
		 * 
		 * <p> 
		 * 
		 * This method takes key inputs from the user and runs different functions
		 * accordingly. If the user presses a letter, it displays the letter on the 
		 * Screen. If the user pressed delete, it deletes a letter from
		 * the key if the stack is not empty. When ENTER is pressed, it executes the
		 * makeGuess method and checks if the game is over or not. If the game is over
		 * and the user won, it calls the animation method. Then it alerts the user
		 * about the correct answer. This class throws exceptions as alerts.
		 * @param ke KeyEvent object that detects input
		 * 
		 */
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
					if(controller.isGameOver()) {
						--i;
						if(controller.didWin())
							this.animation();
						String alertString = "Good game! The word was " + controller.getAnswer() + ".";
						Alert alert = new Alert(AlertType.CONFIRMATION,alertString, ButtonType.OK);
						alert.showAndWait();
					}
				}
				catch (OnlyLettersException e) {
					String alertString = e.toString();
					Alert alert = new Alert(AlertType.WARNING,alertString, ButtonType.CLOSE);
					alert.showAndWait();
				}
				catch(NotInDictionaryException e) {
					String alertString = e.toString();
					Alert alert = new Alert(AlertType.WARNING,alertString, ButtonType.CLOSE);
					alert.showAndWait();
				}
				catch(CorrectLengthException e) {
					String alertString = e.toString();
					Alert alert = new Alert(AlertType.WARNING,alertString, ButtonType.CLOSE);
					alert.showAndWait();
					
				}
			}
			
			else if (j < WordleGUIView.len) {
					String tempLetter = ke.getCode().getName();
					stack.add(tempLetter);
					WordleGUIView.arrayOfLabels[i][j].setText(tempLetter);
					j++;
				}
			
			
		}
		
		
	}
	
	/**
	 * This method is called whenever the observed object is changed.
	 * 
	 * <p>
	 * This method is called whenever the observed object is changed. 
	 * An application calls an Observable object's notifyObservers method 
	 * to have all the object's observers notified of the change. Here,
	 * it runs the colorMain method and the colorizeChar method
	 * to display the appropriate changes. It also can access the
	 * getProgress method and the getGuessedCharacter method directly 
	 * from the model instead of the controller.
	 * 
	 * @param o the observable object
	 * @param arg an argument passed to the notifyObservers method
	 * 
	 */
	
	@Override
	public void update(Observable o, Object arg) {
		guessedChar = controller.allChar();
		progress = model.getProgress();
		guessedIndexResult = model.getGuessedCharacters();
		this.colorMain(progress, controller);
		this.colorizeChar(guessedChar, guessedIndexResult);
	}
	
	/**
	 * Called to perform search on the keyboard array field
	 * 
	 * <p>
	 * 
	 * This is a private method that finds a target's index
	 * in the keyboard array field. We need a search method
	 * because the keyboard letters are not in alphabetical
	 * order
	 * @param target the target letter to be found in the keyboard array
	 * @return the index of the letter in the keyboard array
	 * 
	 * 
	 * 
	 */
	
	private int search(Character target) {
		for(int i = 0; i < keyboard.length; i++) {
			if(keyboard[i].equals(Character.toString(target))) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Recolors the keyboard characters
	 * 
	 * <p>
	 * 
	 * This method colors/recolors the keyboard characters. It works similarly
	 * as colorMain and changes the color of a keyboard character after
	 * each guess according to the rules of Wordle. 
	 * 
	 * @param guessedChar a Character array with all the guessed characters
	 * @param guessedIndexResult an array of INDEX_RESULT that contain the state
	 * of each letter of the given word
	 */
	
	private void colorizeChar(Character[] guessedChar, INDEX_RESULT[] guessedIndexResult) {
		for(int i = 0; i < guessedIndexResult.length; i++) {
			Character temp = guessedChar[i];
			int index = search(temp);
			if(guessedIndexResult[i] == null)
				continue;
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.CORRECT)==0) {
				arrayOfKeyBoardLetters[index].setBackground(greenBackground);
			}
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX)==0) {
				arrayOfKeyBoardLetters[index].setBackground(yellowBackground);
			}
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.INCORRECT)==0) {
				arrayOfKeyBoardLetters[index].setBackground(darkGreyBackground);
			}
			
		}
	}
	
	
	/**
	 * This method changes the color of the incorrectly guessed, partially
	 * correct guessed, and correct guessed Labels in the GUI. It does that
	 * by changing the background of those labels to their appropriate colors
	 * It takes in an array  of guessed characters and an array of INDEX_RESULT
	 * (this tells us about the state of the character). 
	 * 
	 * @param progress an array of Guess elements that contains the current progress
	 * of the game
	 * @param controller an instance of the controller class
	 */
	
	private void colorMain(Guess[] progress, WordleController controller) {
		for(int i = 0; i < attempt; i++) {
			if(progress[i] != null) 
			{
				INDEX_RESULT[] index = progress[i].getIndices();
				for(int j = 0; j < len; j++) {
					if(index[j].compareTo(INDEX_RESULT.CORRECT) == 0) {
						arrayOfLabels[i][j].setBackground(greenBackgroundBig);
					}
					else if (index[j].compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX) == 0) {
						arrayOfLabels[i][j].setBackground(yellowBackgroundBig);
					}
					else { 
						arrayOfLabels[i][j].setBackground(afterEnterBackground);
					}
						
				}
			}
		}
	}
		
	/**
	 * Initializes all the fields
	 * 
	 * <p>
	 * 
	 * This private method has been created to reduce clutter in the start method. Its goal
	 * is to initialize all private fields in one place
	 */
	
	
	private void fieldInitialization() {
		model = new WordleModel();
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
	
	/**
	 * Set Nodes to their properties
	 * 
	 * <p>
	 * 
	 * This private method has been created to reduce clutter in the start method. Its goal
	 * is to contain all the setter method calls by all the nodes. 
	 */
	
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
	/**
	 * Populates arrayOfLabels and arrayOfKeyBoardLetters
	 * 
	 * <p>
	 * 
	 * This private method has been created to reduce clutter in the start method. Its goal
	 * is to add new labels to these fields and set their properties using loops.
	 * 
	 */
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
	/**
	 * Joins all the letters 
	 * 
	 * <p>
	 * 
	 * This private method is used to join all the letters to a String
	 * from the stack
	 * @param s an ArrayList of Strings acting as a stack
	 * @return a String comprised of all the letters in the ArrayList
	 */
	private String join(ArrayList<String> s) {
		String result = "";
		for (String elem:s) {
			result += elem;
		}
		return result;
	}

}
