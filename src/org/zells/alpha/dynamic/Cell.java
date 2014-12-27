package org.zells.alpha.dynamic;

public interface Cell {

    public Object receive(Object message);

    public Object receive(Object message, Cell self);

    public Object get(String name);

    public void add(String name, Object child);

    public void remove(String name);
}
