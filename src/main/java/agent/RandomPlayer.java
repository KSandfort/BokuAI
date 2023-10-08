package agent;

import engine.BoardState;

import java.util.Random;

public class RandomPlayer extends Player{
    @Override
    public int getMove(BoardState boardState) {
        int[] possibleMoves = boardState.getPossibleMoves();
        Random random = new Random();
        int randomIndex = random.nextInt(possibleMoves.length - 1);
        int nextMove = possibleMoves[randomIndex];
        System.out.println("Random Move Coord: " + nextMove);
        return nextMove;
    }

    @Override
    public int getPieceToTake(int[] toTake) {
        Random random = new Random();
        return toTake[random.nextInt(toTake.length - 1)];
    }
}
