package org.zells.alpha.gui;

import org.zells.alpha.dynamic.Cell;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Tree extends JPanel {
    private CellNode root;
    private DefaultTreeModel model;
    private JTree tree;

    public Tree(Cell cell) {
        setLayout(new BorderLayout());

        Listener listener = new Listener();
        root = new CellNode(cell);
        model = new DefaultTreeModel(root);
        model.addTreeModelListener(listener);

        tree = new JTree(model);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        add(tree);
        JPanel buttons = new JPanel();
        add(buttons, BorderLayout.NORTH);
        buttons.add(new JButton(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCell();
            }
        }));
        buttons.add(new JButton(new AbstractAction("Remove") {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeCell();
            }
        }));
    }

    private void addCell() {
        CellNode parent;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parent = root;
        } else {
            parent = (CellNode) parentPath.getLastPathComponent();
        }

        Cell child = new Cell("New Cell", null, parent.cell);
        parent.cell.add(child);
        CellNode childNode = new CellNode(child);

        model.insertNodeInto(childNode, parent, parent.getChildCount());
        tree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }

    private void removeCell() {
        TreePath path = tree.getSelectionPath();

        if (path == null || path.getPath().length == 1) {
            return;
        }
        CellNode target = (CellNode) path.getLastPathComponent();

        target.cell.parent().remove(target.cell);
        model.removeNodeFromParent(target);
    }

    private class CellNode extends DefaultMutableTreeNode {
        public Cell cell;
        private boolean initialized = false;

        public CellNode(Cell cell) {
            this.cell = cell;
        }

        @Override
        public String toString() {
            if (cell.stem() != null) {
                return cell.name() + " : " + cell.stem().fullName();
            }
            return cell.name();
        }

        @Override
        public boolean isLeaf() {
            if (!initialized) {
                for (Cell c : cell.children()) {
                    add(new CellNode(c));
                }
                initialized = true;
            }
            return super.isLeaf();
        }
    }

    private class Listener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)
                    (e.getTreePath().getLastPathComponent());

        /*
         * If the event lists children, then the changed
         * node is the child of the node we have already
         * gotten.  Otherwise, the changed node and the
         * specified node are the same.
         */
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                        (node.getChildAt(index));
            } catch (NullPointerException ignored) {
            }

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }

        public void treeNodesInserted(TreeModelEvent e) {
        }

        public void treeNodesRemoved(TreeModelEvent e) {
        }

        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
}
