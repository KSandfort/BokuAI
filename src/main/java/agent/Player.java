package agent;

import engine.BoardState;
import engine.GameController;

public abstract class Player {
    public boolean isWhitePlayer;
    public GameController gameController;
    public abstract int getMove(BoardState boardState);
    public abstract int getPieceToTake(int[] toTake);
}
