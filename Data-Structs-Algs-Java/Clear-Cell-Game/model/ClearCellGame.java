package model;

import java.awt.Color;
import java.util.Random;

/* This class must extend Game */
public class ClearCellGame extends Game  {
	private Random random;
	private int strategy;
	private int score;
	/**
	 * Defines a board with BoardCell.EMPTY cells.
	 * 
	 * @param maxRows
	 * @param maxCols
	 * @param random  used for the generation of random cells
	 * @param strategy defines which clearing cell strategy to use
	 */
	public ClearCellGame(int maxRows,
            int maxCols,
            java.util.Random random,
            int strategy) {
		super(maxRows, maxCols);
		this.random = random;
		this.strategy = strategy;
		this.score = 0;
	}
	/**
	 * The game is over when the last board row (row with index board.length -1) 
	 * is different from empty row.
	 * 
	 * @return true if the game is over; false otherwise
	 */
	public boolean isGameOver() {
		return !isRowEmpty(maxRows - 1);
	}
	
	public int getScore() {
		return score;
	}
	/**
	 * This method will attempt to insert a row of random BoardCell objects if 
	 * the last board row (row with index board.length -1) corresponds to the 
	 * empty row; otherwise no operation will take place.
	 */
	public void nextAnimationStep() {
		if (isRowEmpty(maxRows - 1)) {
			int highestRowIndex = -1;
			for (int i = maxRows - 2; i > -1; i--) {
				if (!isRowEmpty(i)) {
					highestRowIndex = i;
					break;
				}
			}
			if (highestRowIndex == -1) {
				for (int i = 0; i < maxCols; i++) {
					setBoardCell(0, i, BoardCell.getNonEmptyRandomBoardCell(random));
				}
			} else {
				for (int i = highestRowIndex; i > -1; i--) {
					for (int j = 0; j < maxCols; j++) {
						setBoardCell(i + 1, j, getBoardCell(i, j));
					}
				}
				for (int i = 0; i < maxCols; i++) {
					setBoardCell(0, i, BoardCell.getNonEmptyRandomBoardCell(random));
				}
			}
			
		}
	}
	/**
	 * This method will turn to BoardCell.EMPTY the cell selected and any adjacent
	 * surrounding cells in the vertical, horizontal, and diagonal directions that 
	 * have the same color. The clearing of adjacent cells will continue as long as 
	 * cells have a color that corresponds to the selected cell. Clearing a cell adds 
	 * one point to the game's score.
	 */
	public void processCell(int rowIndex, int colIndex) {
		if (!isGameOver() && strategy == 1) {
			BoardCell boardCell = getBoardCell(rowIndex, colIndex);
			if (!boardCell.equals(BoardCell.EMPTY)) {
				setBoardCell(rowIndex, colIndex, BoardCell.EMPTY);
				score++;
				for (int i = rowIndex - 1; i > -1; i--) {
					if (!getBoardCell(i, colIndex).equals(boardCell)) {
						break;
					} else {
						setBoardCell(i, colIndex, BoardCell.EMPTY);
						score++;
					}
				}
				for (int i = rowIndex - 1, j = colIndex + 1; i > -1 && j < maxCols; i--, j++) {
					if (!getBoardCell(i, j).equals(boardCell)) {
						break;
					} else {
						setBoardCell(i, j, BoardCell.EMPTY);
						score++;
					}
				}
				for (int i = colIndex + 1; i < maxCols; i++) {
					if (!getBoardCell(rowIndex, i).equals(boardCell)) {
						break;
					} else {
						setBoardCell(rowIndex, i, BoardCell.EMPTY);
						score++;
					}
				}
				for (int i = rowIndex + 1, j = colIndex + 1; i < maxRows && j < maxCols; i++, j++) {
					if (!getBoardCell(i, j).equals(boardCell)) {
						break;
					} else {
						setBoardCell(i, j, BoardCell.EMPTY);
						score++;
					}
				}
				for (int i = rowIndex + 1; i < maxRows; i++) {
					if (!getBoardCell(i, colIndex).equals(boardCell)) {
						break;
					} else {
						setBoardCell(i, colIndex, BoardCell.EMPTY);
						score++;
					}
				}
				for (int i = rowIndex + 1, j = colIndex - 1; i < maxRows && j > -1; i++, j--) {
					if (!getBoardCell(i, j).equals(boardCell)) {
						break;
					} else {
						setBoardCell(i, j, BoardCell.EMPTY);
						score++;
					}
				}
				for (int i = colIndex - 1; i > -1; i--) {
					if (!getBoardCell(rowIndex, i).equals(boardCell)) {
						break;
					} else {
						setBoardCell(rowIndex, i, BoardCell.EMPTY);
						score++;
					}
				}
				for (int i = rowIndex - 1, j = colIndex - 1; i > -1 && j > -1; i--, j--) {
					if (!getBoardCell(i, j).equals(boardCell)) {
						break;
					} else {
						setBoardCell(i, j, BoardCell.EMPTY);
						score++;
					}
				}
				int highestRowIndex = -1;
				for (int i = maxRows - 2; i > -1; i--) {
					if (!isRowEmpty(i)) {
						highestRowIndex = i;
						break;
					}
				}
				int highestEmptyRowIndex = -1;
				for (int i = highestRowIndex - 1; i > -1; i--) {
					if (isRowEmpty(i)) {
						highestEmptyRowIndex = i;
						break;
					}
				}
				while (highestEmptyRowIndex != -1) {
					for (int i = highestEmptyRowIndex + 1; i < highestRowIndex + 1; i++) {
						for (int j = 0; j < maxCols; j++) {
							setBoardCell(i - 1, j, getBoardCell(i, j));
						}
					}
					for (int i = 0; i < maxCols; i++) {
						setBoardCell(highestRowIndex, i, BoardCell.EMPTY);
					}
					highestRowIndex = -1;
					for (int i = maxRows - 2; i > -1; i--) {
						if (!isRowEmpty(i)) {
							highestRowIndex = i;
							break;
						}
					}
					highestEmptyRowIndex = -1;
					for (int i = highestRowIndex - 1; i > -1; i--) {
						if (isRowEmpty(i)) {
							highestEmptyRowIndex = i;
							break;
						}
					}
				}
			}
		}
	}
	
	private boolean isRowEmpty(int rowIndex) {
		for (int i = 0; i < maxCols; i++) {
			if (getBoardCell(rowIndex, i) != BoardCell.EMPTY) {
				return false;
			}
		}
		return true;
	}
}