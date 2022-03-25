package view;

import java.util.Scanner;

import controller.WordleController.CorrectLengthException;
import controller.WordleController.NotInDictionaryException;
import controller.WordleController.OnlyLettersException;
import javafx.application.Application;

public class Wordle {
	
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
