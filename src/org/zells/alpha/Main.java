package org.zells.alpha;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.zells.alpha.rendering.Gui;
import org.zells.alpha.snake.Game;
import org.zells.alpha.test.Test;

import javax.script.ScriptException;
import javax.swing.*;

public class Main {

    public static void main(String[] args) throws ScriptException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game game = new Game();
                new Gui(game);

                new Gui(Test.build());
            }
        });
        runScript();
    }

    private static void runScript() {
        Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects();
        ScriptableObject.putProperty(scope, "test", new DynamicCell());
        cx.evaluateString(scope, "java.lang.System.out.println(test(test.You))", "<cmd>", 1, null);
    }

    public static class DynamicCell extends BaseFunction {

        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            return receive(args[0]);
        }

        private Object receive(Object message) {
            return "Received object " + message;
        }

        @Override
        public Object get(String name, Scriptable start) {
            return "Hello " + name;
        }
    }
}
