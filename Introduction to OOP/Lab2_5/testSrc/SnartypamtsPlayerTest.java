import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the SnartypamtsPlayer Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
class SnartypamtsPlayerTest {
	/**
	 * Checks that the SnartypamtsPlayer beats the CleverPlayer by at least the requested distribution - EPSILON.
	 * <p>
	 * It is recommended you temporarily remove the 'final' keyword from SIZE and WIN_STREAK and than
	 * uncomment the commented out lines for a full check of all scenarios.
	 * <p>
	 * if not the check will be done only for your current SIZE and WIN_STREAK constants.
	 * <p>
	 * NOTICE:
	 * This test relies on probability, so you might fail it once in a few tries even if your code is correct.
	 */
	@Test
	void checkAllWinDistribution() {
		checkWinDistribution();
		// TODO uncomment from here
//		int initialSize = Board.SIZE, initialStreak = Board.WIN_STREAK;
//		for (int streak = 3; streak < 8; ++streak) {
//			for (int size = 3; size < 8; ++size) {
//				if (streak > size) {
//					continue;
//				}
//				Board.WIN_STREAK = streak;
//				Board.SIZE = size;
//		checkWinDistribution();
//			}
//		}
//		Board.WIN_STREAK = initialStreak;
//		Board.SIZE = initialSize;
		// TODO uncomment to here
	}

	/**
	 * Checks that the CleverPlayer beats the Whatever player by at least the requested distribution - EPSILON.
	 * The check is done only for your current SIZE and WIN_STREAK constants.
	 * <p>
	 * NOTICE:
	 * This test relies on probability, so you might fail it once in a few tries even if your code is correct.
	 */
	private void checkWinDistribution() {
		if (CleverPlayerTest.runTournament(PlayerFactoryTest.SNARTYPAMTS, PlayerFactoryTest.CLEVER)) return;
		var results = CleverPlayerTest.getResults();
		assert (results[0] > results[1]);
	}
}