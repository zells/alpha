package org.zells.alpha.dynamic;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptedCell extends BaseFunction implements Cell {
    private BaseCell base;
    private String response;

    public ScriptedCell(String name, Cell stem, Cell parent) {
        base = new BaseCell(name, stem, parent);
    }

    @Override
    public Object get(String name, Scriptable start) {
        return base.child(name);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return receive(args[0], this);
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
        ScriptableObject.putProperty(scope, "parent", self.parent());

        return cx.evaluateString(scope, response, "<cmd>", 1, null);
    }

    @Override
    public Cell child(String name) {
        return base.child(name);
    }

    @Override
    public Cell parent() {
        return base.parent();
    }

    @Override
    public String name() {
        return base.name();
    }

    @Override
    public String fullName() {
        return base.fullName();
    }

    @Override
    public void add(Cell child) {
        base.add(child);
    }

    @Override
    public Iterable<Cell> children() {
        return base.children();
    }

    @Override
    public void remove(String name) {
        base.remove(name);
    }

    @Override
    public Cell stem() {
        return base.stem();
    }

    public void respond(String code) {
        response = code;
    }

}
