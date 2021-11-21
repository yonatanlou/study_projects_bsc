import java.util.Scanner;

/**
 * The class will represent any player in the game and will get input from the user.
 */
public class HumanPlayer implements Player {
    /**
     * Constructor of player
     */
    public HumanPlayer() {

    }

    /**
     * Getting coordinates input from the user (only valid coordinates).
     *
     * @param board cuurent board.
     * @param mark  which mark is this player
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        System.out.println("Player " + mark + ", type coordinates: ");
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        int[] choice = indexInputExtractor(num);
        while (!board.putMark(mark, choice[0], choice[1])) {
            System.out.println("Invalid coordinates, type again: ");
            num = in.nextInt();
            choice = indexInputExtractor(num);
        }


    }

    /**
     * Extract the number from the user input
     *
     * @param num user input
     * @return Array of ints [size=2]
     */
    @Override
    public int[] indexInputExtractor(int num) {
        int row = Math.floorDiv(num, 10) - 1;
        int col = num % 10 - 1;

        int[] ans = {row, col};
        return ans;
    }
}
