package org.zells.alpha.snake;

import org.zells.alpha.core.Body;
import org.zells.alpha.core.Cell;
import org.zells.alpha.rendering.Canvas;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

abstract public class Piece extends Cell {
    private Point2D position = new Point2D.Double(0, 0);
    private double orientation = 0;

    abstract public double radius();

    public Piece() {
        body = new Body() {
            @Override
            protected void paintBody(Canvas c) {
                double r = radius();
                int o = openingDegree();

                double degree = orientation * 180 / Math.PI;
                c.draw(new Arc2D.Double(
                        position.getX() - r, position.getY() - r,
                        r * 2, r * 2,
                        o / 2 - degree, 360 - o,
                        o == 0 ? Arc2D.OPEN : Arc2D.PIE));
            }
        };
    }

    public int openingDegree() {
        return 0;
    }

    public void turnTo(Point2D p) {
        orientation = Math.atan2(
                p.getY() - position.getY(),
                p.getX() - position.getX()
        );
        body.events().fire(new Turned());
    }

    public void move(double d) {
        moveTo(new Point2D.Double(
                position.getX() + d * Math.cos(orientation),
                position.getY() + d * Math.sin(orientation)
        ));
    }

    public void moveTo(Point2D p) {
        position = p;
        body.events().fire(new Moved());
    }

    public Point2D position() {
        return position;
    }

    public static class Moved {
    }

    public static class Turned {
    }
}
