package org.zells.alpha.core.eventing;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher {
    private List<EventListener> listeners = new CopyOnWriteArrayList<EventListener>();

    public void fire(Object event) {
        for (EventListener l : listeners) {
            if (l.handles(event)) {
                l.handle(event);
            }
        }
    }

    public void add(EventListener listener) {
        listeners.add(listener);
    }
}
