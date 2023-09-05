package engine;

import agent.Player;

public class GameController {

    // State properties
    private int gameState; // 0 = running, 1 = player 1 won, 2 = player 2 won
    private int moveCount;
    private boolean whiteToTurn; // If true, the white player (1) has to make a move
    private Player player1;
    private Player player2;

    public GameController(Player player1, Player player2) {
        initNewGame(player1, player2);
    }

    private void initNewGame(Player player1, Player player2) {
        gameState = 0;
        moveCount = 0;
        whiteToTurn = true;
        this.player1 = player1;
        this.player2 = player2;
    }


}
