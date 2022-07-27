package tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import model.BoardCell;
import model.ClearCellGame;
import model.Game;

public class StudentTest {
	public static final String TESTS_TAG = "\nClearCellGameTest";

	@Test
	public void test() {
		int maxRows = 8, maxCols = 8, strategy = 1;
		Game ccGame = new ClearCellGame(maxRows, maxCols, new Random(1L), strategy);
	   
		ccGame.setRowWithColor(2, BoardCell.BLUE);
		ccGame.setRowWithColor(5, BoardCell.BLUE);
		ccGame.setColWithColor(maxCols - 1, BoardCell.BLUE);
		ccGame.setBoardCell(maxRows - 1, maxCols - 1, BoardCell.EMPTY);
		
		String answer = "Before processCell\n\n";
		answer += getBoardStr(ccGame);
		ccGame.processCell(2, 6);
		answer += "\nAfter processCell\n";
		answer += getBoardStr(ccGame);
		ccGame.processCell(2, 5);
		answer += "\nAfter processCell\n";
		answer += getBoardStr(ccGame);
		ccGame.processCell(2, 7);
		answer += "\nAfter processCell\n";
		answer += getBoardStr(ccGame);
		
		answer += TESTS_TAG;
		assertTrue(TestsSupport.isCorrect("StudentTest.txt", answer));
	}
	
	/* Support methods */
	private static String getBoardStr(Game game) {
		int maxRows = game.getMaxRows(), maxCols = game.getMaxCols();

		String answer = "Board(Rows: " + maxRows + ", Columns: " + maxCols + ")\n";
		for (int row = 0; row < maxRows; row++) {
			for (int col = 0; col < maxCols; col++) {
				answer += game.getBoardCell(row, col).getName();
			}
			answer += "\n";
		}

		return answer;
	}

}
