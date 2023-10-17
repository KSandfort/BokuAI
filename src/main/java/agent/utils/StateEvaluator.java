package agent.utils;

import engine.BoardState;

public class StateEvaluator {
    //Todo: Add multiple evaluation methods that can be called

    // Feature weights
    private static final int BOUND = 1000;
    private static final int PIECE_COUNT = 1;
    private static final int ROW_OF_2 = 30;
    private static final int ROW_OF_3 = 60;
    private static final int ROW_OF_4 = 90;
    private static final int OPEN_ROW_OF_2 = 60;
    private static final int OPEN_ROW_OF_3 = 120;
    private static final int OPEN_ROW_OF_4 = 180;
    private static final int GAP_OF_3 = 30;
    private static final int GAP_OF_2 = 60;
    private static final int GAP_OF_1 = 90;
    private static final int CAPTURE_POSSIBLE = 50;

    public int evaluate_v1(BoardState boardState, boolean whitePlayer) {
        short playerCode;
        if (whitePlayer) {
            playerCode = 1;
        }
        else {
            playerCode = -1;
        }

        // Check for game termination
        if (boardState.isGameWon(whitePlayer)) {
            return BOUND;
        }
        if (boardState.isGameWon(!whitePlayer)) {
            return -BOUND;
        }
        if (boardState.isDraw()) {
            return -1000;
        }

        // Init score
        int score = 0;

        // Get number of pieces for each player
        for (int i = 0; i < boardState.getBoard().length; i++) {
            if (boardState.getBoard()[i] == playerCode) {
                score += PIECE_COUNT;
            }
            if (boardState.getBoard()[i] == playerCode * -1) {
                score -= PIECE_COUNT;
            }
        }


        return score;
    }
}
