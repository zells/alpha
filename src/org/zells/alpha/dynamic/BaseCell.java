package org.zells.alpha.dynamic;

import java.util.ArrayList;
import java.util.List;

public class BaseCell implements Cell {
    private Cell stem;
    private Cell parent;
    private List<Cell> children = new ArrayList<Cell>();
    private String name;

    public BaseCell(String name, Cell stem, Cell parent) {
        this.name = name;
        this.stem = stem;
        this.parent = parent;
    }

    public Object receive(Object message, Cell self) {
        if (stem != null) {
            return stem.receive(message, self);
        }
        return null;
    }

    public Cell stem() {
        return stem;
    }

    public Cell child(String name) {
        for (Cell child : children) {
            if (child.name().equals(name)) {
                return child;
            }
        }

        if (stem != null) {
            return new InheritedCell(stem.child(name), this);
        }
        throw new RuntimeException("Could not find [" + name + "] in [" + fullName() + "]");
    }

    @Override
    public String fullName() {
        if (parent() != null) {
            return parent().fullName() + "." + name();
        }
        return name();
    }

    @Override
    public Cell parent() {
        return parent;
    }

    @Override
    public String name() {
        return name;
    }

    public void add(Cell child) {
        children.add(child);
    }

    public void remove(String name) {
        Cell remove = null;
        for (Cell child : children) {
            if (child.name().equals(name)) {
                remove = child;
                break;
            }
        }
        if (remove == null) {
            throw new RuntimeException("Cannot remove inherited cell [" + name + "]");
        }
        children.remove(remove);
    }

    public Iterable<Cell> children() {
        return children;
    }

    @Override
    public String toString() {
        return fullName() + "@" + hashCode();
    }
}
