package agent.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerStatistics {
    boolean isWhitePlayer;
    String playerType;
    int lastMoveIndex;
    int lastTakeIndex;
    int nodesCreated;
    int nodesEvaluated;
    int totalNodesCreated;
    int totalNodesEvaluated;

    public PlayerStatistics(boolean white) {
        this.isWhitePlayer = white;
        this.lastMoveIndex = -1;
        this.lastTakeIndex = -1;
        this.nodesCreated = 0;
        this.nodesEvaluated = 0;
        this.totalNodesCreated = 0;
        this.totalNodesEvaluated = 0;
    }

    public String out() {
        String output = "";
        if (isWhitePlayer) {
            output += "White Player:\n";
        }
        else {
            output += "Black Player:\n";
        }
        output += "---------------------\n";
        output += "Last Move: \t" + lastMoveIndex + "\n";
        output += "Last Capture: \t" + lastTakeIndex + "\n";
        output += "Nodes Created: \t" + nodesCreated + "\n";
        output += "Nodes Evaluated: \t" + nodesEvaluated + "\n";
        output += "---------------------\n";
        output += "Total:\n";
        output += "Nodes Created: \t" + this.totalNodesCreated + "\n";
        output += "Nodes Evaluated: \t" + this.totalNodesEvaluated+ "\n";
        return output;
    }
}
