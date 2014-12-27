package org.zells.alpha.snake;

import org.zells.alpha.core.Body;
import org.zells.alpha.core.Cell;

import java.util.Timer;
import java.util.TimerTask;

public class Snake extends Cell {

    public final Head head = new Head();
    private Piece followNext = head;
    private double speed = 1;

    public Snake() {
        body.add(head);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                head.move(speed);
                body.events().fire(new Body.RepaintEvent());
            }
        }, 0, 40);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                grow();
            }
        }, 4000, 2000);
    }

    public void grow() {
        Segment segment = new Segment(followNext);
        followNext = segment;
        body.add(segment);
    }
}
