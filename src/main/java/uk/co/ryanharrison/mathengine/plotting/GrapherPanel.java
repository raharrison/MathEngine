package uk.co.ryanharrison.mathengine.plotting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interactive graphing panel for plotting mathematical functions.
 * <p>
 * This panel provides a high-quality, interactive graphing experience with:
 * </p>
 * <ul>
 *     <li>Mouse drag panning</li>
 *     <li>Mouse wheel zooming (centered on cursor position)</li>
 *     <li>Keyboard navigation (arrow keys, Space to reset)</li>
 *     <li>Support for multiple functions with different colors</li>
 *     <li>Adaptive function sampling with discontinuity detection</li>
 *     <li>Coordinate display on mouse hover</li>
 * </ul>
 *
 * <h2>Mouse Controls:</h2>
 * <ul>
 *     <li><b>Drag:</b> Pan the viewport</li>
 *     <li><b>Wheel:</b> Zoom in/out (centered on cursor)</li>
 *     <li><b>Double-click:</b> Zoom in centered on click point</li>
 * </ul>
 *
 * <h2>Keyboard Controls:</h2>
 * <ul>
 *     <li><b>Arrow keys:</b> Pan viewport</li>
 *     <li><b>Space:</b> Reset to default view</li>
 *     <li><b>+/-:</b> Zoom in/out</li>
 * </ul>
 */
public final class GrapherPanel extends JPanel {
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color GRID_COLOR = new Color(220, 220, 220);
    private static final Color AXIS_COLOR = Color.BLACK;
    private static final Color ORIGIN_COLOR = new Color(100, 100, 100);
    private static final Color CROSSHAIR_COLOR = new Color(150, 150, 150);

    private static final BasicStroke GRID_STROKE = new BasicStroke(0.5f);
    private static final BasicStroke AXIS_STROKE = new BasicStroke(1.5f);
    private static final BasicStroke TICK_STROKE = new BasicStroke(1.0f);

    private final List<PlottedFunction> functions = new ArrayList<>();
    private GraphCoordinateSystem coords;
    private final FunctionRenderer renderer = new FunctionRenderer();

    // Interaction state
    private Point lastMousePoint;
    private Point currentMousePoint;
    private boolean isDragging = false;

    // Rendering optimization
    private Image backBuffer;
    private Graphics2D backGraphics;
    private boolean needsRedraw = true;

    // Event listeners for status updates
    private final List<GrapherEventListener> listeners = new ArrayList<>();

    /**
     * Creates a new grapher panel with default settings.
     */
    public GrapherPanel() {
        coords = new GraphCoordinateSystem(800, 600);
        coords.resetView();

        setBackground(BACKGROUND_COLOR);
        setDoubleBuffered(true);
        setFocusable(true);

        initializeEventHandlers();
    }

    /**
     * Adds a function to the graph.
     *
     * @param function the function to add
     */
    public void addFunction(PlottedFunction function) {
        functions.add(function);
        needsRedraw = true;
        repaint();
        fireGraphChanged();
    }

    /**
     * Removes a function from the graph.
     *
     * @param function the function to remove
     * @return true if the function was removed
     */
    public boolean removeFunction(PlottedFunction function) {
        boolean removed = functions.remove(function);
        if (removed) {
            needsRedraw = true;
            repaint();
            fireGraphChanged();
        }
        return removed;
    }

    /**
     * Removes all functions from the graph.
     */
    public void clearFunctions() {
        functions.clear();
        needsRedraw = true;
        repaint();
        fireGraphChanged();
    }

    /**
     * Gets an unmodifiable view of the current functions.
     *
     * @return the list of functions
     */
    public List<PlottedFunction> getFunctions() {
        return Collections.unmodifiableList(functions);
    }

    /**
     * Replaces a function at the given index.
     *
     * @param index       the index of the function to replace
     * @param newFunction the new function
     */
    public void replaceFunction(int index, PlottedFunction newFunction) {
        if (index >= 0 && index < functions.size()) {
            functions.set(index, newFunction);
            needsRedraw = true;
            repaint();
            fireGraphChanged();
        }
    }

    /**
     * Adds an event listener for graph events.
     *
     * @param listener the listener to add
     */
    public void addGrapherEventListener(GrapherEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets the current coordinate system.
     *
     * @return the coordinate system
     */
    public GraphCoordinateSystem getCoordinateSystem() {
        return coords;
    }

    /**
     * Forces a full redraw of the graph on the next paint.
     * <p>
     * Call this after modifying the coordinate system externally.
     * </p>
     */
    public void forceRedraw() {
        needsRedraw = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        // Update coordinate system if size changed
        if (width != coords.getWidth() || height != coords.getHeight()) {
            coords = coords.withDimensions(width, height);
            needsRedraw = true;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        // Use back buffer for smooth rendering
        if (backBuffer == null || backBuffer.getWidth(this) != width || backBuffer.getHeight(this) != height) {
            backBuffer = createImage(width, height);
            backGraphics = (Graphics2D) backBuffer.getGraphics();
            backGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            backGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            backGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            backGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            needsRedraw = true;
        }

        // Redraw background layers if needed
        if (needsRedraw) {
            renderBackground(backGraphics);
            needsRedraw = false;
        }

        // Draw back buffer to screen
        g2d.drawImage(backBuffer, 0, 0, this);

        // Draw overlay elements (crosshair, origin marker)
        renderOverlay(g2d);
    }

    /**
     * Renders the background (grid, axes, functions) to the back buffer.
     */
    private void renderBackground(Graphics2D g) {
        int width = getWidth();
        int height = getHeight();

        // Clear background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);

        // Draw grid
        renderGrid(g);

        // Draw axes
        renderAxes(g);

        // Draw all functions
        for (PlottedFunction function : functions) {
            renderer.render(g, function, coords);
        }
    }

    /**
     * Renders overlay elements that change with mouse movement.
     */
    private void renderOverlay(Graphics2D g) {
        // Draw origin marker
        Point2D.Double origin = coords.toScreen(0, 0);
        if (coords.isInBounds(origin.x, origin.y)) {
            drawPoint(g, origin.x, origin.y, 3.0, ORIGIN_COLOR);
        }

        // Draw crosshair at center
        drawCrosshair(g);
    }

    /**
     * Renders the grid lines.
     */
    private void renderGrid(Graphics2D g) {
        g.setColor(GRID_COLOR);
        g.setStroke(GRID_STROKE);

        double unitSize = coords.getUnitSize();
        double minX = coords.getMinVisibleX();
        double maxX = coords.getMaxVisibleX();
        double minY = coords.getMinVisibleY();
        double maxY = coords.getMaxVisibleY();

        // Vertical grid lines
        double startX = Math.floor(minX / unitSize) * unitSize;
        for (double x = startX; x <= maxX; x += unitSize * 0.2) {
            Point2D.Double top = coords.toScreen(x, maxY);
            Point2D.Double bottom = coords.toScreen(x, minY);
            g.draw(new Line2D.Double(top.x, top.y, bottom.x, bottom.y));
        }

        // Horizontal grid lines
        double startY = Math.floor(minY / unitSize) * unitSize;
        for (double y = startY; y <= maxY; y += unitSize * 0.2) {
            Point2D.Double left = coords.toScreen(minX, y);
            Point2D.Double right = coords.toScreen(maxX, y);
            g.draw(new Line2D.Double(left.x, left.y, right.x, right.y));
        }
    }

    /**
     * Renders the coordinate axes with labels.
     */
    private void renderAxes(Graphics2D g) {
        g.setStroke(AXIS_STROKE);
        g.setColor(AXIS_COLOR);

        Point2D.Double origin = coords.toScreen(0, 0);
        int width = getWidth();
        int height = getHeight();

        // Clamp axis positions to visible area
        double xAxisY = Math.max(10, Math.min(height - 20, origin.y));
        double yAxisX = Math.max(10, Math.min(width - 10, origin.x));

        // Draw y-axis
        g.draw(new Line2D.Double(yAxisX, 0, yAxisX, height));

        // Draw x-axis
        g.draw(new Line2D.Double(0, xAxisY, width, xAxisY));

        // Draw tick marks and labels
        renderAxisLabels(g, yAxisX, xAxisY);
    }

    /**
     * Renders tick marks and labels on the axes.
     */
    private void renderAxisLabels(Graphics2D g, double yAxisX, double xAxisY) {
        g.setStroke(TICK_STROKE);
        g.setColor(AXIS_COLOR);

        Font font = new Font("SansSerif", Font.PLAIN, 10);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        double unitSize = coords.getUnitSize();
        double minX = coords.getMinVisibleX();
        double maxX = coords.getMaxVisibleX();
        double minY = coords.getMinVisibleY();
        double maxY = coords.getMaxVisibleY();

        // Y-axis labels
        double startY = Math.floor(minY / unitSize) * unitSize;
        for (double y = startY; y <= maxY; y += unitSize) {
            if (Math.abs(y) < unitSize * 0.01) continue; // Skip zero

            Point2D.Double point = coords.toScreen(0, y);
            if (point.y < 0 || point.y > getHeight()) continue;

            // Tick mark
            g.draw(new Line2D.Double(yAxisX - 4, point.y, yAxisX + 4, point.y));

            // Label
            String label = formatAxisLabel(y);
            float labelX = (float) Math.max(2, yAxisX - fm.stringWidth(label) - 6);
            float labelY = (float) (point.y + fm.getHeight() / 3.0);
            g.drawString(label, labelX, labelY);
        }

        // X-axis labels
        double startX = Math.floor(minX / unitSize) * unitSize;
        for (double x = startX; x <= maxX; x += unitSize) {
            if (Math.abs(x) < unitSize * 0.01) continue; // Skip zero

            Point2D.Double point = coords.toScreen(x, 0);
            if (point.x < 0 || point.x > getWidth()) continue;

            // Tick mark
            g.draw(new Line2D.Double(point.x, xAxisY - 4, point.x, xAxisY + 4));

            // Label
            String label = formatAxisLabel(x);
            float labelX = (float) (point.x - fm.stringWidth(label) / 2.0);
            float labelY = (float) (xAxisY + fm.getHeight() + 2);
            if (xAxisY > getHeight() - 25) {
                labelY = (float) (xAxisY - 6);
            }
            g.drawString(label, labelX, labelY);
        }
    }

    /**
     * Formats a value for axis labels, choosing appropriate precision.
     */
    private String formatAxisLabel(double value) {
        // Round to avoid floating point artifacts
        double rounded = Math.round(value * 1e10) / 1e10;

        if (Math.abs(rounded) < 1e-10) {
            return "0";
        } else if (rounded == (int) rounded) {
            return String.valueOf((int) rounded);
        } else if (Math.abs(rounded) >= 1000 || Math.abs(rounded) < 0.01) {
            return String.format("%.2e", rounded);
        } else {
            // Use appropriate decimal places based on magnitude
            String formatted = String.format("%.6f", rounded);
            // Remove trailing zeros
            formatted = formatted.replaceAll("0+$", "").replaceAll("\\.$", "");
            return formatted;
        }
    }

    /**
     * Draws a small point at the given screen coordinates.
     */
    private void drawPoint(Graphics2D g, double x, double y, double radius, Color color) {
        Ellipse2D circle = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        g.setColor(color);
        g.fill(circle);
        g.setColor(AXIS_COLOR);
        g.setStroke(new BasicStroke(1.0f));
        g.draw(circle);
    }

    /**
     * Draws a crosshair at the center of the viewport.
     */
    private void drawCrosshair(Graphics2D g) {
        g.setStroke(new BasicStroke(1.0f));
        g.setColor(CROSSHAIR_COLOR);

        double centerX = getWidth() / 2.0;
        double centerY = getHeight() / 2.0;
        double size = 5.0;

        g.draw(new Line2D.Double(centerX - size, centerY, centerX + size, centerY));
        g.draw(new Line2D.Double(centerX, centerY - size, centerX, centerY + size));
    }


    /**
     * Initializes mouse and keyboard event handlers.
     */
    private void initializeEventHandlers() {
        // Mouse listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePoint = e.getPoint();
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Double-click to zoom in at point
                    coords.zoomToward(e.getX(), e.getY(), true);
                    needsRedraw = true;
                    repaint();
                    fireViewChanged();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentMousePoint = null;
                fireMouseExited();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastMousePoint != null) {
                    double dx = e.getX() - lastMousePoint.x;
                    double dy = e.getY() - lastMousePoint.y;
                    coords.pan(dx, dy);
                    lastMousePoint = e.getPoint();
                    isDragging = true;
                    needsRedraw = true;
                    repaint();
                    fireViewChanged();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                currentMousePoint = e.getPoint();
                fireMouseMoved(e.getPoint());
            }
        });

        // Mouse wheel listener
        addMouseWheelListener(e -> {
            boolean zoomIn = e.getWheelRotation() < 0;
            coords.zoomToward(e.getX(), e.getY(), zoomIn);
            needsRedraw = true;
            repaint();
            fireViewChanged();
        });

        // Keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
    }

    /**
     * Handles keyboard navigation.
     */
    private void handleKeyPress(int keyCode) {
        double panAmount = coords.getUnitSize() * 2;
        boolean changed = false;

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                coords.setOrigin(coords.getOriginX() - panAmount, coords.getOriginY());
                changed = true;
                break;
            case KeyEvent.VK_RIGHT:
                coords.setOrigin(coords.getOriginX() + panAmount, coords.getOriginY());
                changed = true;
                break;
            case KeyEvent.VK_UP:
                coords.setOrigin(coords.getOriginX(), coords.getOriginY() + panAmount);
                changed = true;
                break;
            case KeyEvent.VK_DOWN:
                coords.setOrigin(coords.getOriginX(), coords.getOriginY() - panAmount);
                changed = true;
                break;
            case KeyEvent.VK_SPACE:
                coords.resetView();
                changed = true;
                break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_EQUALS:
                changed = coords.zoomIn();
                break;
            case KeyEvent.VK_MINUS:
            case KeyEvent.VK_UNDERSCORE:
                changed = coords.zoomOut();
                break;
        }

        if (changed) {
            needsRedraw = true;
            repaint();
            fireViewChanged();
        }
    }

    private void fireGraphChanged() {
        for (GrapherEventListener listener : listeners) {
            listener.onGraphChanged();
        }
    }

    private void fireViewChanged() {
        for (GrapherEventListener listener : listeners) {
            listener.onViewChanged(coords);
        }
    }

    private void fireMouseMoved(Point point) {
        Point2D.Double cartesian = coords.toCartesian(point.x, point.y);
        for (GrapherEventListener listener : listeners) {
            listener.onMouseMoved(cartesian.x, cartesian.y);
        }
    }

    private void fireMouseExited() {
        for (GrapherEventListener listener : listeners) {
            listener.onMouseExited();
        }
    }

    /**
     * Interface for listening to grapher events.
     */
    public interface GrapherEventListener {
        /**
         * Called when the graph content changes (functions added/removed).
         */
        default void onGraphChanged() {
        }

        /**
         * Called when the viewport changes (pan/zoom).
         *
         * @param coords the current coordinate system
         */
        default void onViewChanged(GraphCoordinateSystem coords) {
        }

        /**
         * Called when the mouse moves over the graph.
         *
         * @param x the x-coordinate in graph space
         * @param y the y-coordinate in graph space
         */
        default void onMouseMoved(double x, double y) {
        }

        /**
         * Called when the mouse exits the graph area.
         */
        default void onMouseExited() {
        }
    }
}
