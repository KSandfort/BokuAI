package engine;

import agent.HumanPlayer;
import agent.MinimaxPlayer;
import agent.Player;
import agent.RandomPlayer;
import agent.utils.StateEvaluator;
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
    private int gameState; // 0 = pause, 1 = player 1 to move, 2 = player 2 to move, 3 = player 1 won, 4 = player 2 won, 5 = white can take, 6 = black can take, 7 = draw
    private int moveCount;
    private Player player1;
    private Player player2;

    private BoardState boardState;

    private List<BoardState> boardStateHistory;

    private boolean humanVsAgent = false;
    private boolean agentVsHuman = false;

    public GameController(App appInstance) {
        this.appInstance = appInstance;
        this.gameState = 0;
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
        player1.isWhitePlayer = true;
        this.player2 = this.getPlayerInstanceByName(player2Selection);
        player2.isWhitePlayer = false;
        if (DEBUG_LOG) {
            System.out.println("Starting game");
        }
        this.appInstance.setWhoToTurnLabel(this.gameState);

        // Human vs Agent
        if (this.player1 instanceof HumanPlayer && !(this.player2 instanceof HumanPlayer)) {
            this.humanVsAgent = true;
        }

        // Agent vs Human
        if (!(this.player1 instanceof HumanPlayer) && this.player2 instanceof HumanPlayer) {
            this.agentVsHuman = true;
            this.executeMove(player2.getMove(this.boardState));
        }

        // Agent vs Agent - Enter game loop
        if (!(this.player1 instanceof HumanPlayer || this.player2 instanceof HumanPlayer)) {
            //gameLoopAgentVsAgent();
        }

    }

    public void agentVsAgentOneStep() {
        // If player 1 is an agent, perform action
        if (this.gameState == 1) {
            this.executeMove(player1.getMove(this.boardState));
        }
        // If player 1 is an agent, perform action
        else if (this.gameState == 2) {
            this.executeMove(player2.getMove(this.boardState));
        }

        this.bokuBoard.updateGUI(this.boardState);
    }

    /*
    private void gameLoopAgentVsAgent() {
        do {
            // If player 1 is an agent, perform action
            if (this.gameState == 1) {
                this.executeMove(player1.getMove(this.boardState));
            }
            // If player 1 is an agent, perform action
            else if (this.gameState == 2) {
                this.executeMove(player2.getMove(this.boardState));
            }

            Platform.runLater(() -> {
                this.bokuBoard.updateGUI(this.boardState);
            });

            try {
                Platform.runLater(() -> this.bokuBoard.updateGUI(this.getBoardState()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (this.gameState != 3 && this.gameState != 4);
    }
    */

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

        // Check for draw
        if (this.boardState.isDraw()) {
            this.gameState = 7;
            System.out.println("It's a draw!");
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
            if ((this.gameState == 5 && this.player1 instanceof HumanPlayer) || (this.gameState == 6 && this.player2 instanceof HumanPlayer)) {
                for (int i : piecesToTake) {
                    bokuBoard.placeTakeMarker(i);
                }
            }
            else {
                if (this.gameState == 5) {
                    removePiece(player1.getPieceToTake(piecesToTake));
                    this.gameState = 2;
                }
                else {
                    removePiece(player2.getPieceToTake(piecesToTake));
                    this.gameState = 1;
                }
            }
        }

        //TODO: Remove later
        StateEvaluator.evaluate_v1(this.boardState);
        System.out.println(this.boardState.possibleCapturesNextMove(1).length);

        // Toggle Player Turn
        togglePlayerTurn();
        this.appInstance.setWhoToTurnLabel(this.gameState);

        // If human player plays against AI, trigger next move
        if (humanVsAgent && this.gameState == 2) {
            this.executeMove(player2.getMove(this.boardState));
        }
        else if (agentVsHuman && this.gameState == 1) {
            this.executeMove(player1.getMove(this.boardState));
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
        // Update state history and blocked coordinate
        this.boardState.setBlockedCoordinate(coordinateIndex);
        this.boardStateHistory.remove(this.boardStateHistory.size() - 1);
        this.boardStateHistory.add(this.boardState);

        if (this.gameState == 2 && this.player1 instanceof HumanPlayer && !(this.player2 instanceof HumanPlayer)) {
            this.executeMove(player2.getMove(this.boardState));
        }
        if (this.gameState == 1 && this.player2 instanceof HumanPlayer && !(this.player1 instanceof HumanPlayer)) {
            this.executeMove(player1.getMove(this.boardState));
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
