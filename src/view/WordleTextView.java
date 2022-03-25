package view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import controller.WordleController;
import controller.WordleController.CorrectLengthException;
import controller.WordleController.NotInDictionaryException;
import controller.WordleController.OnlyLettersException;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;


public class WordleTextView implements Observer{
	
	WordleModel model;
	WordleController controller;
	Character[] guessedChar;
	String guess;
	public WordleTextView() {
		model = new WordleModel();
		controller = new WordleController(model);
		model.addObserver(this);
	}
	
	public void runText() throws OnlyLettersException, NotInDictionaryException, CorrectLengthException{
		
			// a model and a controller are defined at the start
			
			guessedChar = controller.allChar();				
			// main game loop
			while(!controller.isGameOver()) {
				Scanner sc = new Scanner(System.in);
				System.out.print("Enter a guess: ");
				guess = sc.next().toUpperCase();
				try {
					controller.makeGuess(guess);	
				}
				catch (OnlyLettersException e) {
					System.out.println(e.toString());
				}
				catch(NotInDictionaryException e) {
					System.out.println(e.toString());
				}
				catch(CorrectLengthException e) {
					System.out.println(e.toString());
				}
			}
			System.out.println("Good game! The word was " + getAnswer() + ".");
			System.out.println("Would you like to play again? yes/no");
			
	}
			
	
	public String getAnswer() {
		return controller.getAnswer();
	}
	
	/**
	 * Prints incorrectly guessed, partially, correct guessed, and correct guessed characters
	 * <p>
	 * 
	 * This static method prints out the unguessed, incorrectly guessed, partially
	 * correct guessed, and correct guessed in the console. It takes in an array
	 * of guessed characters and an array of INDEX_RESULT (this tells us about
	 * the state of the character). Then, it add the character according to their
	 * state in the predefined ArrayLists
	 * 
	 * @param guessedChar a Character array with all the guessed characters
	 * @param guessedIndexResult an array of INDEX_RESULT that contain the state
	 * of each letter of the given word
	 * @param unguessed an ArrayList of Characters that stores the unguessed letters
	 * @param incorrect an ArrayList of Characters that stores the incorrect letters
	 * @param correct an ArrayList of Characters that stores the unguessed letters
	 * @param correctWrong an ArrayList of Characters that stores the unguessed letters
	 */
	public void printChar(Character[] guessedChar, INDEX_RESULT[] guessedIndexResult, ArrayList<Character> unguessed,
			ArrayList<Character> incorrect, ArrayList<Character> correct, ArrayList<Character> correctWrong) {
		
		for(int i = 0; i < guessedIndexResult.length; i++) {
			if(guessedIndexResult[i] == null)
				unguessed.add(guessedChar[i]);
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.CORRECT)==0)
				correct.add(guessedChar[i]);
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX)==0)
				correctWrong.add(guessedChar[i]);
			else if(guessedIndexResult[i].compareTo(INDEX_RESULT.INCORRECT)==0)
				incorrect.add(guessedChar[i]);
			
		}
		
		if(unguessed.size() != 0)
			System.out.println("Unguessed " + unguessed);
		if(incorrect.size() != 0)
			System.out.println(INDEX_RESULT.INCORRECT.getDescription() + " " + incorrect);
		if(correct.size() != 0)
			System.out.println(INDEX_RESULT.CORRECT.getDescription() + " " + correct);
		if(correctWrong.size() != 0)
			System.out.println(INDEX_RESULT.CORRECT_WRONG_INDEX.getDescription() + " " + correctWrong);
		System.out.println();
	}
	
	/**
	 * Prints progress
	 * <p>
	 * 
	 * This method prints out the progress as a 2 dimensional grid of letters and
	 * spaces. It prints out the correctly guessed letters in upper case.
	 * If it's correctly guessed but on the wrong index, it prints out in lower case
	 * @param progress an array of Guess elements that contains the current progress
	 * of the game
	 * @param controller an instance of the controller class
	 */
	
	public void toStringProgress(Guess[] progress, WordleController controller) {
		for(int i = 0; i < controller.getAllowedNumberOfGuesses(); i++) {
			if(progress[i] != null) 
			{
				INDEX_RESULT[] index = progress[i].getIndices();
				String guess = progress[i].getGuess();
				
				for(int j = 0; j < controller.getLen(); j++) {
					if(index[j].compareTo(INDEX_RESULT.CORRECT) == 0) {
						String s = Character.toUpperCase(guess.charAt(j)) + " ";
						System.out.print(s);
					}
					else if (index[j].compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX) == 0) {
						String s = Character.toLowerCase(guess.charAt(j)) + " ";
						System.out.print(s);
					}
					else System.out.print("_ ");
						
				}
				System.out.println();
			}
			else System.out.println("_ _ _ _ _");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Guess[] progress = model.getProgress();
		
		// ArrayLists of relevant criteria
		ArrayList<Character> unguessed = new ArrayList<Character>();
		ArrayList<Character> incorrect = new ArrayList<Character>();
		ArrayList<Character> correct = new ArrayList<Character>();
		ArrayList<Character> correctWrong = new ArrayList<Character>();
		toStringProgress(progress, controller);
		System.out.println();
		INDEX_RESULT[] guessedIndexResult = model.getGuessedCharacters();
		printChar(guessedChar, guessedIndexResult,unguessed,
				incorrect, correct, correctWrong);
			
	}

	
	
	
}
