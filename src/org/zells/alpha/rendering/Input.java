package org.zells.alpha.rendering;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

abstract public class Input {
    private MouseEvent event;

    public Input(MouseEvent e) {
        event = e;
    }

    public MouseEvent event() {
        return event;
    }

    abstract public void execute(MouseAdapter a);

    public void transform(AffineTransform transformation) {
        try {
            Point2D transformed = transformation.inverseTransform(event.getPoint(), null);
            event.translatePoint(
                    (int) (transformed.getX() - event.getPoint().getX()),
                    (int) (transformed.getY() - event.getPoint().getY()));
        } catch (NoninvertibleTransformException ignored) {
        }
    }
}
