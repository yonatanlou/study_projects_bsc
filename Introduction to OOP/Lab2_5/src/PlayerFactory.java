/**
 * Containing all the possible players in the game.
 */
public class PlayerFactory {

    /**
     * Translate user input for which player (through main args) to the actual players.
     *
     * @param input
     * @return Player class.
     */
    Player buildPlayer(String input) {
        if (input.equals("human")) {
            return new HumanPlayer();
        }
        if (input.equals("whatever")) {
            return new WhateverPlayer();
        }
        if (input.equals("clever")) {
            return new CleverPlayer();
        }

        if (input.equals("snartypamts")) {
            return new SnartypamtsPlayer();
        }
        return null;
    }
}
