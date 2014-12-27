package org.zells.alpha.snake;

public class Head extends Piece {
    @Override
    public int openingDegree() {
        return 40;
    }

    @Override
    public double radius() {
        return 10;
    }
}
