package org.zells.alpha.dynamic;

import java.util.HashMap;
import java.util.Map;

public class BaseCell implements Cell {
    private Cell stem;
    private Map<String, Object> children = new HashMap<String, Object>();

    public BaseCell() {
    }

    public BaseCell(Cell stem) {
        this.stem = stem;
    }

    public Object receive(Object message) {
        return receive(message, this);
    }

    public Object receive(Object message, Cell self) {
        if (stem != null) {
            return stem.receive(message, self);
        }
        return null;
    }

    public void add(String name, Object child) {
        children.put(name, child);
    }

    public Object get(String name) {
        if (!children.containsKey(name)) {
            if (stem != null) {
                return stem.get(name);
            }
            throw new RuntimeException("Could not find [" + name + "]");
        }
        return children.get(name);
    }

    public void remove(String name) {
        children.remove(name);
    }

    public Map children() {
        return children;
    }
}
