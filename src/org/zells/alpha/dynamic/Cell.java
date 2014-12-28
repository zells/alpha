package org.zells.alpha.dynamic;

public interface Cell {

    public Cell stem();

    public Cell parent();

    public String name();

    public String fullName();

    public Object receive(Object message, Cell self);

    public void add(Cell child);

    public Cell child(String name);

    public Iterable<Cell> children();

    public void remove(String name);
}
