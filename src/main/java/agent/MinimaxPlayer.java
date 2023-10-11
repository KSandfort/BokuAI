package agent;

import engine.BoardState;

public class MinimaxPlayer extends Player{

    int indexNextPieceToTake = -1; // Keeps the next piece to take accessible in memory

    @Override
    public int getMove(BoardState boardState) {
        return 0;
    }

    @Override
    public int getPieceToTake(int[] toTake) {
        return this.indexNextPieceToTake;
    }
}
