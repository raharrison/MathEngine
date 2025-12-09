package uk.co.ryanharrison.mathengine.plotting;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Status bar for displaying graph information.
 * <p>
 * Shows real-time information about the current graph state:
 * </p>
 * <ul>
 *     <li>Current zoom level and unit size</li>
 *     <li>Viewport bounds (visible x and y ranges)</li>
 *     <li>Number of plotted functions</li>
 * </ul>
 */
public final class GrapherStatusBar extends JPanel {
    private final JLabel zoomLabel = new JLabel();
    private final JLabel boundsLabel = new JLabel();
    private final JLabel functionsLabel = new JLabel();
    private final JLabel coordinatesLabel = new JLabel();

    /**
     * Creates a new status bar.
     */
    public GrapherStatusBar() {
        setLayout(new GridLayout(1, 4, 10, 0));
        setBorder(BorderFactory.createCompoundBorder(
                new BevelBorder(BevelBorder.LOWERED),
                BorderFactory.createEmptyBorder(2, 10, 2, 10)
        ));

        Font font = new Font("SansSerif", Font.PLAIN, 11);
        zoomLabel.setFont(font);
        boundsLabel.setFont(font);
        functionsLabel.setFont(font);
        coordinatesLabel.setFont(font);

        add(zoomLabel);
        add(boundsLabel);
        add(functionsLabel);
        add(coordinatesLabel);

        updateZoom(13, 1.0);
        updateBounds(0, 0, 0, 0);
        updateFunctionCount(0);
        updateCoordinates(0, 0);
    }

    /**
     * Updates the zoom information display.
     *
     * @param zoomLevel the current zoom level
     * @param unitSize  the graph unit size at this zoom
     */
    public void updateZoom(int zoomLevel, double unitSize) {
        String unitStr = formatValue(unitSize);
        zoomLabel.setText(String.format("Zoom: %d  |  Unit: %s", zoomLevel, unitStr));
    }

    /**
     * Updates the visible bounds display.
     *
     * @param minX the minimum visible x-coordinate
     * @param maxX the maximum visible x-coordinate
     * @param minY the minimum visible y-coordinate
     * @param maxY the maximum visible y-coordinate
     */
    public void updateBounds(double minX, double maxX, double minY, double maxY) {
        String xRange = formatValue(minX) + " to " + formatValue(maxX);
        String yRange = formatValue(minY) + " to " + formatValue(maxY);
        boundsLabel.setText(String.format("X: [%s]  Y: [%s]", xRange, yRange));
    }

    /**
     * Updates the function count display.
     *
     * @param count the number of functions
     */
    public void updateFunctionCount(int count) {
        functionsLabel.setText(String.format("Functions: %d", count));
    }

    /**
     * Updates the mouse coordinates display.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void updateCoordinates(double x, double y) {
        coordinatesLabel.setText(String.format("Cursor: (%.4f, %.4f)", x, y));
    }

    /**
     * Clears the coordinate display (when mouse leaves).
     */
    public void clearCoordinates() {
        coordinatesLabel.setText("Cursor: -");
    }

    /**
     * Updates all status information from a coordinate system.
     *
     * @param coords        the coordinate system
     * @param functionCount the number of functions
     */
    public void updateFromCoordinateSystem(GraphCoordinateSystem coords, int functionCount) {
        updateZoom(coords.getZoomLevel(), coords.getUnitSize());
        updateBounds(
                coords.getMinVisibleX(),
                coords.getMaxVisibleX(),
                coords.getMinVisibleY(),
                coords.getMaxVisibleY()
        );
        updateFunctionCount(functionCount);
    }

    /**
     * Formats a numeric value for display, choosing appropriate precision.
     */
    private String formatValue(double value) {
        double abs = Math.abs(value);

        if (abs < 1e-10) {
            return "0";
        } else if (abs >= 10000 || (abs < 0.001 && abs > 0)) {
            return String.format("%.2e", value);
        } else if (value == (int) value) {
            return String.valueOf((int) value);
        } else {
            // Format with appropriate precision
            String formatted = String.format("%.4f", value);
            // Remove trailing zeros
            formatted = formatted.replaceAll("0+$", "").replaceAll("\\.$", "");
            return formatted;
        }
    }
}
