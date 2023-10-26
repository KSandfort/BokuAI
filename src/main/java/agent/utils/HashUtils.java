package agent.utils;

import java.util.Random;

public class HashUtils {

    public static int nRandom = 200;
    public static int[] randomNumbers = new int[nRandom];

    public static void init() {
        Random rand = new Random();
        for (int i = 0; i < randomNumbers.length; i++) {
            randomNumbers[i] = rand.nextInt();
        }
    }

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

    public static int xor(int oldHash, boolean whitePlayer, int positionIndex) {
        return oldHash ^ getRandomNumber(whitePlayer, positionIndex);
    }

    public static int getRandomNumber(boolean whitePlayer, int pieceIndex) {
        int index = pieceIndex;
        if (!whitePlayer) {
            index+= 100;
        }
        return randomNumbers[index];
    }
}
