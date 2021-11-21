
/**
 * Class that containing all the data of a particular TicTacToe.
 * Each game have his own board, and the board will be updated by the turns of the players.
 *
 * @author yonatan_lourie
 */
public class Board {
    /**
     * The size of the board.
     */

    Mark game_status = Mark.BLANK;
    public static final int SIZE = 6;
    /**
     * The amount of marks you need in a sequence to win.
     */

    public static final int WIN_STREAK = 4;


    private int turn_counter = 0;

    Mark[][] board_data = new Mark[SIZE][SIZE];


    /**
     * The constructor of the board class.
     */
    public Board() {
        for (int i = 0; i < board_data.length; i++) {
            for (int j = 0; j < board_data[i].length; j++) {
                board_data[i][j] = Mark.BLANK;
            }
        }
    }


    /**
     * Check if the index is valid for that particular board.
     * The validation is only for the board sizes (and not if there is a mark).
     *
     * @param row x coordinate of the board.
     * @param col y coordinate of the board.
     * @return boolean
     */
    boolean isValidInput(int row, int col) {
        boolean invalidPlace = (row < 0 || row >= SIZE || col < 0 || col >= SIZE);
        if (invalidPlace) {
            return false;
        }
        return true;
    }

    /**
     * Putting a new mark on the board if its valid coordinates
     * updating game_status
     *
     * @param mark The mark which the board will be updated.
     * @param row  x coordinate for the new mark.
     * @param col  y coordinate for the new mark.
     * @return boolean
     */
    boolean putMark(Mark mark, int row, int col) {

        //check if input is valid
        if (!isValidInput(row, col) || !mark.getClass().isEnum()) {
            return false;
        }
        //check if input is already on the board and blank
        if (getMark(row, col) == Mark.BLANK) {
            board_data[row][col] = mark;
            turn_counter += 1;
            game_status = gameScore();
            return true;
        }
        return false;
    }


    /**
     * Showing which mark have in the board on that coordinates.
     *
     * @param row x coordinate
     * @param col y coordinate
     * @return Mark
     */
    public Mark getMark(int row, int col) {
        if (!isValidInput(row, col)) {
            return Mark.BLANK;
        }
        return board_data[row][col];
    }

    /**
     * Checking if the game has ended or not.
     *
     * @return boolean
     */
    boolean gameEnded() {
        if (turn_counter < SIZE * SIZE) {
            return false;
        }
        return true;
    }

    /**
     * given a direction and a starting coordinate, checking how many marks there are in that direction.
     *
     * @param row           x coord
     * @param col           y coord
     * @param row_direction which row direction (with {1,-1}
     * @param col_direction which col direction (with {1,-1}
     * @param mark          preferably X,O
     * @return int: howe many in row/col/diag
     */

    public int directionCounter(int row, int col, int row_direction, int col_direction, Mark mark) {
        int count = 0;
        while (row < SIZE && row >= 0 && col < SIZE && col >= 0 && board_data[row][col] == mark) {
            count++;
            row += row_direction;
            col += col_direction;
        }
        return count;

    }

    /**
     * check what is the game score by searching all of the board WIN_STREAK of X or O.
     *
     * @return Mark type (BLANK for no winner/draw, X, O)
     */
    Mark gameScore() {

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Mark mark = board_data[row][col];
                if (mark == Mark.BLANK)
                    continue;
                if (directionCounter(row, col, 0, 1, mark) >= WIN_STREAK)
                    return mark;
                if (directionCounter(row, col, 1, 0, mark) >= WIN_STREAK)
                    return mark;
                if (directionCounter(row, col, 1, 1, mark) >= WIN_STREAK)
                    return mark;
                if (directionCounter(row, col, 1, -1, mark) >= WIN_STREAK)
                    return mark;
            }
        }
        return Mark.BLANK;
    }


    /**
     * Checking who is the winner by checking game_status
     *
     * @return Will return mark by the winner
     */

    public Mark getWinner() {
        return game_status;
    }

}


