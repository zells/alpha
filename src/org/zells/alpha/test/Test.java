package org.zells.alpha.test;

import org.zells.alpha.core.Body;
import org.zells.alpha.core.eventing.SpecificEventListener;
import org.zells.alpha.core.layouting.Layouter;
import org.zells.alpha.rendering.Canvas;
import org.zells.alpha.core.Cell;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class Test extends Cell {
    private String name;
    private ArrayList<Point2D> points = new ArrayList<Point2D>();

    public Test(String n) {
        this.name = n;
        body = new TestBody();
    }

    public static Test build() {
        Test a = new Test("A");

        a.body.add(new Test("B"));
        a.body.add(new Test("C"));

        return a;
    }

    @Override
    public String toString() {
        return name;
    }

    private class DraggedEvent {
        public Test cell;
        public MouseEvent event;

        public DraggedEvent(Test c, MouseEvent e) {
            cell = c;
            event = e;
        }
    }

    public class TestBody extends Body {
        private Map<Body, Point2D> positions = new HashMap<Body, Point2D>();

        public TestBody() {
            mouse = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    points.add(e.getPoint());
                    body.events().fire(new Body.RepaintEvent());
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    body.events().fire(new DraggedEvent(Test.this, e));
                }
            };

            layouter = new Layouter() {
                @Override
                public Iterable<Canvas> layout(List<Body> children, Canvas c) {
                    List<Canvas> laidOut = new ArrayList<Canvas>();
                    for (Body child : children) {
                        Canvas canvas = c.child(child, new Dimension(100, 100));
                        Point2D p = positions.get(child);
                        canvas.transformation().translate(p.getX(), p.getY());
                        laidOut.add(canvas);
                    }
                    return laidOut;
                }
            };
        }

        public void add(Cell child) {
            positions.put(child.body, new Point2D.Double(0, 0));
            child.body.events().add(new SpecificEventListener<DraggedEvent>(DraggedEvent.class) {
                @Override
                public void handleSpecific(DraggedEvent dragged) {
                    Point2D point = dragged.event.getPoint();
                    Point2D position = positions.get(dragged.cell.body);
                    position.setLocation(position.getX() + point.getX(), position.getY() + point.getY());

                    events().fire(new RepaintEvent());
                }
            });
            super.add(child);
        }

        @Override
        public void paintBody(Canvas c) {
            double w = c.size().getWidth() / 2;
            double h = c.size().getHeight() / 2;
            c.draw(new Line2D.Double(-w, h, w, -h));
            c.draw(new Line2D.Double(w, h, -w, -h));

            for (Point2D p : points) {
                c.draw(new Arc2D.Double(p.getX() - 1, p.getY() - 1, 2, 2, 0, 360, Arc2D.OPEN));
            }
        }
    }
}
