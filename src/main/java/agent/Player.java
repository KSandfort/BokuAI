package agent;

import engine.BoardState;

public abstract class Player {
    public abstract int getMove(BoardState boardState);
}
