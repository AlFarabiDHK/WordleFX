package view;

import java.util.Scanner;
import controller.WordleController.CorrectLengthException;
import controller.WordleController.NotInDictionaryException;
import controller.WordleController.OnlyLettersException;
import javafx.application.Application;

/**
 * This class runs either the WordleTextView or the WordleGUIView based off of
 * the program argument. It runs the text view as many times as the user wants
 * but runs the GUI view once per game. 
 * @author Muhtasim Al-Farabi
 *
 */

public class Wordle {
	
	/**
	 * Main method
	 * 
	 * <p>
	 * 
	 * This method runs either the WordleTextView or the WordleGUIView based off of
     * the program argument. It runs the text view as many times as the user wants
     * but runs the GUI view once per game. 
     * 
	 * @param args an array of program arguments
	 * @throws OnlyLettersException if guess contains non-alphabet letter
	 * @throws NotInDictionaryException if guess is not in the Dictionary.txt file
	 * @throws CorrectLengthException if guess is not equal to the allowed Length
	 */
	
    public static void main(String[] args) throws OnlyLettersException, NotInDictionaryException, CorrectLengthException {
    	if (args[0].equals("-text")) {
	    	boolean playAgain = true;
			// this loop initiates each game
			while(playAgain){
			WordleTextView text = new WordleTextView();
	    	text.runText();
			Scanner sc = new Scanner(System.in);
			String s = sc.next();
			sc.close();
			if(s.equals("no")) break;
			}
			
    	}
    	else if(args[0].equals("-gui")) {
    		Application.launch(WordleGUIView.class, args[0]);
    	}
    }
    
}
