package agent;

import engine.BoardState;

public abstract class Player {
    public boolean isWhitePlayer;
    public abstract int getMove(BoardState boardState);
    public abstract int getPieceToTake(int[] toTake);
}
