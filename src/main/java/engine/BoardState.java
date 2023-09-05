package engine;

import java.util.List;

public class BoardState {
    private int[][] field; // First dimension: Numbers representing A-J

    public BoardState() {
        field = new int[10][10];
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
