package org.zells.alpha.core.eventing;

public abstract class EventListener {
    public boolean handles(Object event) {
        return true;
    }

    public abstract void handle(Object event);
}
