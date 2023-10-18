package engine;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
/**
 * This class holds an abstract representation of the board state at a given moment during the game.
 */
public class BoardState {

    int[] board;
    boolean whiteToMove;
    int blockedCoordinate = -1;

    public BoardState() {
        this.board = new int[100];
        this.whiteToMove = true;
    }

    public BoardState(BoardState oldBoard, boolean whiteMove, int positionIndex) {
        this.board = oldBoard.getBoard().clone();
        this.whiteToMove = whiteMove;
        int playerValue;
        if (whiteMove) {
            playerValue = 1;
        }
        else {
            playerValue = -1;
        }
        // Place the piece on the board
        this.board[positionIndex] = playerValue;
    }

    public int[] getPossibleMoves() {
        ArrayList<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0 && isCoordinateValid(coord1dTo2d(i))) {
                targetList.add(i);
            }
        }
        return targetList.stream().mapToInt(i -> i).toArray();
    }

    public boolean isGameWon(boolean whitePlayer) {
        //TODO: Make more efficient by just checking tiles in a certain distance (direct line) of the piece that is put
        int playerCode;
        if (whitePlayer) {
            playerCode = 1;
        }
        else {
            playerCode = -1;
        }
        // Check rows in all 3 possible orientations
        // First diagonal
        int counter;
        for (int i = 0; i < 10; i++) {
            counter = 0;
            for (int j = 0; j < 10; j++) {
                if (this.board[coord2dTo1d(new int[]{i, j})] == playerCode) {
                    counter += 1;
                    if (counter >= 5) {
                        return true;
                    }
                }
                else {
                    counter = 0;
                }
            }
        }

        // Second diagonal
        counter = 0;
        for (int i = 0; i < 10; i++) {
            counter = 0;
            for (int j = 0; j < 10; j++) {
                if (this.board[coord2dTo1d(new int[]{j, i})] == playerCode) {
                    counter += 1;
                    if (counter >= 5) {
                        return true;
                    }
                }
                else {
                    counter = 0;
                }
            }
        }

        // Vertical
        // Left Side
        for (int i = 0; i < 7; i++) {
            counter = 0;
            for (int j = 0; j < 10; j++) {
                if (i + j < 10) {
                    if (this.board[coord2dTo1d(new int[]{i + j, j})] == playerCode) {
                        counter += 1;
                        if (counter >= 5) {
                            return true;
                        }
                    } else {
                        counter = 0;
                    }
                }
            }
        }
        // Right side
        for (int i = 1; i < 7; i++) {
            counter = 0;
            for (int j = 0; j < 10; j++) {
                if (i + j < 10) {
                    if (this.board[coord2dTo1d(new int[]{j, i + j})] == playerCode) {
                        counter += 1;
                        if (counter >= 5) {
                            return true;
                        }
                    } else {
                        counter = 0;
                    }
                }
            }
        }

        return false;
    }

    public boolean isDraw() {
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] == 0 && isCoordinateValid(coord1dTo2d(this.board[i]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if pieces can be taken.
     * @param coordinateIndex one-dimensional index of the current tile that is being placed
     * @param whitePlayer true if it is the white player's turn
     * @return array of indices that describe positions where opponents' pieces can be taken
     */
    public int[] getPiecesToTake(int coordinateIndex, boolean whitePlayer) {
        // Convert coordinate into two components
        int[] coord2d = this.coord1dTo2d(coordinateIndex);

        // Translate whitePlayer boolean to player code
        int playerCode;
        if (whitePlayer) {
            playerCode = 1;
        }
        else {
            playerCode = -1;
        }

        // Init empty arraylist
        List<Integer> toTake = new ArrayList<>();
        int[] firstStep;
        int[] secondStep;
        int[] thirdStep;

        // Check top left direction
        firstStep = new int[]{coord2d[0] + 1, coord2d[1]};
        secondStep = new int[]{coord2d[0] + 2, coord2d[1]};
        thirdStep = new int[]{coord2d[0] + 3, coord2d[1]};

        if (canTakeOneDir(firstStep, secondStep, thirdStep, playerCode)) {
            toTake.add(coord2dTo1d(firstStep));
            toTake.add(coord2dTo1d(secondStep));
        }

        // Check bottom right direction
        firstStep = new int[]{coord2d[0] - 1, coord2d[1]};
        secondStep = new int[]{coord2d[0] - 2, coord2d[1]};
        thirdStep = new int[]{coord2d[0] - 3, coord2d[1]};

        if (canTakeOneDir(firstStep, secondStep, thirdStep, playerCode)) {
            toTake.add(coord2dTo1d(firstStep));
            toTake.add(coord2dTo1d(secondStep));
        }

        // Check bottom left direction
        firstStep = new int[]{coord2d[0], coord2d[1] - 1};
        secondStep = new int[]{coord2d[0], coord2d[1] - 2};
        thirdStep = new int[]{coord2d[0], coord2d[1] - 3};

        if (canTakeOneDir(firstStep, secondStep, thirdStep, playerCode)) {
            toTake.add(coord2dTo1d(firstStep));
            toTake.add(coord2dTo1d(secondStep));
        }

        // Check top right direction
        firstStep = new int[]{coord2d[0], coord2d[1] + 1};
        secondStep = new int[]{coord2d[0], coord2d[1] + 2};
        thirdStep = new int[]{coord2d[0], coord2d[1] + 3};

        if (canTakeOneDir(firstStep, secondStep, thirdStep, playerCode)) {
            toTake.add(coord2dTo1d(firstStep));
            toTake.add(coord2dTo1d(secondStep));
        }

        // Check downwards direction
        firstStep = new int[]{coord2d[0] - 1, coord2d[1] - 1};
        secondStep = new int[]{coord2d[0] - 2, coord2d[1] - 2};
        thirdStep = new int[]{coord2d[0] - 3, coord2d[1] - 3};

        if (canTakeOneDir(firstStep, secondStep, thirdStep, playerCode)) {
            toTake.add(coord2dTo1d(firstStep));
            toTake.add(coord2dTo1d(secondStep));
        }

        // Check upwards direction
        firstStep = new int[]{coord2d[0] + 1, coord2d[1] + 1};
        secondStep = new int[]{coord2d[0] + 2, coord2d[1] + 2};
        thirdStep = new int[]{coord2d[0] + 3, coord2d[1] + 3};

        if (canTakeOneDir(firstStep, secondStep, thirdStep, playerCode)) {
            toTake.add(coord2dTo1d(firstStep));
            toTake.add(coord2dTo1d(secondStep));
        }

        return toTake.stream().mapToInt(i -> i).toArray();
    }

    public boolean canTakeOneDir(int[] step1, int[] step2, int[] step3, int playerCode) {
        // Check if line is within the board
        if (!(isCoordinateValid(step1) && isCoordinateValid(step2) && isCoordinateValid(step3))) {
            return false;
        }
        if (
                board[coord2dTo1d(step1)] == playerCode * -1 &&
                board[coord2dTo1d(step2)] == playerCode * -1 &&
                board[coord2dTo1d(step3)] == playerCode
        ) {
            return true;
        }
        return false;
    }

    private boolean isCoordinateValid(int[] coord) {
        if (coord[0] - coord[1] >= 6) { // LHS cutoff bound
            return false;
        }
        if (coord[1] - coord[0] >= 6) { // RHS cutoff bound
            return false;
        }
        if (coord[0] > 9 || coord[0] < 0 || coord[1] > 9 || coord[1] < 0 ) { // Out of coordinate range 0 - 9
            return false;
        }
        return true;
    }

    public int coord2dTo1d(int[] index2d) {
        return (index2d[0] * 10) + index2d[1];
    }

    public int[] coord1dTo2d(int index1d) {
        return new int[]{index1d / 10, index1d % 10};
    }

    public int[] getBoardFeatures(int playerCode) {
        int[] result = new int[14];
        int nRows2Own = 0; //  0 = Own player rows of 2
        int nRows2Opp = 0; //  1 = Opp player rows of 2
        //  2 = Own player rows of 3
        //  3 = Opp player rows of 3
        //  4 = Own player rows of 4
        //  5 = Opp player rows of 4
        //  6 = Own player rows of 2
        //  7 = Opp player rows of 2
        //  8 = Own player rows of 3
        //  9 = Opp player rows of 3
        // 10 = Own player rows of 4
        // 11 = Opp player rows of 4
        // 12 = Own player possible captures
        // 13 = Opp player possible captures

        int countOwn = 0;
        int countOpp = 0;
        int countOwnGap = 0;
        int countOppGap = 0;
        int blanksOwn = 0;
        int blanksOpp = 0;

        int maxCountOwn = 0;
        int maxCountOpp = 0;
        int maxCountOwnGap = 0;
        int maxCountOppGap = 0;

        // First diagonal
        for (int i = 0; i < 10; i++) { // For each column
            countOwn = 0;
            countOwnGap = 0;
            for (int j = 0; j < 10; j++) { // For each element within the column
                if (isCoordinateValid(new int[]{i, j})) { // Check validity of cell
                    int cellCode = this.board[coord2dTo1d(new int[]{i, j})];
                    if (cellCode == playerCode) {
                        countOwn += 1;
                        countOpp = 0;
                        countOwnGap += 1;
                        countOppGap = 0;
                        blanksOwn = 0;
                        blanksOpp = 0;
                    }
                    if (cellCode == playerCode * -1) {
                        countOwn = 0;
                        countOpp += 1;
                        countOwnGap = 0;
                        countOppGap += 1;
                        blanksOwn = 0;
                        blanksOpp = 0;
                    }
                    if (cellCode == 0) {
                        countOwn = 0;
                        countOpp = 0;
                        countOwnGap += 1;
                        countOppGap += 1;
                        blanksOwn += 1;
                        blanksOpp += 1;
                    }
                    // Update maximum counts
                    maxCountOwn = (Math.max(countOwn, maxCountOwn));
                    maxCountOpp = (Math.max(countOpp, maxCountOpp));

                }
                if (maxCountOwn == 2) {
                    result[0] += 1;
                }
                if (maxCountOpp == 2) {
                    result[1] += 1;
                }
                if (maxCountOwn == 3) {
                    result[2] += 1;
                }
                if (maxCountOpp == 3) {
                    result[3] += 1;
                }
                if (maxCountOwn == 4) {
                    result[4] += 1;
                }
                if (maxCountOwn == 4) {
                    result[5] += 1;
                }
            }
        }

        // Second diagonal
        for (int i = 0; i < 10; i++) { // For each column
            for (int j = 0; j < 10; j++) { // For each element within the column
            }
        }

        // Vertical
        // Left Side
        for (int i = 0; i < 7; i++) { // For each column
            for (int j = 0; j < 10; j++) { // For each element within the column
            }
        }
        // Right side
        for (int i = 1; i < 7; i++) { // For each column
            for (int j = 0; j < 10; j++) { // For each element within the column
            }
        }
        return result;
    }

    private int[] getBoardFeaturesOneDir() {
        int[] result = new int[14];

        return result;
    }
}