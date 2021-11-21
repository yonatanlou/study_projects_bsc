/**
 * The Snartypamts player goal, is to win all of the players.
 * His methodolgy is built upon the CleverPlayer methodology, with a little bonus that will sure that he will
 * always beat the CleverPlayer. He will always start from coord (0,1) and will fill the column below.
 * With this method, he will always will be better (with 1 or 2 marks in a row) from the clever one.
 *
 * @inheritDoc
 */
public class SnartypamtsPlayer implements Player {
    /**
     * Will always start from (0,1) coord and will try the mark the whole column.
     *
     * @param board
     * @param mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int i = 0;
        int j = 1;
        while (!board.putMark(mark, i, j)) {
            if (i == Board.SIZE - 1 && j == Board.SIZE - 1) {
                break;
            }
            if (i == Board.SIZE - 1) {
                i = -1;
                j++;
            }

            i++;

        }

    }

    @Override
    public int[] indexInputExtractor(int num) {
        return new int[0];
    }
}
