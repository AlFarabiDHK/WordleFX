package controller;

import utilities.Guess;
import utilities.INDEX_RESULT;
import controller.WordleController.CorrectLengthException;
import controller.WordleController.NotInDictionaryException;
import controller.WordleController.OnlyLettersException;
import model.WordleModel;

/**
*
* 
* This class is the controller class for the Wordle program. It has methods to control
* the flow of the game. It tracks how many guesses had been made,or if the game had ended, 
* or performs calculation to show the user their current progress. This class also has
* exception classes to handle any sort of error present.
* 
* @author Muhtasim Al-Farabi, Tyler Conklin
*/

public class WordleController {
	
	private WordleModel model;
	/**
	 * Allowed number of guessed
	 */
	private final int ALLOWEDNUMBEROFGUESSES = 6;
	/**
	 * Allowed length of a word
	 */
	private final int LEN = 5;
	private Guess[] progress;
	private int curr;
	private boolean isGameOverBool;
	private String answer;
	private boolean win;
	
	/**
	 * Constructor
	 * <p>
	 * This method is the constructor for this class. It sets each element of the progress
	 * array to null and assigns relevant values to other fields
	 * @param model an instance of WordleModel class
	 */
	
	public WordleController (WordleModel model) {
		this.model = model;
		this.isGameOverBool = false;
		this.curr = 1;
		this.win = false;
		this.answer = model.getAnswer();
		this.progress = new Guess[this.ALLOWEDNUMBEROFGUESSES];
		for(int i = 0; i < this.ALLOWEDNUMBEROFGUESSES; i++) {
				progress[i] = null;
		}
	} 
	
	/**
	 * Gets answer
	 * <p>
	 * This method gets the answer from the model
	 * @return a string that is the actual answer word
	 */
	
	public String getAnswer() {
		return model.getAnswer();
	}
	
	
	/**
	 * Gets all characters
	 * <p>
	 * This method returns the allChar() method from the model
	 * @return an array of Character, the output of model.allChar()
	 */
	
	public Character[] allChar(){
		return model.allChar();
	}
	
	/**
	 * Gets the number of attempts
	 * 
	 * <p>
	 * THis method gets the number of attempts the player has 
	 * tried already.
	 * @return the private field curr
	 */
	
	public int getCurr() {
		return this.curr;
	}
	
	/**
	 * Gets the allowed number of guess
	 * 
	 * <p>
	 * 
	 * This method gets the allowed number of guessed by 
	 * returning the field ALLOWEDNUMBEROFGUESSES
	 * 
	 * @return returns the field ALLOWEDNUMBEROFGUESSES
	 */
	
	public int getAllowedNumberOfGuesses() {
		return ALLOWEDNUMBEROFGUESSES;
	}
	
	/**
	 * Gets the allowed length of the guesses
	 * 
	 * <p>
	 * 
	 * This method gets the allowed length of guessed by 
	 * returning the field LEN
	 * 
	 * @return returns the field LEN
	 */
	
	public int getLen() {
		return LEN;
	}
	
	/**
	 * Determines the game's completion
	 * <p>
	 * This determines whether the game is over yet or not. We modify the field 
	 * isGameOverBool in another method
	 * @return returns the field called isGameOverBool
	 */
	
	public boolean isGameOver() {
		return this.isGameOverBool;
	}
	
	/**
	 * Determine's if the user won or not
	 * 
	 * <p>
	 * 
	 * This method returns the win field which indicates
	 * if a user won or lost
	 * @return whether a user won or not
	 */
	
	public boolean didWin() {
		return this.win;
	}
	
	
	/**
	 * Takes a guess and stores into the progress
	 * <p>
	 * This method calls the makeGuess method from the model and returns the output of the method
	 * @param guess an instance of Guess, contains information about the current guess
	 * @throws OnlyLettersException if guess contains non-alphabet letter
	 * @throws NotInDictionaryException if guess is not in the Dictionary.txt file
	 * @throws CorrectLengthException if guess is not equal to the allowed Length
	 */
	
	public void makeGuess(String guess) throws OnlyLettersException, NotInDictionaryException, CorrectLengthException{
		if(guess.length() != LEN)
			throw new CorrectLengthException("Invalid input. The word should be "+ LEN + " letters long.");
		if(!guess.matches("[a-zA-Z]+"))
			throw new OnlyLettersException("Invalid input. Must only contain alphabets");
		if(!model.getDict().contains(guess))
			throw new NotInDictionaryException("Invalid input. The word is not in the dictionary.");
		model.makeGuess(this.curr, guess);
		if(this.answer.equals(guess.toUpperCase()) || this.getCurr() == ALLOWEDNUMBEROFGUESSES) {
			this.isGameOverBool = true;
			if(this.answer.equals(guess.toUpperCase())) this.win = true;
		}
		this.curr++;
	}
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * This class is used in the view to catch exceptions where the
	 * letters of the guess contains characters other than
	 * alphabets.
	 * 
	 *  @author Muhtasim Al-Farabi
	 *
	 */
	
	public class OnlyLettersException extends Exception {
		/**
		 * Constructor 
		 * 
		 * <p>
		 * This method is the constructor of the class
		 * @param e a String, the error message we wish to display
		 */
		public OnlyLettersException(String e) {
			super(e);
		}
		/**
		 * 
		 * toString() method
		 * <p>
		 * This method returns the error message
		 * @return a String, getMessage(), inherited from super()
		 */
		public String toString() {
			return getMessage();
		}
	}
		
	/**
	 * 
	 * 
	 * 
	 * This class is used in the view to catch exceptions where the
	 * guess does not belong to any of the words in dictionary.txt.
	 * 
	 * @author Muhtasim Al-Farabi
	 *
	 */
	
	public class NotInDictionaryException extends Exception{
		/**
		 * Constructor 
		 * 
		 * <p>
		 * This method is the constructor of the class
		 * @param e a String, the error message we wish to display
		 */
		public NotInDictionaryException(String e) {
			super(e);
		}
		
		/**
		 * * toString() method
		 * <p>
		 * This method returns the error message
		 * @return a String, getMessage(), inherited from super()
		 */
		public String toString() {
			return getMessage();
		}
	}
	
	/**
	 * 
	 *
	 * 
	 * This class is used in the view to catch exceptions where the
	 * guess does not follow the length constrain of our game.
	 * 
	 * @author Muhtasim Al-Farabi
	 *
	 */
	
	public class CorrectLengthException extends Exception{
		/**
		 * Constructor 
		 * 
		 * <p>
		 * 
		 * This method is the constructor of the class
		 * @param e a String, the error message we wish to display
		 */
		
		public CorrectLengthException(String e) {
			super(e);
		}
		
		/**
		 * * toString() method
		 * <p>
		 * This method returns the error message
		 * @return a String, getMessage(), inherited from super()
		 */
		
		public String toString() {
			return getMessage();
		}
	}
	

}
