package org.zells.alpha.dynamic;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptedResponse implements Response {
    private String code;

    public ScriptedResponse(String code) {
        this.code = code;
    }

    @Override
    public Object execute(Object message, Cell self) {
        Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects();

        ScriptableObject.putProperty(scope, "message", message);
        ScriptableObject.putProperty(scope, "self", new ScriptedCell(self));
        ScriptableObject.putProperty(scope, "parent", new ScriptedCell(self.parent()));

        return cx.evaluateString(scope, code, "<cmd>", 1, null);
    }
}
