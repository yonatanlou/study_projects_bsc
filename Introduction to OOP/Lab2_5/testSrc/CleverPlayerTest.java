import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the CleverPlayer Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
class CleverPlayerTest {
	/**
	 * number of games to play times 100.
	 */
	public static final int GAMES = 100;

	/**
	 * distribution wiggle room.
	 */
	public static final int EPSILON = 0;

	/**
	 * Checks that the CleverPlayer beats the WhateverPlayer by at least the requested distribution - EPSILON.
	 * <p>
	 * It is recommended you temporarily remove the 'final' keyword from SIZE and WIN_STREAK and than
	 * uncomment the 4 commented out lines for a full check of all scenarios.
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
//				checkWinDistribution();
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
		int distributionTarget = currentDistributionTarget();
		if (runTournament(PlayerFactoryTest.CLEVER, PlayerFactoryTest.WHATEVER)) return;
		var results = getResults();
		assertTrue (results[0] >= results[1], "Player 1 does not win most of the time.");
		if (results[0] >= results[1])
			assertTrue (results[0] > GAMES * distributionTarget - EPSILON,
					"Player 1 wins most of the time which is enough but the win distribution is not " +
							"as requested in campus IL, I belive you can accomplish it :)");
	}

	/**
	 * Returns the win distribution by the table from Campus IL.
	 *
	 * @return expected win distribution by Board.SIZE and Board.WIN_STREAK
	 */
	private static int currentDistributionTarget() {
		int sizeStreakRatio = Board.SIZE - Board.WIN_STREAK;
		if (sizeStreakRatio < 2) {
			return 60;
		}
		if (sizeStreakRatio == 2) {
			return 90;
		}
		if (sizeStreakRatio == 3) {
			return 95;
		}
		if (sizeStreakRatio == 4) {
			return 99;
		}
		fail("current SIZE/WIN_STREAK ratio not expected.");
		return 0;
	}

	/**
	 * Runs a single tournament between player 1 and player 2 with the output redirected to out.txt.
	 * @param player1
	 * @param player2
	 * @return
	 */
	static public boolean runTournament(String player1, String player2) {
		PrintStream stream;
		try {
			stream = CleverPlayerTest.printToFile();
		} catch (IOException e) {
			fail("unable to print to file.");
			return true;
		}
		Tournament.main(new String[]{
				String.format("%d", GAMES * 100),
				RendererFactoryTest.NONE,
				player1,
				player2
		});
		stream.close();
		return false;
	}

	/**
	 * Extracts the results from the output printed to out.txt.
	 * @return the results in an array as follows: (player 1 wins, player 2 wins, draws).
	 */
	public static int[] getResults() {
		var results = new int[3];
		String currentLine, prevLine = "", lastLine = "";
		try (BufferedReader br = new BufferedReader(new FileReader("out.txt"))) {
			while ((currentLine = br.readLine()) != null && !currentLine.equals("")) {
				prevLine = lastLine;
				lastLine = currentLine;
			}
		} catch (IOException e) {
			fail("unable to read out file.");
			return results;
		}
		lastLine = lastLine.replaceAll("[^0-9]", " ");
		var resultsAsStrings = lastLine.split("\\s+");
		if (resultsAsStrings.length < 3) {
			lastLine = prevLine;
			lastLine = lastLine.replaceAll("[^0-9]", " ");
			resultsAsStrings = lastLine.split("\\s+");
		}
		if (resultsAsStrings.length > 4) {
			results[0] = Integer.parseInt(resultsAsStrings[resultsAsStrings.length - 4]);
			results[1] = Integer.parseInt(resultsAsStrings[resultsAsStrings.length - 2]);
			results[2] = Integer.parseInt(resultsAsStrings[resultsAsStrings.length - 1]);
		} else if (resultsAsStrings.length > 2) {
			for (int i = resultsAsStrings.length - 3; i < resultsAsStrings.length; ++i) {
				results[i] = Integer.parseInt(resultsAsStrings[i]);
			}
		} else {
			fail("Tournament.playTournament didn't print enough numbers.");
		}
		return results;
	}

	/**
	 * Redirects the output from the standard output to out.txt.
	 * @return the opened output stream to out.txt.
	 * @throws FileNotFoundException if an error occurred while creating or opening out.txt.
	 */
	public static PrintStream printToFile() throws FileNotFoundException {
		new File("out.txt");
		PrintStream out = new PrintStream("out.txt");
		System.setOut(out);
		return out;
	}
}