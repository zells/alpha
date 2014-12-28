package org.zells.alpha.dynamic;

import org.zells.alpha.core.Body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cell {
    private String name;
    private Cell stem;
    private Cell parent;
    private List<Cell> children = new ArrayList<Cell>();
    private Response response;

    public Body body = new Body();

    public Cell(String name, Cell stem, Cell parent) {
        this.name = name;
        this.stem = stem;
        this.parent = parent;
    }

    public Object receive(Object message) {
        return response().execute(message, this);
    }

    protected Response response() {
        if (response != null) {
            return response;
        } else if (stem != null) {
            return stem.response();
        } else {
            return new Response() {
                @Override
                public Object execute(Object message, Cell self) {
                    return null;
                }
            };
        }
    }

    public void respond(Response r) {
        response = r;
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

    public String fullName() {
        if (parent() != null) {
            return parent().fullName() + "." + name();
        }
        return name();
    }

    public Cell parent() {
        return parent;
    }

    public String name() {
        return name;
    }

    public void add(Cell child) {
        children.add(child);
    }

    public void remove(Cell child) {
        if (!children.contains(child)) {
            throw new RuntimeException("Cannot remove inherited cell [" + name + "]");
        }
        children.remove(child);
    }

    public Iterable<Cell> children() {
        Map<String, Cell> all = new HashMap<String, Cell>();
        if (stem != null) {
            for (Cell c : stem.children()) {
                all.put(c.name(), c);
            }
        }
        for (Cell c : children) {
            all.put(c.name(), c);
        }
        return all.values();
    }

    @Override
    public String toString() {
        return fullName() + "@" + hashCode();
    }
}
