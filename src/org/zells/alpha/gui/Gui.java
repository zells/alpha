package org.zells.alpha.gui;

import org.zells.alpha.dynamic.Cell;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    public Gui() throws HeadlessException {
        setTitle("Zells (alpha)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Cell root = new Cell("root", null, null);
        add(new Tab(root));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
