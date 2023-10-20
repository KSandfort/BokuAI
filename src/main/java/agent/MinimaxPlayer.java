package agent;

import agent.utils.MoveNode;
import engine.BoardState;

import java.util.ArrayList;

public class MinimaxPlayer extends Player{

    int indexNextPieceToTake = -1; // Keeps the next piece to take accessible in memory

    @Override
    public int getMove(BoardState boardState) {
        int[] nextMoveCoordinate = MiniMax(boardState, 2);
        indexNextPieceToTake = nextMoveCoordinate[1]; // Extract second index (in case of capture)
        return nextMoveCoordinate[0];
    }

    @Override
    public int getPieceToTake(int[] toTake) {
        return this.indexNextPieceToTake;
    }

    private int[] MiniMax(BoardState boardState, int maxDepth) {
        // Get possible positions, including captures
        ArrayList<MoveNode> moveNodes = new ArrayList<>();
        int[] possibleMoves = boardState.getPossibleMoves();
        for (int i : possibleMoves) {
            if (i != boardState.getBlockedCoordinate()) {
                // Create new board state and consider if a piece can be taken
                BoardState newBoardState = new BoardState(boardState, boardState.getWhiteToMove(), i);
                MoveNode newMoveNode = new MoveNode(boardState);
                moveNodes.add(i);
            }
        }
        // Iterate over all possible positions and return max score
    }

    private int alphaBeta(MoveNode moveNode, int depth, int alpha, int beta) {

    }
}
