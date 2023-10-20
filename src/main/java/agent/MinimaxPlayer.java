package agent;

import agent.utils.MoveNode;
import engine.BoardState;

import java.util.ArrayList;

public class MinimaxPlayer extends Player {

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
                BoardState newBoardState = new BoardState(boardState, !boardState.whiteToMove, i);
                // Check for capture
                int[] piecesToTake = newBoardState.getPiecesToTake(i, boardState.whiteToMove);
                if (piecesToTake.length != 0) {
                    for (int t : piecesToTake) {
                        BoardState takeBoardState = boardState.agentPieceCapture(t);
                        MoveNode newMoveNode = new MoveNode(takeBoardState, i, t);
                        moveNodes.add(newMoveNode);
                    }
                }
                else {
                    MoveNode newMoveNode = new MoveNode(newBoardState, i, -1);
                    moveNodes.add(newMoveNode);
                }
            }
        }
        // Iterate over all possible positions and return max score
        for(MoveNode moveNode : moveNodes) {
            alphaBeta(moveNode, maxDepth - 1, -1000, 1000, !this.isWhitePlayer); // Start with min
        }

        // Select best move
        int maxIndex = -1;
        if (this.isWhitePlayer) {
            int maxScore = -1000;
            for (int i = 0; i < moveNodes.size(); i++) {
                if (moveNodes.get(i).getScore() > maxScore) {
                    maxIndex = i;
                    maxScore = moveNodes.get(i).getScore();
                }
            }
        }
        else {
            int maxScore = 1000;
            for (int i = 0; i < moveNodes.size(); i++) {
                if (moveNodes.get(i).getScore() < maxScore) {
                    maxIndex = i;
                    maxScore = moveNodes.get(i).getScore();
                }
            }
        }

        // Return new array with position of where to place the next tile and in case of a capture where to take one.
        int index0Value = moveNodes.get(maxIndex).getNewTile();
        int index1Value = moveNodes.get(maxIndex).getCaptureTile();
        return new int[]{index0Value, index1Value};
    }

    private int alphaBeta(MoveNode moveNode, int depth, int alpha, int beta, boolean maximizingPlayer) {
        // If max search depth is reached
        if (depth == 0) {
            moveNode.heuristicEvaluation();
            return moveNode.getScore();
        }
        // If node is terminal
        if (moveNode.getBoardState().isGameWon(maximizingPlayer ^ this.isWhitePlayer)) {
            if (this.isWhitePlayer) {
                return 100000;
            }
            else {
                return -100000;
            }
        }

        if (maximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (child in node's children):
                int value = alphaBeta(child, depth - 1, false, alpha, beta);
                int bestValue = max(bestValue, value);
                alpha = max(alpha, bestValue)
                if beta <= alpha:
                    break;  // Beta cutoff
            return bestValue
            // Get score
            // For every child
            // Value = recursive call

            // if (value > score) -> score = value
            // if (score > alpha) -> alpha = score
            // if (score >= beta) -> break (PRUNING)

            // return score
        }
        else {
            int bestValue = Integer.MAX_VALUE;
            for (child in node's children)
                value = minimax(child, depth - 1, true, alpha, beta)
                bestValue = min(bestValue, value)
                beta = min(beta, bestValue)
                if beta <= alpha:
                    break  # Alpha cutoff
            return bestValue
        }
    }
}
