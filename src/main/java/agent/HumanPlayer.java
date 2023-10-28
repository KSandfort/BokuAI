package agent;

import engine.BoardState;

/**
 * Placeholder for a human player.
 */
public class HumanPlayer extends Player {

    /**
     * No functionality, inherited method.
     * @param boardState current board state
     * @return next move position
     */
    @Override
    public int getMove(BoardState boardState) {
        return 0;
    }

    /**
     * No functionality, inherited method.
     * @param toTake list of all pieces to take
     * @return best piece to take
     */
    @Override
    public int getPieceToTake(int[] toTake) {
        return 0;
    }
}
