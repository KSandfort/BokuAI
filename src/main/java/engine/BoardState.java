package engine;

import agent.utils.HashUtils;
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

    private int[] board;
    public boolean whiteToMove;
    private int blockedCoordinate = -1;
    int boardHash;

    public BoardState() {
        this.board = new int[100];
        this.whiteToMove = true;
        this.boardHash = 0;
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
        this.boardHash = HashUtils.xor(oldBoard.boardHash, whiteToMove, positionIndex);
    }

    public BoardState(BoardState oldBoard, boolean whiteMove) {
        this.board = oldBoard.getBoard().clone();
        this.whiteToMove = whiteMove;
        this.boardHash = oldBoard.boardHash;
    }

    /**
     * Creates a new board where a piece gets taken.
     * This function is used purely for generating board states in
     * the agent's decision process and not in the actual game.
     * @param positionIndex position index of the piece to take
     * @return new board state after the capture of a piece
     */
    public BoardState agentPieceCapture(int positionIndex) {
        BoardState newBoard = new BoardState(this, this.whiteToMove);
        newBoard.getBoard()[positionIndex] = 0;
        newBoard.setBlockedCoordinate(positionIndex);
        newBoard.boardHash = HashUtils.xor(this.boardHash, !this.whiteToMove, positionIndex);
        return newBoard;
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
            if (this.board[i] == 0 && isCoordinateValid(coord1dTo2d(i))) {
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

    /**
     * Checks the board in one direction if pieces can be taken.
     * The opposite direction in the same dimension is NOT considered.
     * @param step1 coordinate of the first step in the desired direction
     * @param step2 coordinate of the second step in the desired direction
     * @param step3 coordinate of the third step in the desired direction
     * @param playerCode 1 for white, -1 for black
     * @return array coordinate indices of all potential pieces that can be captured
     */
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

    /**
     * Returns all indices (1d) of positions where pieces can be taken in the next move.
     * This function is also intended to return the total number of pieces that could be taken in the next move
     * as part of the board evaluation.
     * @param playerCode 1 for white, -1 for black
     * @return array of all positions (1d)
     */
    public int[] possibleCapturesNextMove(int playerCode) {

        // Initialise empty list to return later
        ArrayList<Integer> possibleCaptureMoves = new ArrayList<>();
        // Translate player code into boolean
        boolean whitePlayer = playerCode == 1;

        // Iterate along diagonal diagonal
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int[] coord2d = new int[]{i, j};
                if (isCoordinateValid(coord2d) && this.board[coord2dTo1d(coord2d)] == 0) {
                    int[] own = getPiecesToTake(coord2dTo1d(coord2d), whitePlayer);
                    for (int item : own) {
                        if (!possibleCaptureMoves.contains(item)) {
                            possibleCaptureMoves.add(item);
                        }
                    }
                }
            }
        }
        return possibleCaptureMoves.stream().mapToInt(i -> i).toArray();
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

    public boolean isArraySubstring(int[] a, int[] row) {
        String aStr = "";
        String rowStr = "";
        for (int i : a) {
            aStr += i;
        }
        for (int i : row) {
            rowStr += i;
        }
        if (rowStr.contains(aStr)) {
            return true;
        }
        else {
            return false;
        }
    }

    public int[] getBoardFeatures() {
        int[] result = new int[14];
        // 0 = Own player rows of 2
        // 1 = Opp player rows of 2
        // 2 = Own player rows of 3
        // 3 = Opp player rows of 3
        // 4 = Own player rows of 4
        // 5 = Opp player rows of 4
        // 6 = Own player possible captures
        // 7 = Opp player possible captures

        // 8 = White rows of 3 both open
        // 9 = White rows of 4 one open
        // 10 = White rows of 4 both open

        // 11 = White rows of 3 both open
        // 12 = White rows of 4 one open
        // 13 = White rows of 4 both open

        int countWhite;
        int countBlack;
        int maxCountWhite;
        int maxCountBlack;

        int[] w3bothOpen = new int[]{0, 1, 1, 1, 0};
        int[] w4openStart = new int[]{0, 1, 1, 1, 1};
        int[] w4openEnd = new int[]{1, 1, 1, 1, 0};
        int[] w4bothOpen = new int[]{0, 1, 1, 1, 0};

        int[] b3bothOpen = new int[]{0, -1, -1, -1, 0};
        int[] b4openStart = new int[]{0, -1, -1, -1, -1};
        int[] b4openEnd = new int[]{-1, -1, -1, -1, 0};
        int[] b4bothOpen = new int[]{0, -1, -1, -1, 0};

        // First diagonal
        for (int i = 0; i < 10; i++) { // For each column
            countWhite = 0;
            countBlack = 0;
            maxCountWhite = 0;
            maxCountBlack = 0;

            int[] rowSequence = new int[10];

            for (int j = 0; j < 10; j++) { // For each element within the column
                if (isCoordinateValid(new int[]{i, j})) { // Check validity of cell
                    int cellCode = this.board[coord2dTo1d(new int[]{i, j})];
                    rowSequence[j] = cellCode;
                    if (cellCode == 1) {
                        countWhite += 1;
                        countBlack = 0;
                    }
                    if (cellCode == -1) {
                        countWhite = 0;
                        countBlack += 1;
                    }
                    if (cellCode == 0) {
                        countWhite = 0;
                        countBlack = 0;
                    }

                }

                // Update maximum counts
                maxCountWhite = (Math.max(countWhite, maxCountWhite));
                maxCountBlack = (Math.max(countBlack, maxCountBlack));

            }
            if (maxCountWhite == 2) {
                result[0] += 1;
            }
            if (maxCountBlack == 2) {
                result[1] += 1;
            }
            if (maxCountWhite == 3) {
                result[2] += 1;
            }
            if (maxCountBlack == 3) {
                result[3] += 1;
            }
            if (maxCountWhite == 4) {
                result[4] += 1;
            }
            if (maxCountBlack == 4) {
                result[5] += 1;
            }

            if (isArraySubstring(w3bothOpen, rowSequence)) {
                result[8] += 1;
            }
            if (isArraySubstring(w4openStart, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4openEnd, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4bothOpen, rowSequence)) {
                result[10] += 1;
            }

            if (isArraySubstring(b3bothOpen, rowSequence)) {
                result[11] += 1;
            }
            if (isArraySubstring(b4openStart, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4openEnd, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4bothOpen, rowSequence)) {
                result[13] += 1;
            }
        }

        // Second diagonal
        for (int i = 0; i < 10; i++) { // For each column
            countWhite = 0;
            countBlack = 0;

            maxCountWhite = 0;
            maxCountBlack = 0;

            int[] rowSequence = new int[10];

            for (int j = 0; j < 10; j++) { // For each element within the column
                if (isCoordinateValid(new int[]{j, i})) { // Check validity of cell
                    int cellCode = this.board[coord2dTo1d(new int[]{j, i})];
                    rowSequence[j] = cellCode;
                    if (cellCode == 1) {
                        countWhite += 1;
                        countBlack = 0;
                    }
                    if (cellCode == -1) {
                        countWhite = 0;
                        countBlack += 1;
                    }
                    if (cellCode == 0) {
                        countWhite = 0;
                        countBlack = 0;
                    }

                }

                // Update maximum counts
                maxCountWhite = (Math.max(countWhite, maxCountWhite));
                maxCountBlack = (Math.max(countBlack, maxCountBlack));

            }
            if (maxCountWhite == 2) {
                result[0] += 1;
            }
            if (maxCountBlack == 2) {
                result[1] += 1;
            }
            if (maxCountWhite == 3) {
                result[2] += 1;
            }
            if (maxCountBlack == 3) {
                result[3] += 1;
            }
            if (maxCountWhite == 4) {
                result[4] += 1;
            }
            if (maxCountBlack == 4) {
                result[5] += 1;
            }

            if (isArraySubstring(w3bothOpen, rowSequence)) {
                result[8] += 1;
            }
            if (isArraySubstring(w4openStart, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4openEnd, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4bothOpen, rowSequence)) {
                result[10] += 1;
            }

            if (isArraySubstring(b3bothOpen, rowSequence)) {
                result[11] += 1;
            }
            if (isArraySubstring(b4openStart, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4openEnd, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4bothOpen, rowSequence)) {
                result[13] += 1;
            }
        }

        // Vertical
        // Left Side
        for (int i = 0; i < 7; i++) { // For each column
            countWhite = 0;
            countBlack = 0;

            maxCountWhite = 0;
            maxCountBlack = 0;

            int[] rowSequence = new int[10];

            for (int j = 0; j < 10; j++) { // For each element within the column
                if (isCoordinateValid(new int[]{i + j, j})) { // Check validity of cell
                    int cellCode = this.board[coord2dTo1d(new int[]{i + j, j})];
                    rowSequence[j] = cellCode;
                    if (cellCode == 1) {
                        countWhite += 1;
                        countBlack = 0;
                    }
                    if (cellCode == -1) {
                        countWhite = 0;
                        countBlack += 1;
                    }
                    if (cellCode == 0) {
                        countWhite = 0;
                        countBlack = 0;
                    }

                }

                // Update maximum counts
                maxCountWhite = (Math.max(countWhite, maxCountWhite));
                maxCountBlack = (Math.max(countBlack, maxCountBlack));

            }
            if (maxCountWhite == 2) {
                result[0] += 1;
            }
            if (maxCountBlack == 2) {
                result[1] += 1;
            }
            if (maxCountWhite == 3) {
                result[2] += 1;
            }
            if (maxCountBlack == 3) {
                result[3] += 1;
            }
            if (maxCountWhite == 4) {
                result[4] += 1;
            }
            if (maxCountBlack == 4) {
                result[5] += 1;
            }

            if (isArraySubstring(w3bothOpen, rowSequence)) {
                result[8] += 1;
            }
            if (isArraySubstring(w4openStart, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4openEnd, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4bothOpen, rowSequence)) {
                result[10] += 1;
            }

            if (isArraySubstring(b3bothOpen, rowSequence)) {
                result[11] += 1;
            }
            if (isArraySubstring(b4openStart, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4openEnd, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4bothOpen, rowSequence)) {
                result[13] += 1;
            }
        }

        // Right side
        for (int i = 1; i < 7; i++) { // For each column
            countWhite = 0;
            countBlack = 0;
            maxCountWhite = 0;
            maxCountBlack = 0;

            int[] rowSequence = new int[10];

            for (int j = 0; j < 10; j++) { // For each element within the column
                if (isCoordinateValid(new int[]{j, i + j})) { // Check validity of cell
                    int cellCode = this.board[coord2dTo1d(new int[]{j, i + j})];
                    rowSequence[j] = cellCode;
                    if (cellCode == 1) {
                        countWhite += 1;
                        countBlack = 0;
                    }
                    if (cellCode == -1) {
                        countWhite = 0;
                        countBlack += 1;
                    }
                    if (cellCode == 0) {
                        countWhite = 0;
                        countBlack = 0;
                    }

                }

                // Update maximum counts
                maxCountWhite = (Math.max(countWhite, maxCountWhite));
                maxCountBlack = (Math.max(countBlack, maxCountBlack));
            }
            if (maxCountWhite == 2) {
                result[0] += 1;
            }
            if (maxCountBlack == 2) {
                result[1] += 1;
            }
            if (maxCountWhite == 3) {
                result[2] += 1;
            }
            if (maxCountBlack == 3) {
                result[3] += 1;
            }
            if (maxCountWhite == 4) {
                result[4] += 1;
            }
            if (maxCountBlack == 4) {
                result[5] += 1;
            }

            if (isArraySubstring(w3bothOpen, rowSequence)) {
                result[8] += 1;
            }
            if (isArraySubstring(w4openStart, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4openEnd, rowSequence)) {
                result[9] += 1;
            }
            if (isArraySubstring(w4bothOpen, rowSequence)) {
                result[10] += 1;
            }

            if (isArraySubstring(b3bothOpen, rowSequence)) {
                result[11] += 1;
            }
            if (isArraySubstring(b4openStart, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4openEnd, rowSequence)) {
                result[12] += 1;
            }
            if (isArraySubstring(b4bothOpen, rowSequence)) {
                result[13] += 1;
            }
        }

        // Add scores for pieces to take
        result[6] = this.possibleCapturesNextMove(1).length;
        result[7] = this.possibleCapturesNextMove(-1).length;

        return result;
    }
}