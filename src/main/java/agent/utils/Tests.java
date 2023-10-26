package agent.utils;

import engine.BoardState;

import java.util.Arrays;

public class Tests {

    public static void main(String[] args) {
        BoardState bs = new BoardState();
        System.out.println(bs.isArraySubstring(new int[]{0, 1, 1, 1, 0}, new int[]{0, 1, 0, 1, 1, 1, 0, 0, 1}));
        System.out.println(bs.isArraySubstring(new int[]{0, 1, 1, 1, 0}, new int[]{0, 1, 0, 1, 1, 0, 0, 1}));
    }
}
