package controller;

import utilities.Guess;
import utilities.INDEX_RESULT;
import model.WordleModel;

// TODO Transfer getprogress to model

public class WordleController {
	
	private WordleModel model;
	/**
	 * Allowed number of guessed
	 */
	public final int ALLOWEDNUMBEROFGUESSES = 6;
	/**
	 * Allowed length of a word
	 */
	public final int LEN = 5;
	private Guess[] progress;
	private int curr;
	private boolean isGameOverBool;
	
	/**
	 * Constructor
	 * <p>
	 * This method is the constructor for this class. It sets each element of the progress
	 * array to null and assigns relevant values to other fields
	 * @param model an instance of WordleModel class
	 */
	
	public WordleController (WordleModel model) {
		this.model = model;
		this.curr = 1;
		this.isGameOverBool = false;
		this.progress = new Guess[this.ALLOWEDNUMBEROFGUESSES];
		for(int i = 0; i < this.ALLOWEDNUMBEROFGUESSES; i++) {
				progress[i] = null;
		}
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
	 * Gets the answer
	 * <p>
	 * This method gets the answer from the model
	 * @return a string from the model that is the actual answer word
	 */
	
	public String getAnswer() {
		return this.model.getAnswer();
	}
	
	/**
	 * Takes a guess and stores into the progress
	 * <p>
	 * This method calls the makeGuess method from the model and returns the output of the method
	 * @param guess an instance of Guess, contains information about the current guess
	 * @return returns a guess using the given parameter
	 * @throws OnlyLettersException if guess contains non-alphabet letter
	 * @throws NotInDictionaryException if guess is not in the Dictionary.txt file
	 * @throws CorrectLengthException if guess is not equal to the allowed Length
	 */
	
	public void makeGuess(String guess) {
		//if(guess.length() != LEN)
			//throw new CorrectLengthException("Invalid input. The word should be "+ LEN + " letters long.");
		//if(!guess.matches("[a-zA-Z]+"))
			//throw new OnlyLettersException("Invalid input. Must only contain alphabets");
		//if(!model.getDict().contains(guess))
			//throw new NotInDictionaryException("Invalid input. The word is not in the dictionary.");
		
		
		if(model.getAnswer().equals(guess) || this.curr == ALLOWEDNUMBEROFGUESSES)
			this.isGameOverBool = true;
		Guess g = model.makeGuess(guess);
		if (g.getIsCorrect()) this.isGameOverBool = true;
		this.progress[this.curr-1] = g;
		this.curr++;
	}
	
	

}
