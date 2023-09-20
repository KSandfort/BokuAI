package engine;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class BoardState {

    int[] board;

    public BoardState() {
        this.board = new int[100];
    }

    public BoardState(BoardState oldBoard, boolean whiteMove, int positionIndex) {
        this.board = oldBoard.getBoard().clone();
        int playerValue;
        if (whiteMove) {
            playerValue = 1;
        }
        else {
            playerValue = -1;
        }
        // Place the piece on the board
        this.board[positionIndex] = playerValue;
    }

    public boolean isGameWon(boolean whitePlayer) {
        return false;
    }

    private boolean isAdjacent(int x1, int y1, int x2, int y2) {
        //TODO: Implement
        return false;
    }

    private boolean didPlayerWin(int player) {
        //TODO: Implement
        return false;
    }


    //TODO: Rethink the whole structure of how to check if a piece can be taken and integrate this into the move.
    private boolean canTake(int player) {
        //TODO: Implement
        return false;
    }

    private int[][] piecesToTake(int player) {
        return null;
    }
}
