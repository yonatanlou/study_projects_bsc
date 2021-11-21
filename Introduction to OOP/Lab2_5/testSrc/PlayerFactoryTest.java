import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the RendererFactory Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
class PlayerFactoryTest {
	/**
	 * string representing a human player.
	 */
	public static final String HUMAN = "human";

	/**
	 * string representing a random player.
	 */
	public static final String WHATEVER = "whatever";

	/**
	 * string representing a "clever" player.
	 */
	public static final String CLEVER = "clever";

	/**
	 * string representing a "snartypamts" player.
	 */
	public static final String SNARTYPAMTS = "snartypamts";

	private static final String HUMAN_PLAYER = "HumanPlayer";
	private static final String WHATEVER_PLAYER = "WhateverPlayer";
	private static final String CLEVER_PLAYER = "CleverPlayer";
	private static final String SNARTYPAMTS_PLAYER = "SnartypamtsPlayer";

	private final PlayerFactory PF = new PlayerFactory();

	/**
	 * Checks that buildPlayer builds according to the correct strings and returns null otherwise.
	 */
	@Test
	void checkBuildPlayer() {
		assertNull(PF.buildPlayer(""));
		assertNull(PF.buildPlayer("player"));

		Player humanPlayer = PF.buildPlayer(HUMAN);
		assertNotNull(humanPlayer);
		assertEquals(HUMAN_PLAYER, humanPlayer.getClass().getSimpleName());

		Player whateverPlayer = PF.buildPlayer(WHATEVER);
		assertNotNull(whateverPlayer);
		assertEquals(WHATEVER_PLAYER, whateverPlayer.getClass().getSimpleName());

		Player cleverPlayer = PF.buildPlayer(CLEVER);
		assertNotNull(cleverPlayer);
		assertEquals(CLEVER_PLAYER, cleverPlayer.getClass().getSimpleName());

		Player snartypamtsPlayer = PF.buildPlayer(SNARTYPAMTS);
		assertNotNull(snartypamtsPlayer);
		assertEquals(SNARTYPAMTS_PLAYER, snartypamtsPlayer.getClass().getSimpleName());
	}
}