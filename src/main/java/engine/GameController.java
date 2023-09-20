package engine;

import agent.Player;
import gui.BokuBoard;
import gui.Hexagon;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class GameController {

    private static boolean DEBUG_LOG = true;

    BokuBoard bokuBoard;

    // State properties
    private int gameState; // 0 = pause, 1 = player 1 to move, 2 = player 2 to move, 3 = player 1 won, 4 = player 2 won
    private int moveCount;
    private boolean whiteToTurn; // If true, the white player (1) has to make a move
    private Player player1;
    private Player player2;

    private BoardState boardState;

    private List<BoardState> boardStateHistory;

    public GameController() {
        this.gameState = 0; //Todo: Remove later
        this.boardStateHistory = new ArrayList<>();
        // Create new empty board
        this.boardState = new BoardState();
        // Add empty board to history
        this.boardStateHistory.add(boardState);
    }

    public void initNewGame(Player player1, Player player2) {
        gameState = 1;
        moveCount = 0;
        whiteToTurn = true;
        this.player1 = player1;
        this.player2 = player2;
        this.startGame();
    }

    public void startGame() {
        if (DEBUG_LOG) {
            System.out.println("Starting game");
        }
    }

    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Attempts to make a move when a human player clicks a tile to place a stone
     */
    public void attemptMoveOnClick(int coordinateIndex) throws Exception {
        boolean rejectMove = false; // Flag to tell whether an attempted move should be rejected
        // Check game state
        if (this.gameState == 0) {
            rejectMove = true;
            System.out.println("Invalid Game State");
        }
        // Check if there is already a piece on that position
        if (this.boardState.getBoard()[coordinateIndex] != 0) {
            rejectMove = true;
            System.out.println("Cannot place a piece on this field!");
        }

        if (DEBUG_LOG) {
            System.out.printf("Index of attempted move: %s\n", coordinateIndex);
        }
        if (!rejectMove) {
            executeMove(coordinateIndex);
        }
    }

    public void executeMove(int coordinateIndex) throws Exception {
        // Update Backend
        BoardState newBoardState;
        if (this.gameState == 1) {
            newBoardState = new BoardState(boardState, true, coordinateIndex);
        }
        else if (this.gameState == 2) {
            newBoardState = new BoardState(boardState, false, coordinateIndex);
        }
        else {
            newBoardState = this.boardState;
            //Todo: Proper error handling
            System.out.println("No valid game state");
        }
        // Set new state to current one
        this.boardState = newBoardState;
        this.moveCount += 1;
        if (DEBUG_LOG) {
            System.out.println("Game State " + this.gameState);
            System.out.println("Board: " + Arrays.toString(this.boardState.getBoard()));
        }
        boardStateHistory.add(boardState);

        // Update GUI

        for (Hexagon hg : bokuBoard.getHexagons()) {
            if (hg.getCoordinateIndex() == coordinateIndex) {
                if (this.gameState == 1) {
                    hg.setBaseColour(Color.WHITE);
                }
                else if (this.gameState == 2) {
                    hg.setBaseColour(Color.BLACK);
                }
                hg.setFill(hg.getBaseColour());
            }
        }

        // Check if game is won
        boolean whitePlayer = this.gameState == 1;
        if (this.boardState.isGameWon(whitePlayer)) {
            System.out.println("Player " + this.gameState + " won the game!");
        }

        // Toggle Player Turn
        togglePlayerTurn();
    }

    private void togglePlayerTurn() {
        if (this.gameState == 1) {
            this.gameState = 2;
        }
        else if (this.gameState == 2) {
            this.gameState = 1;
        }
    }
}
