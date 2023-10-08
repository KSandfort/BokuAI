package agent;

import engine.BoardState;

public class HumanPlayer extends Player {

    @Override
    public int getMove(BoardState boardState) {
        return 0;
    }

    @Override
    public int getPieceToTake(int[] toTake) {
        return 0;
    }
}
