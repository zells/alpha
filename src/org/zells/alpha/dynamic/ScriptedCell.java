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
        return new ScriptedCell(cell.child(name));
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return call(args[0]);
    }

    public Object call(Object message) {
        return cell.receive(message);
    }

}
