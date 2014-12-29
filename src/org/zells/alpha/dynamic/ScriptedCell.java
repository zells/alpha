package org.zells.alpha.dynamic;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ScriptedCell extends BaseFunction {
    public Cell cell;

    public ScriptedCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public Object get(String name, Scriptable start) {
        Cell child = cell.child(name);
        if (child instanceof NativeCell) {
            return ((NativeCell) child).object;
        }
        return new ScriptedCell(child);
    }

    @Override
    public void put(String name, Scriptable start, Object value) {
        if (value instanceof ScriptedCell) {
            cell.add(new Cell(name, ((ScriptedCell) value).cell, cell));
        } else {
            cell.add(new NativeCell(name, value, cell));
        }
        super.put(name, start, value);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return call(args[0]);
    }

    public Object call(Object message) {
        return cell.receive(message);
    }
}
