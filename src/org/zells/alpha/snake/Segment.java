package org.zells.alpha.snake;

import org.zells.alpha.core.eventing.SpecificEventListener;

public class Segment extends Piece {

    public Segment(final Piece follow) {
        moveTo(follow.position());
        follow.body.events().add(new SpecificEventListener<Piece.Moved>(Piece.Moved.class) {
            @Override
            protected void handleSpecific(Moved event) {
                double distance = position().distance(follow.position()) - radius() - follow.radius();
                if (distance > 0) {
                    turnTo(follow.position());
                    move(distance);
                }
            }
        });
    }

    @Override
    public double radius() {
        return 8;
    }
}
