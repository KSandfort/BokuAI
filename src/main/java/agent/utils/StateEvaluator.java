package agent.utils;

import engine.BoardState;

import java.util.Random;

public class StateEvaluator {
    //Todo: Add multiple evaluation methods that can be called

    private static final boolean DEBUG_LOG = false;
    private static final int RAND_SPREAD = 20;

    // Feature weights
    private static final int BOUND = 1000000;
    private static final int PIECE_COUNT = 30;
    private static final int ROW_OF_2 = 10;
    private static final int ROW_OF_3 = 100;
    private static final int ROW_OF_4 = 500;

    private static final int ROW_OF_3_BOTH_OPEN = 300;
    private static final int ROW_OF_4_ONE_OPEN = 2000;
    private static final int ROW_OF_4_BOTH_OPEN = 10000;
    private static final int CAPTURE_POSSIBLE = 50;

    public static int evaluate_v1(BoardState boardState) {

        // Check for game termination
        if (boardState.isGameWon(true)) {
            return BOUND;
        }
        if (boardState.isGameWon(false)) {
            return -BOUND;
        }
        if (boardState.isDraw()) {
            return -BOUND;
        }

        // Init score
        int score = 0;
        int whitePlayerPieces = 0;
        int blackPlayerPieces = 0;

        // Get number of pieces for each player
        for (int i = 0; i < boardState.getBoard().length; i++) {
            if (boardState.getBoard()[i] == 1) {
                whitePlayerPieces += 1;
            }
            if (boardState.getBoard()[i] == -1) {
                blackPlayerPieces += 1;
            }
        }

        score += whitePlayerPieces * PIECE_COUNT;
        score -= blackPlayerPieces * PIECE_COUNT;

        // Obtain all board features in one array
        int[] boardFeatures = boardState.getBoardFeatures();
        score += boardFeatures[0] * ROW_OF_2; // Own player rows of 2
        score -= boardFeatures[1] * ROW_OF_2; // Opp player rows of 2
        score += boardFeatures[2] * ROW_OF_3; // Own player rows of 3
        score -= boardFeatures[3] * ROW_OF_3; // Opp player rows of 3
        score += boardFeatures[4] * ROW_OF_4; // Own player rows of 4
        score -= boardFeatures[5] * ROW_OF_4; // Opp player rows of 4
        //score += boardFeatures[6] * MAX_TILES_IN_UNBLOCKED_ROW; // Own player maximum number of tiles in unblocked row
        //score -= boardFeatures[7] * MAX_TILES_IN_UNBLOCKED_ROW; // Opp player maximum number of tiles in unblocked row
        score += boardFeatures[6] * CAPTURE_POSSIBLE; // Own player possible captures
        score -= boardFeatures[7] * CAPTURE_POSSIBLE; // Opp player possible captures

        score += boardFeatures[8] * ROW_OF_3_BOTH_OPEN;
        score += boardFeatures[9] * ROW_OF_4_ONE_OPEN;
        score += boardFeatures[10] * ROW_OF_4_BOTH_OPEN;

        score -= boardFeatures[11] * ROW_OF_3_BOTH_OPEN;
        score -= boardFeatures[12] * ROW_OF_4_ONE_OPEN;
        score -= boardFeatures[13] * ROW_OF_4_BOTH_OPEN;

        if (DEBUG_LOG) {
            System.out.println("--- BOARD EVALUATION ---");
            System.out.printf("Own pieces: %d \n", whitePlayerPieces);
            System.out.printf("Own pieces: %d \n", blackPlayerPieces);
            for (int i = 0; i < boardFeatures.length; i++) {
                System.out.printf("Board Feature %d: %d \n", i, boardFeatures[i]);
            }
            System.out.printf("Total Score: %d \n", score);
            System.out.println("-------------------------");
        }
        int rand = (int) (Math.random() * (RAND_SPREAD * 2) - RAND_SPREAD);
        return score + rand;
    }
}
