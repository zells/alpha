package org.zells.alpha.dynamic;

public class NativeCell extends Cell {
    public Object object;

    public NativeCell(String name, Object o, Cell parent) {
        super(name, null, parent);
        object = o;
    }
}
