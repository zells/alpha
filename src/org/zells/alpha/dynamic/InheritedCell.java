package org.zells.alpha.dynamic;

public class InheritedCell implements Cell {
    private Cell cell;
    private Cell parent;

    public InheritedCell(Cell cell, Cell parent) {
        this.cell = cell;
        this.parent = parent;
    }

    @Override
    public Object receive(Object message, Cell self) {
        return cell.receive(message, self);
    }

    @Override
    public void add(Cell child) {
        BaseCell adopted = new BaseCell(name(), cell, parent);
        parent.add(adopted);
        adopted.add(child);
    }

    @Override
    public Cell child(String name) {
        return cell.child(name);
    }

    @Override
    public Cell parent() {
        return parent;
    }

    @Override
    public String name() {
        return cell.name();
    }

    @Override
    public String fullName() {
        if (parent() != null) {
            return parent().fullName() + "." + name();
        }
        return name();
    }

    @Override
    public Iterable<Cell> children() {
        return cell.children();
    }

    @Override
    public void remove(String name) {
        cell.remove(name);
    }

    @Override
    public Cell stem() {
        return cell.stem();
    }
}
