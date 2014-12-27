package org.zells.alpha.rendering;

import org.zells.alpha.core.Body;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Canvas {
    private Body body;
    private Graphics2D graphics;
    private Dimension2D size;
    private AffineTransform transformation = new AffineTransform();
    private ArrayList<Canvas> children = new ArrayList<Canvas>();

    public Canvas(Body b, Dimension2D s, Graphics2D g) {
        body = b;
        graphics = g;
        size = s;
    }

    public Canvas draw(Shape s, Stroke r) {
        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(r);
        draw(s);
        graphics.setStroke(oldStroke);
        return this;
    }

    public Canvas draw(Shape s) {
        graphics.draw(s);
        return this;
    }

    public Canvas child(Body child, Dimension2D size) {
        Canvas canvas = new Canvas(child, size, graphics);
        children.add(canvas);
        return canvas;
    }

    public Dimension2D size() {
        return size;
    }

    public AffineTransform transformation() {
        return transformation;
    }

    public void render() {
        AffineTransform before = graphics.getTransform();
        graphics.transform(transformation);
        body.paint(this);
        graphics.setTransform(before);
    }

    public boolean contains(Point2D point) {
        try {
            Point2D transformed = transformation.inverseTransform(point, null);
            return Math.abs(transformed.getX()) < size.getWidth() / 2
                    && Math.abs(transformed.getY()) < size.getHeight() / 2;
        } catch (NoninvertibleTransformException ignored) {
            return false;
        }
    }

    public void input(Input i) {
        i.transform(transformation);
        for (Canvas c : children) {
            if (c.contains(i.event().getPoint())) {
                c.input(i);
                return;
            }
        }
        i.execute(body.mouse);
    }
}
