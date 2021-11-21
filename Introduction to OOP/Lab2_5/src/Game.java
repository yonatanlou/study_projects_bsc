/**
 * This class will run a whole game.
 */
public class Game {
    Player playerX;
    Player playerO;
    Renderer renderer;


    /**
     * The constructor of Game.
     *
     * @param playerX  player that will be X
     * @param playerO  player that will be O
     * @param renderer The renderer which display the game
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * This method will make the game to run.
     * the method will run until the game will end, each player will play at his turn.
     *
     * @return the winner (blank for draw)
     */
    public Mark run() {
        int counter = 0;

        Player[] players = {playerX, playerO};
        Mark[] marks = {Mark.X, Mark.O};
        Board board = new Board();
        renderer.renderBoard(board);

        while (!board.gameEnded() && board.getWinner() == Mark.BLANK) {
            players[counter % 2].playTurn(board, marks[counter % 2]);
            counter += 1;
            renderer.renderBoard(board);

        }
        return board.getWinner();


    }
}




