package org.zells.alpha;

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
    }
}
