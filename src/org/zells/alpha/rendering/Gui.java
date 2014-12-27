package org.zells.alpha.rendering;

import org.zells.alpha.core.Cell;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    public Gui(Cell root) throws HeadlessException {
        setTitle("Zells");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(new View(root, new Dimension(400, 400)));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
