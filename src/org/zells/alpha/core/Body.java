package org.zells.alpha.core;

import org.zells.alpha.core.eventing.EventDispatcher;
import org.zells.alpha.core.eventing.SpecificEventListener;
import org.zells.alpha.core.layouting.Layouter;
import org.zells.alpha.core.layouting.NullLayouter;
import org.zells.alpha.rendering.Canvas;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

public class Body {

    public Layouter layouter = new NullLayouter();
    public MouseAdapter mouse = new MouseAdapter() {
    };
    private EventDispatcher events = new EventDispatcher();
    private List<Body> children = new ArrayList<Body>();

    public void paint(Canvas c) {
        paintBody(c);
        paintChildren(c);
    }

    protected void paintBody(Canvas c) {
    }

    protected void paintChildren(Canvas c) {
        for (Canvas child : layouter.layout(children, c)) {
            child.render();
        }
    }

    public EventDispatcher events() {
        return events;
    }

    public void add(Cell child) {
        children.add(child.body);
        child.body.events.add(new SpecificEventListener<Body.RepaintEvent>(Body.RepaintEvent.class) {
            @Override
            protected void handleSpecific(RepaintEvent event) {
                events.fire(event);
            }
        });
    }

    public static class RepaintEvent {
    }
}
