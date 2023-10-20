package agent.utils;

import engine.BoardState;

public class StateEvaluator {
    //Todo: Add multiple evaluation methods that can be called

    private static final boolean DEBUG_LOG = true;

    // Feature weights
    private static final int BOUND = 100000;
    private static final int PIECE_COUNT = 1;
    private static final int ROW_OF_2 = 30;
    private static final int ROW_OF_3 = 60;
    private static final int ROW_OF_4 = 90;
    private static final int MAX_TILES_IN_UNBLOCKED_ROW = 30;
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
        int ownPlayerPieces = 0;
        int oppPlayerPieces = 0;

        // Get number of pieces for each player
        for (int i = 0; i < boardState.getBoard().length; i++) {
            if (boardState.getBoard()[i] == 1) {
                ownPlayerPieces += 1;
            }
            if (boardState.getBoard()[i] == -1) {
                oppPlayerPieces += 1;
            }
        }

        score += ownPlayerPieces * PIECE_COUNT;
        score -= oppPlayerPieces * PIECE_COUNT;

        // Obtain all board features in one array
        int[] boardFeatures = boardState.getBoardFeatures();
        score += boardFeatures[0] * ROW_OF_2; // Own player rows of 2
        score -= boardFeatures[1] * ROW_OF_2; // Opp player rows of 2
        score += boardFeatures[2] * ROW_OF_3; // Own player rows of 3
        score -= boardFeatures[3] * ROW_OF_3; // Opp player rows of 3
        score += boardFeatures[4] * ROW_OF_4; // Own player rows of 4
        score -= boardFeatures[5] * ROW_OF_4; // Opp player rows of 4
        score += boardFeatures[6] * MAX_TILES_IN_UNBLOCKED_ROW; // Own player maximum number of tiles in unblocked row
        score -= boardFeatures[7] * MAX_TILES_IN_UNBLOCKED_ROW; // Opp player maximum number of tiles in unblocked row
        score += boardFeatures[8] * CAPTURE_POSSIBLE; // Own player possible captures
        score -= boardFeatures[9] * CAPTURE_POSSIBLE; // Opp player possible captures

        if (DEBUG_LOG) {
            System.out.println("--- BOARD EVALUATION ---");
            System.out.printf("Own pieces: %d \n", ownPlayerPieces);
            System.out.printf("Own pieces: %d \n", oppPlayerPieces);
            for (int i = 0; i < boardFeatures.length; i++) {
                System.out.printf("Board Feature %d: %d \n", i, boardFeatures[i]);
            }
            System.out.printf("Total Score: %d \n", score);
            System.out.println("-------------------------");
        }

        return score;
    }
}
