package view;

import java.io.PrintWriter;

import commons.Level;
import commons.SignBoard;

/**
 * Prints to screen the game, steps counter and stars left.
 * @author שדמה
 *
 */
public class PrintCLI implements Printer {

	@Override
	public void print(Level l, PrintWriter pw) {
		char[][] signBoard = new SignBoard(l).getSignBoard();
		for (int i = 0; i < l.getBoundCol(); i++) 
		{
			pw.println(signBoard[i]);
			pw.flush();
		}
		pw.println("Steps: "+l.getSteps());
		pw.flush();
		pw.println("Stars left: "+l.getNumOfStars());
		pw.flush();
		l.getMyTimer().printTime(pw);
		pw.flush();
	}
	
	public void printWin(Level l, PrintWriter pw) {
		
		pw.println("YOU WON! Congratulations!");
		pw.flush();
		pw.println("You moved "+l.getSteps()+" steps, and it took you: ");
		pw.flush();
		l.getMyTimer().printTime(pw);	
		pw.flush();
	}
}

