package org.zells.alpha.dynamic;

public class InheritedCell extends Cell {
    private Cell cell;

    public InheritedCell(Cell cell, Cell parent) {
        super(cell.name(), cell.stem(), parent);
        this.cell = cell;
    }

    @Override
    public Cell child(String name) {
        return new InheritedCell(cell.child(name), this);
    }

    @Override
    public void add(Cell child) {
        Cell adopted = new Cell(name(), cell, parent());
        parent().add(adopted);
        adopted.add(child);
    }

}
