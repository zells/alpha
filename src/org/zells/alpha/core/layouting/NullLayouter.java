package org.zells.alpha.core.layouting;

import org.zells.alpha.core.Body;
import org.zells.alpha.rendering.Canvas;

import java.util.ArrayList;
import java.util.List;

public class NullLayouter implements Layouter {
    @Override
    public Iterable<Canvas> layout(List<Body> children, Canvas c) {
        List<Canvas> laidOut = new ArrayList<Canvas>();
        for (Body child : children) {
            laidOut.add(c.child(child, c.size()));
        }
        return laidOut;
    }
}
