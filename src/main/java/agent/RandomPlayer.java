package agent;

import engine.BoardState;
import java.util.Random;

/**
 * This class contains the mechanics of a random agent.
 * Not the best but also not the worst possible agent!
 * If you can beat this agent, you can call yourself a real Boku player!
 */
public class RandomPlayer extends Player{

    /**
     * Fetches next move.
     * @param boardState current board state
     * @return next move position
     */
    @Override
    public int getMove(BoardState boardState) {
        int[] possibleMoves = boardState.getPossibleMoves();
        Random random = new Random();
        int randomIndex = random.nextInt(possibleMoves.length - 1);
        int nextMove = possibleMoves[randomIndex];
        System.out.println("Random Move Coord: " + nextMove);
        return nextMove;
    }

    /**
     * Fetches next piece to capture.
     * @param toTake list of all pieces to take
     * @return best piece to take
     */
    @Override
    public int getPieceToTake(int[] toTake) {
        Random random = new Random();
        return toTake[random.nextInt(toTake.length - 1)];
    }
}
