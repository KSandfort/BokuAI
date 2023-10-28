package agent.utils;

import engine.BoardState;
import lombok.Getter;
import lombok.Setter;

/**
 * A move node is used for the alpha-beta pruning method and stores all the necessary information of the state that
 * defines the specific node.
 */
@Getter
@Setter
public class MoveNode implements Comparable<MoveNode> {

    private int score = 0; // Heuristic score (initially 0)
    private BoardState boardState; // Current board state
    int newTile; // New tile that is being placed
    int captureTile = -1; // Tile that is being taken
    int moveOrderingPriority = 0; // Higher value means higher priority in the move ordering

    /**
     * Constructor for a new move node after a piece is placed and another one is taken.
     * @param bs board state
     * @param nt new tile coordinate
     * @param ct captured tile coordinate
     */
    public MoveNode(BoardState bs, int nt, int ct) {
        this.boardState = bs;
        this.newTile = nt;
        this.captureTile = ct;
    }

    /**
     * Constructor for a new move node after a piece is placed.
     * @param bs
     * @param nt
     */
    public MoveNode(BoardState bs, int nt) {
        this.boardState = bs;
        this.newTile = nt;
    }

    /**
     * Executes a heuristic evaluation on the board states and updates the current score.
     */
    public void heuristicEvaluation() {
        this.score = StateEvaluator.evaluate_v1(this.boardState);
    }

    /**
     * Compares the current move node to another one in terms of move ordering priority.
     * This mechanic is essential for the move ordering.
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(MoveNode o) {
        if (o.getMoveOrderingPriority() == this.moveOrderingPriority) {
            return 0;
        }
        else if (o.getMoveOrderingPriority() < this.moveOrderingPriority){
            return 1;
        }
        else {
            return -1;
        }
    }
}
