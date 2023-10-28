package agent.utils;

import java.util.Random;

/**
 * Provides utility functions for the hash map that is used for the transposition table in the game.
 */
public class HashUtils {
    public static int nRandom = 200; // Total random numbers
    public static int[] randomNumbers = new int[nRandom]; // Array to store all random numbers

    /**
     * Initialize the array containing nRandom numbers.
     */
    public static void init() {
        Random rand = new Random();
        for (int i = 0; i < randomNumbers.length; i++) {
            randomNumbers[i] = rand.nextInt();
        }
    }

    /**
     * Gets a hash value based on the board state of a move node.
     * @param moveNode target node
     * @return hash value
     */
    public static int getHash(MoveNode moveNode) {
        int hashSum = 0;
        int[] board = moveNode.getBoardState().getBoard();
        for (int i = 0; i < board.length; i++) {
            if(board[i] == 1) {
                hashSum = hashSum ^ getRandomNumber(true, i);
            }
            else if(board[i] == -1) {
                hashSum = hashSum ^ getRandomNumber(false, i);
            }
        }
        return hashSum;
    }

    /**
     * Performs an XOR operation between an old hash value and the new piece that is being placed.
     * @param oldHash old hash value
     * @param whitePlayer true if white player is the target player
     * @param positionIndex position index of the piece being placed
     * @return new hash value
     */
    public static int xor(int oldHash, boolean whitePlayer, int positionIndex) {
        return oldHash ^ getRandomNumber(whitePlayer, positionIndex);
    }

    /***
     * Gets a random number based off the pre-defined array storing all available random numbers.
     * @param whitePlayer true if white player is the target player
     * @param positionIndex position index of the piece being placed
     * @return random number
     */
    public static int getRandomNumber(boolean whitePlayer, int positionIndex) {
        int index = positionIndex;
        if (!whitePlayer) {
            index+= 100;
        }
        return randomNumbers[index];
    }
}
