package Proiect;

import Pieces.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	// check if a given string has the structure of a move
	private static boolean isAMove(String word) {
		if(word.length() != 4)
			if(word.length() != 5)
				return false;
		if(word.charAt(0) < 'a' || word.charAt(0) > 'h' || word.charAt(2) < 'a' || word.charAt(2) > 'h')
			return false;
		if(word.charAt(1) < '1' || word.charAt(1) > '8' || word.charAt(3) < '1' || word.charAt(3) > '8')
			return false;
		return true;
	}

    public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// create a new game
		Game game = new Game();
		game.initializeGame();
		game.setColorBlack();


		String cmd;
		String []words;

		while(true) {
			// read a new command from stdin and extract the first word from it
			cmd = scanner.nextLine();
			words = cmd.split(" ");

			if(words[0].equals("protover")) {
				System.out.println("feature san=0 sigint=0");
			}
			else
			if(words[0].equals("new")) {

				// (re-) initialize the game
				game.initializeGame();
			}
			else
			if(words[0].equals("force")) {

				// set the bot to be off
				game.setBotStatus(false);
			}
			else
			if(words[0].equals("white")) {

				// set the bot's color to white
				game.setColorWhite();
			}
			else
			if(words[0].equals("black")) {

				// set the bot's color to black
				game.setColorBlack();
			}
			else
			if(isAMove(words[0])) { // if the word is a possible move

				// if the bot is on
				if (game.getBotStatus()) {

					// if the bot has a pawn, interpret the move and move the pawn
					game.interpretNotBotMove(words[0]);

					// if there are no movable pieces, resign
					Piece p_crt = game.getCrtPiece();

					if (p_crt == null) {
						System.out.println("resign");
					}
				 	else {
						//move the crt piece
						p_crt.movePiece(game);
					}
				}
				else{
					// if the bot is off, simply interpret the move
					game.interpretNotBotMove(words[0]);
				}
			}
			else
			if(words[0].equals("quit")) {
				return;
			}
			else
			if(words[0].equals("go")){

				// turn the bot on
				game.setBotStatus(true);

				// if the bot has a movable piece, move it; otherwise, resign
				Piece p_crt = game.getCrtPiece();

				String tip = "";

				if(p_crt != null) {
					p_crt.movePiece(game);
				}
				else {
					// if there are no available pieces to move, resign
					System.out.println("resign");
				}
			}
		}
	}
}
