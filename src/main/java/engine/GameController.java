package engine;

import agent.HumanPlayer;
import agent.MinimaxPlayer;
import agent.Player;
import agent.RandomPlayer;
import gui.App;
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

    private App appInstance;

    private static boolean DEBUG_LOG = true;

    BokuBoard bokuBoard;

    // State properties
    private int gameState; // 0 = pause, 1 = player 1 to move, 2 = player 2 to move, 3 = player 1 won, 4 = player 2 won, 5 = white can take, 6 = black can take
    private int moveCount;
    private Player player1;
    private Player player2;

    private BoardState boardState;

    private List<BoardState> boardStateHistory;

    private boolean humanVsAgent = false;
    private boolean agentVsHuman = false;

    public GameController(App appInstance) {
        this.appInstance = appInstance;
        this.gameState = 0; //Todo: Remove later
        this.boardStateHistory = new ArrayList<>();
        // Create new empty board
        this.boardState = new BoardState();
        // Add empty board to history
        this.boardStateHistory.add(boardState);
    }

    public void initNewGame(String player1Selection, String player2Selection) {
        gameState = 1;
        moveCount = 0;
        this.player1 = this.getPlayerInstanceByName(player1Selection);
        this.player2 = this.getPlayerInstanceByName(player2Selection);
        if (DEBUG_LOG) {
            System.out.println("Starting game");
        }
        this.appInstance.setWhoToTurnLabel(this.gameState);
        // Start game loop if both players are agents
        if (!(this.player1 instanceof HumanPlayer || this.player2 instanceof HumanPlayer)) {
            gameLoopAgentVsAgent();
        }

        // Human vs Agent
        if (this.player1 instanceof HumanPlayer && !(this.player2 instanceof HumanPlayer)) {
            this.humanVsAgent = true;
        }

        // Agent vs Human
        if (!(this.player1 instanceof HumanPlayer) && this.player2 instanceof HumanPlayer) {
            this.agentVsHuman = true;
            // TODO: Execute move for player (agent) 1
        }

    }

    private void gameLoopAgentVsAgent() {
        do {
            // If player 1 is an agent, perform action
            if (this.gameState == 1) {

            }
            // If player 1 is an agent, perform action
            else if (this.gameState == 2) {

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (this.gameState != 3 && this.gameState != 4);
    }

    private void gameLoopHumanVsAgent() {

    }

    private void gameLoopAgentVsHuman() {

    }

    /**
     * Attempts to make a move when a human player clicks a tile to place a stone
     */
    public void attemptMoveOnClick(int coordinateIndex) {
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
        if (coordinateIndex == this.boardState.getBlockedCoordinate()) {
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
            System.out.println("No valid game state");
        }
        // Set new state to current one
        this.boardState = newBoardState;
        this.moveCount += 1;
        if (DEBUG_LOG) {
            System.out.println("Game State " + this.gameState);
            System.out.println("Board: " + Arrays.toString(this.boardState.getBoard()));
        }
        // Add to history
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
        }

        // Toggle Player Turn
        togglePlayerTurn();
        this.appInstance.setWhoToTurnLabel(this.gameState);

        // If human player plays against AI, trigger next move
        if (humanVsAgent && this.gameState == 2) {
            // TODO: Get and execute move for player 2
        }
        else if (agentVsHuman && this.gameState == 1) {
            // TODO: Get and execute move for player 2
        }
    }

    public void removePiece(int coordinateIndex) {
        // Update back end
        this.boardState.getBoard()[coordinateIndex] = 0;
        this.boardStateHistory.remove(this.boardStateHistory.size() - 1);
        this.boardStateHistory.add(boardState);
        // Update GUI
        for (Hexagon hex : this.bokuBoard.getHexagons()) {
            if (hex.getCoordinateIndex() == coordinateIndex) {
                hex.setBaseColour(Hexagon.unsetColour);
                hex.setFill(hex.getBaseColour());
            }
        }
        bokuBoard.removeAllTakePieceIndicators(this.gameState);
        this.boardState.setBlockedCoordinate(coordinateIndex);
        this.boardStateHistory.remove(this.boardStateHistory.size() - 1);
        this.boardStateHistory.add(this.boardState);
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

        this.moveCount -= 1;

        // Update GUI
        bokuBoard.updateGUI(this.boardState);
    }

    private Player getPlayerInstanceByName(String name) {
        if (name.equals("Human Player")) {
            return new HumanPlayer();
        }
        if (name.equals("Random")) {
            return new RandomPlayer();
        }
        if (name.equals("Minimax")) {
            return new MinimaxPlayer();
        }

        return new HumanPlayer();
    }
}
