/**
 * The clever player knows that whatever player is playing randomly.
 * The clever player has done some probability courses, and he knows that in average,
 * he will get more victorys if he'll just trying to fill his marks in a row.
 * So this player is trying to mark row by row until he get failed.
 *
 * @author yonatan_lourie
 */
public class CleverPlayer implements Player {
    /**
     * Trying to put a mark in the board from (0,0) coord. if failed trying the next point in the row (0, 1).
     * If there is no place in the row, the player will try put mark in the column below.
     * @param board
     * @param mark
     * @inheritDoc
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int i = 0;
        int j = 0;
        while (!board.putMark(mark, i, j)) {
            if (i == Board.SIZE-1 && j == Board.SIZE-1) {
                break;
            }
            if (j == Board.SIZE-1) {
                j = -1;
                i++;
            }

            j++;

        }

    }

    @Override
    public int[] indexInputExtractor(int num) {
        return new int[0];
    }
}
