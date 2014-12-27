package org.zells.alpha.rendering;

import org.zells.alpha.core.Body;
import org.zells.alpha.core.Cell;
import org.zells.alpha.core.eventing.SpecificEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class View extends JPanel {
    private Body root;
    private Point2D origin;
    private double scale = 1;
    private double zoom = 1;
    private Canvas canvas;

    public View(Cell c, Dimension size) {
        root = c.body;
        root.events().add(new SpecificEventListener<Body.RepaintEvent>(Body.RepaintEvent.class) {
            @Override
            protected void handleSpecific(Body.RepaintEvent event) {
                repaint();
            }
        });

        origin = new Point2D.Double(size.getWidth() / 2, size.getHeight() / 2);
        setPreferredSize(size);

        Listener listener = new Listener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
        addKeyListener(listener);
        addComponentListener(listener);

        setFocusable(true);
        setBackground(Color.white);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g);
    }

    private void render(Graphics2D g) {
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        canvas = new Canvas(root, getZoomedSize(), g);
        canvas.transformation().setTransform(transformation());
        canvas.render();
    }

    private AffineTransform transformation() {
        AffineTransform tx = new AffineTransform();
        tx.translate(origin.getX(), origin.getY());
        tx.scale(scale, -scale);
        return tx;
    }

    public void translate(Point2D by) {
        origin.setLocation(origin.getX() + by.getX(), origin.getY() + by.getY());
        repaint();
    }

    public void scale(double by) {
        scale *= by;
        zoom /= by;
        repaint();
    }

    public void zoom(double by, Point2D focus) {
        try {
            focus = transformation().inverseTransform(focus, null);
        } catch (NoninvertibleTransformException ignored) {
        }
        translate(new Point2D.Double(
                (1 - by) * focus.getX(),
                (by - 1) * focus.getY()
        ));
        zoom *= by;
        repaint();
    }

    private void input(Input i) {
        if (canvas != null && canvas.contains(i.event().getPoint())) {
            canvas.input(i);
        }
    }

    public Dimension2D getZoomedSize() {
        return new Dimension((int)(getSize().width * zoom), (int)(getSize().height * zoom));
    }

    private class Listener implements MouseListener, MouseWheelListener, KeyListener, MouseMotionListener, ComponentListener {

        private Point startDrag;
        private Dimension size;

        @Override
        public void mouseReleased(MouseEvent e) {
            startDrag = null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON2) {
                if (startDrag == null) {
                    startDrag = e.getPoint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (startDrag != null) {
                translate(new Point(e.getX() - startDrag.x, e.getY() - startDrag.y));
                startDrag = e.getPoint();
            } else {
                input(new Input(e) {
                    @Override
                    public void execute(MouseAdapter a) {
                        a.mouseDragged(event());
                    }
                });
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getModifiers() == InputEvent.CTRL_MASK) {
                if (e.getWheelRotation() < 0) {
                    zoom(1.25, e.getPoint());
                } else {
                    zoom(0.8, e.getPoint());
                }
            } else if (e.getModifiers() == InputEvent.SHIFT_MASK) {
                if (e.getWheelRotation() < 0) {
                    scale(1.25);
                } else {
                    scale(0.8);
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            input(new Input(e) {
                @Override
                public void execute(MouseAdapter a) {
                    a.mouseClicked(event());
                }
            });
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            input(new Input(e) {
                @Override
                public void execute(MouseAdapter a) {
                    a.mouseMoved(event());
                }
            });
        }

        @Override
        public void componentResized(ComponentEvent e) {
            Dimension newSize = e.getComponent().getSize();
            if (size != null) {
                translate(new Point2D.Double(
                        -(size.getWidth() - newSize.getWidth()) / 2,
                        -(size.getHeight() - newSize.getHeight()) / 2
                ));
            }
            size = newSize;
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }
    }
}
