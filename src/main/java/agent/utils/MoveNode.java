package agent.utils;

import engine.BoardState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveNode implements Comparable<MoveNode> {

    private int score = 0;
    private BoardState boardState;
    int newTile;
    int captureTile = -1;

    public MoveNode(BoardState bs, int nt, int ct) {
        this.boardState = bs;
        this.newTile = nt;
        this.captureTile = ct;
    }

    public MoveNode(BoardState bs, int nt) {
        this.boardState = bs;
        this.newTile = nt;
    }

    public void heuristicEvaluation() {
        this.score = StateEvaluator.evaluate_v1(this.boardState);
    }

    @Override
    public int compareTo(MoveNode o) {
        if (o.getScore() == this.score) {
            return 0;
        }
        else if (o.getScore() < this.score){
            return 1;
        }
        else {
            return -1;
        }
    }
}
