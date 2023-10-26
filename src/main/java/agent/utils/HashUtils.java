package agent.utils;

import engine.BoardState;

import java.util.Random;

public class HashUtils {

    public static int nRandom = 20000;
    public static int[] randomNumbers = new int[nRandom];

    public static void init() {
        Random rand = new Random();
        for (int i = 0; i < randomNumbers.length; i++) {
            randomNumbers[i] = rand.nextInt();
        }
    }

    public static int getHash(int oldHash, boolean whitePlayer, int placedPieceCoordinate, int takenPieceCoordinate) {
        return oldHash ^ getRandomNumber(whitePlayer, placedPieceCoordinate, takenPieceCoordinate);
    }

    public static int getRandomNumber(boolean whitePlayer, int placedPieceCoordinate, int takenPieceCoordinate) {
        int index = (placedPieceCoordinate * 100) + takenPieceCoordinate;
        if (!whitePlayer) {
            index+= 10000;
        }
        return randomNumbers[index];
    }
}
