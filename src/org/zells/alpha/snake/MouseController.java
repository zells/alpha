package org.zells.alpha.snake;

import org.zells.alpha.core.Cell;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController extends Cell {
    public MouseController(final Head head) {
        super();

        body.mouse = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                head.turnTo(e.getPoint());
            }
        };
    }
}
