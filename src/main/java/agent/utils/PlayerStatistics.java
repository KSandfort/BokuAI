package agent.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides statistics that are shown on the right hand side of the game.
 */
@Getter
@Setter
public class PlayerStatistics {
    boolean isWhitePlayer;
    String playerType;
    int lastMoveIndex;
    int lastTakeIndex;
    int nodesCreated;
    int nodesEvaluated;
    int pruningCount;
    int totalNodesCreated;
    int totalNodesEvaluated;
    int totalPruningCount;
    int transpositionTableSize;
    int transpositionTableLookups;


    /**
     * Constructor
     * @param isWhitePlayer true if target player is the white player
     */
    public PlayerStatistics(boolean isWhitePlayer) {
        this.isWhitePlayer = isWhitePlayer;
        this.lastMoveIndex = -1;
        this.lastTakeIndex = -1;
        this.nodesCreated = 0;
        this.nodesEvaluated = 0;
        this.totalNodesCreated = 0;
        this.totalNodesEvaluated = 0;
    }

    /**
     * Converts all instance variables to a string that is displayed in the GUI.
     * @return formatted string
     */
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
        output += "Pruning Count: \t" + pruningCount + "\n";
        output += "---------------------\n";
        output += "Total:\n";
        output += "Nodes Created: \t" + this.totalNodesCreated + "\n";
        output += "Nodes Evaluated: \t" + this.totalNodesEvaluated + "\n";
        output += "Pruning Count: \t" + totalPruningCount + "\n";
        output += "---------------------\n";
        output += "TT Size: \t" + this.transpositionTableSize + "\n";
        output += "TT Lookups: \t" + this.transpositionTableLookups + "\n";
        return output;
    }
}
