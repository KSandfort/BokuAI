package engine;

import agent.Player;
import gui.BokuBoard;
import gui.Hexagon;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class GameController {

    private static boolean DEBUG_LOG = true;

    BokuBoard bokuBoard;

    // State properties
    private int gameState; // 0 = pause, 1 = player 1 to move, 2 = player 2 to move, 3 = player 1 won, 4 = player 2 won, 5 = white can take, 6 = black can take
    private int moveCount;
    private boolean whiteToTurn; // If true, the white player (1) has to make a move
    private Player player1;
    private Player player2;
    private int blockedCoordinate = -1; // If a piece gets taken, it is blocked for the next move

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
        if (!(this.gameState == 1 || this.gameState == 2)) {
            rejectMove = true;
            System.out.println("Invalid Game State");
        }
        // Check if there is already a piece at that position
        if (this.boardState.getBoard()[coordinateIndex] != 0) {
            rejectMove = true;
            System.out.println("Cannot place a piece on this field!");
        }
        // Check if piece is currently blocked by capture from last move
        if (coordinateIndex == this.blockedCoordinate) {
            rejectMove = true;
            System.out.println("Cannot place a piece on this field, position blocked from capture");
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

        // Remove blocked coordinate
        this.blockedCoordinate = -1;

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
            if (whitePlayer) {
                this.gameState = 3;
            }
            else {
                this.gameState = 4;
            }
        }

        // Check if piece can be taken
        int[] piecesToTake = boardState.getPiecesToTake(coordinateIndex, whitePlayer);
        if (piecesToTake.length != 0) {
            // Execute logic to take a piece
            System.out.println("PIECES TO TAKE");
            System.out.println(Arrays.toString(piecesToTake));

            // Set game state
            if (whitePlayer) {
                this.gameState = 5;
            }
            else {
                this.gameState = 6;
            }

            // Highlight pieces to take
            for (int i : piecesToTake) {
                bokuBoard.placeTakeMarker(i);
            }

            // Wait for decision

            // Remove indicators

            // Remove piece
        }

        // Toggle Player Turn
        togglePlayerTurn();
    }

    public void removePiece(int coordinateIndex) {
        // Update back end
        this.boardState.getBoard()[coordinateIndex] = 0;
        this.boardStateHistory.add(boardState);
        // Update front end
        for (Hexagon hex : this.bokuBoard.getHexagons()) {
            if (hex.getCoordinateIndex() == coordinateIndex) {
                hex.setBaseColour(Hexagon.unsetColour);
                hex.setFill(hex.getBaseColour());
            }
        }
    }

    private void togglePlayerTurn() {
        if (this.gameState == 1) {
            this.gameState = 2;
        }
        else if (this.gameState == 2) {
            this.gameState = 1;
        }
    }

    public void undoSingleMove() {
        // Get board state from history
        this.boardState = this.boardStateHistory.get(this.boardStateHistory.size() - 2);
        this.boardStateHistory.remove(this.boardStateHistory.size() - 1);

        // Toggle state
        if (this.gameState == 1) {
            this.gameState = 2;
        }
        else if (this.gameState == 2) {
            this.gameState = 1;
        }

        // Update GUI
        bokuBoard.updateGUI(boardState);
    }
}
