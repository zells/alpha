package org.zells.alpha.core.layouting;

import org.zells.alpha.core.Body;
import org.zells.alpha.rendering.Canvas;

import java.util.List;

public interface Layouter {

    public Iterable<Canvas> layout(List<Body> children, Canvas c);

}
