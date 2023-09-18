package engine;

import agent.Player;
import gui.BokuBoard;
import gui.Hexagon;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;
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
        // BigInteger boardTest = new BigInteger("0101");
        // System.out.println(boardTest);
        // System.out.println(boardTest.toString(2));
    }

    public void initNewGame(Player player1, Player player2) {
        gameState = 1;
        moveCount = 0;
        whiteToTurn = true;
        this.boardState = new BoardState(new BigInteger("0"), new BigInteger("0"));
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
    public void attemptMoveOnClick(BokuBoard board, int coordinateIndex) {
        // If all conditions are met
        boolean rejectMove = false; // Flag to tell whether an attempted move should be rejected
        // Check game state
        if (this.gameState == 0) {
            rejectMove = true;
        }

        if (DEBUG_LOG) {
            System.out.printf("Index of attempted move: %s\n", coordinateIndex);
        }
        if (!rejectMove) {
            executeMove(coordinateIndex);
        }
    }

    public void executeMove(int coordinateIndex) {
        // Update Backend
        String binaryCoordinate = "1" + "0".repeat(coordinateIndex);
        BigInteger toAdd = new BigInteger(binaryCoordinate, 2);
        BoardState newBoardState;
        if (this.gameState == 1) {
            newBoardState = new BoardState(
                    this.boardState.getWhitePieces().add(toAdd),
                    this.boardState.getBlackPieces());
        }
        else if (this.gameState == 2) {
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
