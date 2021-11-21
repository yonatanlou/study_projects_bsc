import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the Board Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
public class BoardTest {
	private Board board;

	/**
	 * Checks that a board is created with all marks Blank.
	 */
	@Test
	void checkInitialization() {
		board = new Board();
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				assertEquals(Mark.BLANK, board.getMark(row, col));
			}
		}
	}

	/**
	 * Checks the PutMark and GetMark work using the same indices.
	 */
	@Test
	void checkPutMarkAndGetMarkSynchronization() {
		board = new Board();
		Mark[] marks = {Mark.X, Mark.O};
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				board.putMark(marks[(row + 3 * col) % 2], row, col);
			}
		}
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				assertEquals(marks[(row + 3 * col) % 2], board.getMark(row, col));
			}
		}
	}

	/**
	 * Checks that you can't override a placed mark.
	 */
	@Test
	void checkMarksCanNotChange() {
		board = new Board();
		assertTrue(board.putMark(Mark.X, 1, 0));
		assertFalse(board.putMark(Mark.X, 1, 0));
		assertFalse(board.putMark(Mark.O, 1, 0));
		assertEquals(Mark.X, board.getMark(1, 0));
		assertTrue(board.putMark(Mark.O, 0, 1));
		assertFalse(board.putMark(Mark.O, 0, 1));
		assertFalse(board.putMark(Mark.X, 0, 1));
		assertEquals(Mark.O, board.getMark(0, 1));
	}

	/**
	 * Checks that putMark returns false for coordinates out of range and does not mark them.
	 */
	@Test
	void checkPutAndGetMarkOutOfRange() {
		checkPutMarkOutOfRangeWithMark(Mark.X);
		checkPutMarkOutOfRangeWithMark(Mark.O);
	}

	private void checkPutMarkOutOfRangeWithMark(Mark mark) {
		board = new Board();
		assertFalse(board.putMark(mark, -1, 0));
		assertFalse(board.putMark(mark, -2, 0));
		assertFalse(board.putMark(mark, 0, -1));
		assertFalse(board.putMark(mark, 0, -2));
		assertFalse(board.putMark(mark, -1, -1));
		assertFalse(board.putMark(mark, Board.SIZE, 0));
		assertFalse(board.putMark(mark, Board.SIZE + 1, 0));
		assertFalse(board.putMark(mark, 0, Board.SIZE));
		assertFalse(board.putMark(mark, 0, Board.SIZE + 1));
		assertFalse(board.putMark(mark, Board.SIZE, Board.SIZE));
		assertFalse(board.putMark(mark, -1, Board.SIZE));
		assertFalse(board.putMark(mark, Board.SIZE, -1));

		assertEquals(Mark.BLANK, board.getMark(-1, 0));
		assertEquals(Mark.BLANK, board.getMark(-2, 0));
		assertEquals(Mark.BLANK, board.getMark(0, -1));
		assertEquals(Mark.BLANK, board.getMark(0, -2));
		assertEquals(Mark.BLANK, board.getMark(-1, -1));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE, 0));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE + 1, 0));
		assertEquals(Mark.BLANK, board.getMark(0, Board.SIZE));
		assertEquals(Mark.BLANK, board.getMark(0, Board.SIZE + 1));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE, Board.SIZE));
		assertEquals(Mark.BLANK, board.getMark(-1, Board.SIZE));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE, -1));
	}

	/**
	 * Checks that gameEnded does not initialize to True.
	 */
	@Test
	void checkGameIsNotFinishedAtStart() {
		board = new Board();
		assertFalse(board.gameEnded());
	}

	/**
	 * Checks that X can win.
	 */
	@Test
	void checkXWin() {
		checkWin(Mark.X);
	}

	/**
	 * Checks that O can win.
	 */
	@Test
	void checkOWin() {
		checkWin(Mark.O);
	}

	void checkWin(Mark mark) {
		// scenario 1: horizontal
		board = new Board();
		for (int i = 0; i < Board.WIN_STREAK; i++) {
			board.putMark(mark, 2, i);
		}
		assertEquals(mark, board.getWinner());
		// scenario 2: vertical, length 4
		board = new Board();
		assertFalse(board.gameEnded(), "End boolean may unwantedly be static.");
		if (Board.WIN_STREAK < 5) {
			int[] puttingOrder = {3, 1, 0, 4, 2};
			for (int i = 0; i < 5; i++) {
				board.putMark(mark, puttingOrder[i], 1);
			}
			assertEquals(mark, board.getWinner());
		}
		if (Board.WIN_STREAK < 4) {
			int[] puttingOrder = {3, 1, 0, 2};
			for (int i = 0; i < 4; i++) {
				board.putMark(mark, puttingOrder[i], 1);
			}
			assertEquals(mark, board.getWinner());
		}
		// scenario 3: first diagonal
		board = new Board();
		for (int i = 0; i < Board.WIN_STREAK; i++) {
			board.putMark(mark, i, i);
		}
		assertEquals(mark, board.getWinner());
		// scenario 4: second diagonal
		board = new Board();
		for (int i = 0; i < Board.WIN_STREAK; i++) {
			board.putMark(mark, Board.SIZE - 1 - i, i);
		}
		assertEquals(mark, board.getWinner());
	}

	/**
	 * Checks that a draw happens when the board is full with no winning streaks.
	 */
	@Test
	void CheckDraw() {
		board = new Board();
		if (Board.WIN_STREAK < 3) {
			fail("Can't achieve a draw with a win streak smaller than 3.");
			return;
		}
		Mark[] marks = {Mark.X, Mark.X, Mark.O, Mark.O};
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				board.putMark(marks[(2 * row + col) % 4], row, col);
				if ((row == Board.SIZE - 1) && (col == Board.SIZE - 1)) {
					assertEquals(Mark.BLANK, board.getWinner(),
							"Board filled without a win but did not indicate a draw.");
					return;
				}
				assertFalse(board.gameEnded());
			}
		}
	}
}