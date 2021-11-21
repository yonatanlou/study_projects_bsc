import java.util.Random;

/**
 * A pretty dumb player that is playing randomly on the board.
 */
public class WhateverPlayer implements Player {
    /**
     * Putting a random mark in the board.
     *
     * @param board Board class.
     * @param mark  which mark is this player.
     */
    @Override
    public void playTurn(Board board, Mark mark) {

        Random ran = new Random();
        int x = ran.nextInt(Board.SIZE);
        int y = ran.nextInt(Board.SIZE);

        while (!board.putMark(mark, x, y)) {
            x = ran.nextInt(Board.SIZE);
            y = ran.nextInt(Board.SIZE);
        }


    }

    @Override
    public int[] indexInputExtractor(int num) {
        return new int[0];
    }
}
