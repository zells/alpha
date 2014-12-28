package org.zells.alpha;

import org.zells.alpha.gui.Gui;

import javax.script.ScriptException;
import javax.swing.*;

public class Main {

    public static void main(String[] args) throws ScriptException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui();
            }
        });
    }
}
