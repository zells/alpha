package org.zells.alpha.gui;

import org.zells.alpha.dynamic.Cell;
import org.zells.alpha.rendering.View;

import javax.swing.*;
import java.awt.*;

public class Tab extends JPanel {
    public Tab(Cell cell) {
        setLayout(new BorderLayout());
        add(new Toolbar(cell), BorderLayout.NORTH);

        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                new JScrollPane(new Tree(cell)),
                new View(cell, new Dimension(600, 400))));
    }
}
