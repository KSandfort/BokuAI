package agent;

import agent.utils.MoveNode;
import engine.BoardState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

public class AlphaBetaPlayer extends Player {

    private static final int MAX_SEARCH_DEPTH = 10;
    int indexNextPieceToTake = -1; // Keeps the next piece to take accessible in memory
    int nodesCreatedCount = 0;
    int nodesEvaluatedCount = 0;
    int pruningCount = 0;
    int ttLookupCount = 0;
    long searchStartTime;
    int timeOutMillis = 10000;
    int currentSearchDepth;
    boolean terminateSearch = false;

    Hashtable<Integer, MoveNode> transpositionTable = new Hashtable<>();

    @Override
    public int getMove(BoardState boardState) {
        this.nodesCreatedCount = 0;
        this.nodesEvaluatedCount = 0;
        this.pruningCount = 0;

        // Start iterative deepening setup

        this.terminateSearch = false;
        this.searchStartTime = System.currentTimeMillis();
        this.currentSearchDepth = 1;
        int[] bestNextMoveCoordinate = new int[]{-1, -1}; // Keeps track of the best move for increasing depths.

        while (!terminateSearch) {
            // Execute search
            System.out.printf("Search depth: %d\n", this.currentSearchDepth);
            int[] nextMoveCoordinate = MiniMax(boardState);

            if (!terminateSearch) {
                // Update best move if no timeout occurred yet
                bestNextMoveCoordinate = nextMoveCoordinate;
                this.indexNextPieceToTake = bestNextMoveCoordinate[1]; // Extract second index (in case of capture)
            }

            this.currentSearchDepth += 1;
        }

        // Update Player Statistics
        this.updatePlayerStatistics(bestNextMoveCoordinate);

        return bestNextMoveCoordinate[0];
    }

    private void updatePlayerStatistics(int[] nextMove) {
        if (this.isWhitePlayer) {
            this.gameController.getPsWhite().setLastMoveIndex(nextMove[0]);
            this.gameController.getPsWhite().setLastTakeIndex(this.indexNextPieceToTake);
            this.gameController.getPsWhite().setNodesCreated(this.nodesCreatedCount);
            this.gameController.getPsWhite().setNodesEvaluated(this.nodesEvaluatedCount);
            this.gameController.getPsWhite().setPruningCount(this.pruningCount);
            this.gameController.getPsWhite().setTotalNodesCreated(this.gameController.getPsWhite().getTotalNodesCreated() + this.nodesCreatedCount);
            this.gameController.getPsWhite().setTotalNodesEvaluated(this.gameController.getPsWhite().getTotalNodesEvaluated() + this.nodesEvaluatedCount);
            this.gameController.getPsWhite().setTotalPruningCount(this.gameController.getPsWhite().getTotalPruningCount() + this.pruningCount);
            this.gameController.getPsWhite().setTranspositionTableSize(this.transpositionTable.size());
            this.gameController.getPsWhite().setTranspositionTableLookups(this.ttLookupCount);
        }
        else {
            this.gameController.getPsBlack().setLastMoveIndex(nextMove[0]);
            this.gameController.getPsBlack().setLastTakeIndex(this.indexNextPieceToTake);
            this.gameController.getPsBlack().setNodesCreated(this.nodesCreatedCount);
            this.gameController.getPsBlack().setNodesEvaluated(this.nodesEvaluatedCount);
            this.gameController.getPsBlack().setPruningCount(this.pruningCount);
            this.gameController.getPsBlack().setTotalNodesCreated(this.gameController.getPsBlack().getTotalNodesCreated() + this.nodesCreatedCount);
            this.gameController.getPsBlack().setTotalNodesEvaluated(this.gameController.getPsBlack().getTotalNodesEvaluated() + this.nodesEvaluatedCount);
            this.gameController.getPsBlack().setTotalPruningCount(this.gameController.getPsBlack().getTotalPruningCount() + this.pruningCount);
            this.gameController.getPsBlack().setTranspositionTableSize(this.transpositionTable.size());
            this.gameController.getPsBlack().setTranspositionTableLookups(this.ttLookupCount);
        }
    }

    @Override
    public int getPieceToTake(int[] toTake) {
        return this.indexNextPieceToTake;
    }

    private ArrayList<MoveNode> getChildNodes(BoardState boardState) {
        ArrayList<MoveNode> moveNodes = new ArrayList<>();
        int[] possibleMoves = boardState.getPossibleMoves();
        for (int i : possibleMoves) {
            if (i != boardState.getBlockedCoordinate()) {
                // Create new board state and consider if a piece can be taken
                BoardState newBoardState = new BoardState(boardState, !boardState.whiteToMove, i);
                // Check for capture
                int[] piecesToTake = newBoardState.getPiecesToTake(i, !boardState.whiteToMove);
                if (piecesToTake.length != 0) {
                    for (int t : piecesToTake) {
                        BoardState takeBoardState = newBoardState.agentPieceCapture(t);
                        MoveNode newMoveNode = new MoveNode(takeBoardState, i, t);
                        // Update move ordering priority
                        if (!newBoardState.whiteToMove) {
                            newMoveNode.setMoveOrderingPriority(10);
                        }
                        else {
                            newMoveNode.setMoveOrderingPriority(-10);
                        }
                        moveNodes.add(newMoveNode);
                    }
                }
                else {
                    MoveNode newMoveNode = new MoveNode(newBoardState, i);
                    moveNodes.add(newMoveNode);
                }
            }
        }
        // Update count of created nodes
        nodesCreatedCount += moveNodes.size();
        return moveNodes;
    }

    private int[] MiniMax(BoardState boardState) {
        // Get possible positions, including captures
        ArrayList<MoveNode> moveNodes = getChildNodes(boardState);
        Collections.sort(moveNodes);
        // Iterate over all possible positions and return max score
        for(MoveNode moveNode : moveNodes) {
            int score = alphaBeta(moveNode, this.currentSearchDepth - 1, -1000, 1000, !this.isWhitePlayer);
            // Assign score to node on internal node below the root node
            moveNode.setScore(score);
        }

        // Select the best move (maximum value for white player, minimum value for black player)
        int maxIndex = 0;
        if (this.isWhitePlayer) {
            int maxScore = -100000;
            for (int i = 0; i < moveNodes.size(); i++) {
                if (moveNodes.get(i).getScore() > maxScore) {
                    maxIndex = i;
                    maxScore = moveNodes.get(i).getScore();
                }
            }
        }
        else {
            int maxScore = 100000;
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
        System.out.printf("Index1 value = %d\n", index1Value);

        System.out.println("-----------------");
        System.out.printf("Nodes Created: %d\n", this.nodesCreatedCount);
        System.out.printf("Nodes Evaluated: %d\n", this.nodesEvaluatedCount);
        System.out.println("-----------------");

        System.out.println("TRANSPOSITION TABLE SIZE: " + transpositionTable.size());
        return new int[]{index0Value, index1Value};
    }

    private int alphaBeta(MoveNode moveNode, int depth, int alpha, int beta, boolean maximizingPlayer) {
        // Check for timeout
        if (this.searchStartTime + this.timeOutMillis <= System.currentTimeMillis()) {
            this.terminateSearch = true;
            return 0;
        }

        // If max search depth is reached
        if (depth <= 0) {
            // Check if position was already evaluated in transposition table
            if (transpositionTable.containsKey(moveNode.getBoardState().getBoardHash())) {
                this.ttLookupCount++;
                return transpositionTable.get(moveNode.getBoardState().getBoardHash()).getScore();
            }
            moveNode.heuristicEvaluation();
            nodesEvaluatedCount += 1;
            transpositionTable.put(moveNode.getBoardState().getBoardHash(), moveNode);
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

        // Create list of all child nodes (without evaluation)
        ArrayList<MoveNode> childNodes = getChildNodes(moveNode.getBoardState());
        Collections.sort(childNodes);

        if (maximizingPlayer) {
            int bestValue = -100000;
            for (MoveNode child : childNodes) {
                int value = alphaBeta(child, depth - 1, alpha, beta, false);
                bestValue = Math.max(bestValue, value);
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    this.pruningCount++;
                    break;  // Beta cutoff | Do da fancy pruning \(*_*)/
                }
            }
            return bestValue;

        }
        else {
            int bestValue = 100000;
            for (MoveNode child : childNodes) {
                int value = alphaBeta(child, depth - 1, alpha, beta, true);
                bestValue = Math.min(bestValue, value);
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    this.pruningCount++;
                    break; // Alpha cutoff | Do da fancy pruning \(*_*)/
                }
            }
            return bestValue;
        }
    }
}
