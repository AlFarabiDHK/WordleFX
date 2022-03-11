package model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import utilities.Guess;
import utilities.INDEX_RESULT;
/**
 * 
 * This class is the model class for the Wordle program. It has methods to shape the
 * structure of the program. It is an improved version of the wordle program written
 * for project 1 as it approaches problem by using utility classes such as Guess.java and
 * INDEX_RESULT.java It keeps track of the correctly guessed, 
 * correctly guessed but in the wrong place, and wrongly guessed letters.
 * It also updates the guessed characters. This class primarily communicates with the
 * controller class to serve the view class. We only use model inside the controller
 * as the controller handles of the game logic. 
 * 
 * @author Muhtasim Al-Farabi, Tyler Conklin
 */

public class WordleModel {
	
	private static final String FILENAME = "dictionary.txt";
	/**
	 * Allowed number of guessed
	 */
	public final int ALLOWEDNUMBEROFGUESSES = 6;
	/**
	 * Allowed length of a word
	 */
	private static final int LEN = 5;
	private static final int ALPHABETCOUNT = 26;
	private Guess[] progress;
	private String answer;
	private INDEX_RESULT[] guessedCharacters;
	private ArrayList<String> dictionary = new ArrayList<String>();

	/**
	 * Constructor
	 * <p>
	 * This is the constructor for the WordleModel class. It sets all the elements
	 * of the guessedCharacters array to null. Then it opens up the file, adds the words into an ArrayList,
	 * then generates a random number between 0 and that number.
	 * Then, it selects the word at that random index and assigns as the answer.
	 */
	
	public WordleModel() { 
		try{
			this.guessedCharacters = new INDEX_RESULT[ALPHABETCOUNT];
			for(int i = 0; i < ALPHABETCOUNT; i++){
				this.guessedCharacters[i] = null;
			}
			Scanner sc = new Scanner(new File(FILENAME));
			while(sc.hasNext()){
				String temp = sc.next();
				this.dictionary.add(temp);
			}
			sc.close();
			Random rand = new Random();
			int randomNumber = rand.nextInt(dictionary.size());
			this.answer = dictionary.get(randomNumber).toUpperCase();
			this.answer = this.answer.toUpperCase();
			this.progress = new Guess[this.ALLOWEDNUMBEROFGUESSES];

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the dictionary in an ArrayList
	 * <p>
	 * This method returns an array that contains all the words from the dictionary
	 * @return an ArrayList that contains all the words from the dictionary
	 */
	
	public ArrayList<String> getDict(){
		return this.dictionary;
	}
	
	/**
	 * Analyzes the guess and returns it
	 * <p>
	 * This method takes the guess word and determines whether this is the correct one. Firstly, it 
	 * goes through the guessed and compares it with the answer. It instantiates an array of INDEX_RESULT
	 * that will contain information about the status of each letter in the guess. The program loops through
	 * and compares the guessed, and sets the enum values of the array according to the status of the letters
	 * in the guess. 
	 * @param guess a String, guess word from the user
	 * @param guessNumber a number that represents the number of attempts attempted
	 * @return returns a Guess object that has been modified to match the current guess
	 */
	
	public void makeGuess(int guessNumber, String guess) {
		
		INDEX_RESULT[] index = new INDEX_RESULT[LEN];
		boolean bool = false;
		guess = guess.toUpperCase();
		for(int i = 0; i < LEN; i++) {
			for(int j = 0; j < LEN; j++){
			if (guess.charAt(i) == this.answer.charAt(j) && i == j )
				if (index[i] == null) index[i] = INDEX_RESULT.CORRECT;
			}
		}
			
		
		for(int i = 0; i < LEN; i++) {
			for(int j = 0; j < LEN; j++){
			if (guess.charAt(i) == this.answer.charAt(j) && i != j) 
				if (index[i] == null) index[i] = INDEX_RESULT.CORRECT_WRONG_INDEX;
			}
		}
		
		
		for(int i = 0; i < index.length; i++)
			if(index[i] == null) index[i] = INDEX_RESULT.INCORRECT;
		
		for(int i = 0; i < index.length; i++) 
			if(guessedCharacters[guess.charAt(i) -'A'] != null && 
					guessedCharacters[guess.charAt(i) -'A'].equals(INDEX_RESULT.CORRECT))
				continue;
			else
				this.guessedCharacters[guess.charAt(i) - 'A'] = index[i];
		
		if(guess.equals(this.answer)) bool = true;
		this.progress[guessNumber-1] = new Guess(guess, index, bool);
	}
	

	

	/**
	 * Gets answer
	 * <p>
	 * This method gets the answer field
	 * @return a string that is the actual answer word
	 */
	
	public String getAnswer() {
		return this.answer;
	}

	/**
	 * Gets guessed characters
	 * <p>
	 * This method gets the guessed characters from the 
	 * @return an array of INDEX_RESULT that contains the guessedCharacters
	 */
	
	public INDEX_RESULT[] getGuessedCharacters() {
		return this.guessedCharacters;
	}
	
	/**
	 * Gets progress
	 * <p>
	 * This method returns the progress field
	 * @return an array of Guess, a field of the class
	 */
	
	public Guess[] getProgress() {
		return this.progress;
	}
	
	/**
	 * Gets an array of all characters
	 * <p>
	 * This method generated a Character array that has all the upper case English Character
	 * @return a Character array that has all the upper case English Character
	 */
	
	public Character[] allChar(){
		Character[] arr = new Character[ALPHABETCOUNT];
		
		for(int i = 0; i < ALPHABETCOUNT; i++) {
			arr[i] = (char) (i + 65); 
		}
		return arr;
		
	}

}
