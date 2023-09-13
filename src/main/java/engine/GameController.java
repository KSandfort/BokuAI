package engine;

import agent.Player;
import gui.BokuBoard;

public class GameController {

    // State properties
    private int gameState; // 0 = running, 1 = player 1 to move, 2 = player 2 to move, 3 = player 1 won, 4 = player 2 won
    private int moveCount;
    private boolean whiteToTurn; // If true, the white player (1) has to make a move
    private Player player1;
    private Player player2;

    private BoardState boardState = new BoardState();

    public GameController() {

    }

    private void initNewGame(Player player1, Player player2) {
        gameState = 0;
        moveCount = 0;
        whiteToTurn = true;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Attempts to make a move when a human player clicks a tile to place a stone
     */
    public void attemptMoveOnClick(BokuBoard board, int[] coordinate) {
        // If all conditions are met

    }

    public void executeMove() {
        //TODO: Implement
    }
}
