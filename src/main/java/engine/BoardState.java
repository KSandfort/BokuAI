package engine;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class BoardState {

    private BigInteger whitePieces;
    private BigInteger blackPieces;

    public BoardState(BigInteger whiteP, BigInteger blackP) {
        this.whitePieces = whiteP;
        this.blackPieces = blackP;
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
