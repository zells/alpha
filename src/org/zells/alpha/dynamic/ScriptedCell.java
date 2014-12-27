package org.zells.alpha.dynamic;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptedCell extends BaseFunction implements Cell {
    private String response;
    private BaseCell base;

    public ScriptedCell() {
        base = new BaseCell();
    }

    public ScriptedCell(Cell stem) {
        base = new BaseCell(stem);
    }

    public void respond(String code) {
        response = code;
    }

    @Override
    public Object get(String name, Scriptable start) {
        return get(name);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return receive(args[0]);
    }

    @Override
    public Object receive(Object message) {
        return receive(message, this);
    }

    @Override
    public Object receive(Object message, Cell self) {
        if (response == null) {
            return base.receive(message, self);
        }

        Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects();
        ScriptableObject.putProperty(scope, "message", message);
        ScriptableObject.putProperty(scope, "self", self);
        return cx.evaluateString(scope, response, "<cmd>", 1, null);
    }

    @Override
    public Object get(String name) {
        return base.get(name);
    }

    @Override
    public void add(String name, Object child) {
        base.add(name, child);
    }

    @Override
    public void remove(String name) {
        base.remove(name);
    }
}
