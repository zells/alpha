package org.zells.alpha.core.eventing;

abstract public class SpecificEventListener<T> extends EventListener {
    private Class clazz;

    public SpecificEventListener(Class c) {
        super();
        clazz = c;
    }

    @Override
    public boolean handles(Object event) {
        return event.getClass() == clazz;
    }

    public void handle(Object event) {
        handleSpecific((T) event);
    }

    abstract protected void handleSpecific(T event);
}
