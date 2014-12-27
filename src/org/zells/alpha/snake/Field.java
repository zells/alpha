package org.zells.alpha.snake;

import org.zells.alpha.core.Body;
import org.zells.alpha.core.Cell;
import org.zells.alpha.rendering.Canvas;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

public class Field extends Cell {
    private Dimension2D size = new Dimension(300, 200);

    public Field() {
        body = new Body() {
            @Override
            protected void paintBody(Canvas c) {
                c.draw(new Rectangle2D.Double(
                        -size.getWidth() / 2 + 2,
                        -size.getHeight() / 2 + 2,
                        size.getWidth() - 4,
                        size.getHeight() - 4
                ), new BasicStroke(4));
            }
        };

        Snake snake = new Snake();

        body.add(new MouseController(snake.head));
        body.add(snake);
    }
}
