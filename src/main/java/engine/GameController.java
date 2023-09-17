package engine;

import agent.Player;
import gui.BokuBoard;
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
        gameState = 1; //Todo: Remove later
        boardStateHistory = new ArrayList<>();
        boardState = new BoardState(new BigInteger("0"), new BigInteger("0"));
        // BigInteger boardTest = new BigInteger("0101");
        // System.out.println(boardTest);
        // System.out.println(boardTest.toString(2));
    }

    public void startGame() {
        if (DEBUG_LOG) {
            System.out.println("Starting game");
        }
    }

    private void initNewGame(Player player1, Player player2) {
        gameState = 1;
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
    public void attemptMoveOnClick(BokuBoard board, int coordinateIndex) {
        // If all conditions are met
        if (DEBUG_LOG) {
            System.out.printf("Position Index: %s\n", coordinateIndex);
        }
        executeMove(coordinateIndex);
    }

    public void executeMove(int coordinateIndex) {
        // Update Backend
        String binaryCoordinate = "1" + "0".repeat(coordinateIndex);
        BigInteger toAdd = new BigInteger(binaryCoordinate, 2);
        BoardState newBoardState;
        if (this.gameState == 1) {
            System.out.println("Yes");
            newBoardState = new BoardState(
                    this.boardState.getWhitePieces().add(toAdd),
                    this.boardState.getBlackPieces());
        }
        else if (this.gameState == 2) {
            System.out.println("Yesyes");
            newBoardState = new BoardState(
                    this.boardState.getWhitePieces(),
                    this.boardState.getBlackPieces().add(toAdd));
        }
        else {
            newBoardState = this.boardState;
            //Todo: Proper error handling
            System.out.println("No valid game state");
        }
        boardState = newBoardState;
        if (DEBUG_LOG) {
            System.out.println("Game State " + this.gameState);
            System.out.println("Board state (W): " + (newBoardState.getWhitePieces()).toString(2));
            System.out.println("Board state (B): " + (newBoardState.getBlackPieces()).toString(2));
        }
        boardStateHistory.add(boardState);

        // Update GUI
        if (this.gameState == 1) {
            bokuBoard.getHexagons().get(coordinateIndex).setFill(Color.WHITE);
        }
        else if (this.gameState == 2) {
            bokuBoard.getHexagons().get(coordinateIndex).setFill(Color.BLACK);
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
