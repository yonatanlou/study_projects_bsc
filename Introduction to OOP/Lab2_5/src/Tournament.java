/**
 * The main class which will run a whole tournament.
 */
public class Tournament {

    private static final int ARG_ROUND_COUNT = 0;
    private static final int ARG_RENDERER = 1;
    private static final int ARG_PLAYER1 = 2;
    private static final int ARG_PLAYER2 = 3;
    private static final int ARG_NUM = 4;


    int rounds;
    Renderer renderer;
    Player[] players;

    /**
     * The main method which making the tournament (currently one) to work.
     *
     * @param args [round count] [renderer: none/console] [player1: clever/whatever/snartypamts] [player2: as player1]
     */
    public static void main(String[] args) {
        if (args.length != ARG_NUM) {
            System.err.println(String.format("Not enough args, input was %d, suppuse to %d", args.length, ARG_NUM));
            return;
        }

        RendererFactory renderer_factory = new RendererFactory();
        PlayerFactory player_factory = new PlayerFactory();

        //transforming the args to the right classes
        int roundCount = Integer.parseInt(args[ARG_ROUND_COUNT]);
        Renderer renderer = renderer_factory.buildRenderer(args[ARG_RENDERER]);
        Player player1 = player_factory.buildPlayer(args[ARG_PLAYER1]);
        Player player2 = player_factory.buildPlayer(args[ARG_PLAYER2]);
        Player[] players = {player1, player2};


        Tournament tournament = new Tournament(roundCount, renderer, players);
        tournament.playTournament();


    }


    /**
     * Constructor
     *
     * @param rounds   how many round for this tournament.
     * @param renderer which renderer to renderer the game.
     * @param players  array of Player objects.
     */
    public Tournament(int rounds, Renderer renderer, Player[] players) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.players = players;

    }


    /**
     * Loop n (rounds) times, in each loop performing a tic tac toe game and saving the scores.
     * At the end of the method, printing the final results.
     */
    public void playTournament() {
        int player1_index = 0;
        int player2_index = 1;

        int[] results = {0, 0, 0}; //{player1, player2, draw}
        for (int i = 0; i < this.rounds; i++) {
            //The first player will always be X, and each turn the players are switching turns.
            Game game = new Game(players[player1_index % 2], players[player2_index % 2], renderer);
            Mark game_score = game.run();

            results[whosWinner(game_score, player1_index, player2_index)] += 1;

            player1_index++;
            player2_index++;


            System.out.print(String.format("=== player 1: %d | player 2: %d | Draws: %d ===\r", results[0],
                    results[1], results[2]));


        }


    }

    /**
     * Helper function for playTournament, checking who is the winner by his current index,
     * and his current mark.
     *
     * @param game_score    Mark object
     * @param player1_index int the indicates about this player
     * @param player2_index int the indicates about this player
     * @return int for who won {0:payer1, 1:player2, 2:draw}
     */
    private int whosWinner(Mark game_score, int player1_index, int player2_index) {
        if (game_score == Mark.BLANK) {
            return 2;
        }

        if (player1_index % 2 == 0 && game_score == Mark.X) {
            return 0;
        }

        if (player1_index % 2 == 1 && game_score == Mark.O) {
            return 0;
        }

        if (player2_index % 2 == 1 && game_score == Mark.O) {
            return 1;
        }

        if (player2_index % 2 == 0 && game_score == Mark.X) {
            return 1;
        }
        return 2;
    }
}


