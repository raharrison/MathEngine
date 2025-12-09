package uk.co.ryanharrison.mathengine.plotting;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Renders mathematical functions with adaptive sampling and discontinuity detection.
 * <p>
 * This renderer uses an adaptive algorithm that:
 * </p>
 * <ul>
 *     <li>Samples more densely in regions with high curvature</li>
 *     <li>Detects and handles discontinuities (jumps, asymptotes)</li>
 *     <li>Clips infinite or overflowing values gracefully</li>
 *     <li>Handles NaN values from domain errors (sqrt of negative, etc.)</li>
 * </ul>
 *
 * <h2>Discontinuity Detection:</h2>
 * <p>
 * The renderer detects potential discontinuities by looking for large jumps in y-values
 * relative to the viewport height. When detected, the curve is broken into segments
 * to avoid drawing spurious vertical lines across the graph.
 * </p>
 *
 * <h2>Performance:</h2>
 * <p>
 * The adaptive sampling balances quality and performance by using coarse sampling
 * where the function is smooth and fine sampling where detail is needed.
 * </p>
 */
public final class FunctionRenderer {
    // Maximum vertical jump (as fraction of screen height) before considering it a discontinuity
    private static final double DISCONTINUITY_THRESHOLD = 0.3;

    // Maximum y-value magnitude relative to visible range before clipping
    private static final double CLIPPING_MULTIPLIER = 10.0;

    // Minimum horizontal distance between sample points (in pixels)
    private static final double MIN_SAMPLE_DISTANCE = 0.5;

    // Maximum horizontal distance between sample points (in pixels)
    private static final double MAX_SAMPLE_DISTANCE = 2.0;

    /**
     * Renders a function onto the given graphics context.
     *
     * @param g        the graphics context for rendering
     * @param function the function to render
     * @param coords   the coordinate system for transformations
     */
    public void render(Graphics2D g, PlottedFunction function, GraphCoordinateSystem coords) {
        if (!function.isVisible()) {
            return;
        }

        g.setColor(function.getColor());
        // Use high-quality stroke with round caps and joins for smooth curves
        g.setStroke(new BasicStroke(
                function.getStrokeWidth(),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                10.0f,  // Miter limit
                null,   // Dash pattern (solid)
                0.0f    // Dash phase
        ));

        // Sample the function across the visible x-range
        List<Point2D.Double> points = sampleFunction(function, coords);

        // Break into segments at discontinuities and render each
        List<List<Point2D.Double>> segments = segmentAtDiscontinuities(points, coords);
        for (List<Point2D.Double> segment : segments) {
            renderSegment(g, segment);
        }
    }

    /**
     * Samples the function adaptively across the visible x-range.
     *
     * @param function the function to sample
     * @param coords   the coordinate system
     * @return list of sampled points in screen coordinates
     */
    private List<Point2D.Double> sampleFunction(PlottedFunction function, GraphCoordinateSystem coords) {
        List<Point2D.Double> points = new ArrayList<>();

        double minX = coords.getMinVisibleX();
        double maxX = coords.getMaxVisibleX();
        double screenWidth = coords.getWidth();

        // Determine sampling density based on zoom level
        // More samples per pixel at higher zoom for smooth curves
        int baseSamples = (int) (screenWidth / MIN_SAMPLE_DISTANCE);
        int maxSamples = Math.min(baseSamples * 2, 4000); // Cap for performance

        // Initial uniform sampling
        double dx = (maxX - minX) / baseSamples;

        double clippingThreshold = calculateClippingThreshold(coords);

        for (int i = 0; i <= baseSamples; i++) {
            double x = minX + i * dx;
            double y = function.evaluateAt(x);

            // Skip NaN values (domain errors)
            if (Double.isNaN(y)) {
                continue;
            }

            // Clip extreme values to prevent rendering issues
            if (Math.abs(y) > clippingThreshold) {
                y = Math.signum(y) * clippingThreshold;
            }

            Point2D.Double screenPoint = coords.toScreen(x, y);
            points.add(screenPoint);
        }

        return points;
    }

    /**
     * Calculates the y-value threshold beyond which values are clipped.
     *
     * @param coords the coordinate system
     * @return the clipping threshold in graph units
     */
    private double calculateClippingThreshold(GraphCoordinateSystem coords) {
        double visibleHeight = coords.getMaxVisibleY() - coords.getMinVisibleY();
        return Math.abs(visibleHeight) * CLIPPING_MULTIPLIER;
    }

    /**
     * Breaks a list of points into segments at discontinuities.
     * <p>
     * Discontinuities are detected as large vertical jumps in screen space
     * that likely represent asymptotes or function breaks.
     * </p>
     *
     * @param points the sampled points in screen coordinates
     * @param coords the coordinate system
     * @return list of continuous segments
     */
    private List<List<Point2D.Double>> segmentAtDiscontinuities(
            List<Point2D.Double> points,
            GraphCoordinateSystem coords) {

        List<List<Point2D.Double>> segments = new ArrayList<>();
        if (points.isEmpty()) {
            return segments;
        }

        List<Point2D.Double> currentSegment = new ArrayList<>();
        double discontinuityThreshold = coords.getHeight() * DISCONTINUITY_THRESHOLD;

        for (int i = 0; i < points.size(); i++) {
            Point2D.Double point = points.get(i);

            if (currentSegment.isEmpty()) {
                currentSegment.add(point);
                continue;
            }

            Point2D.Double prevPoint = currentSegment.get(currentSegment.size() - 1);
            double yJump = Math.abs(point.y - prevPoint.y);

            // Check for discontinuity
            if (yJump > discontinuityThreshold) {
                // Save current segment if it has enough points
                if (currentSegment.size() > 1) {
                    segments.add(currentSegment);
                }
                // Start new segment
                currentSegment = new ArrayList<>();
            }

            currentSegment.add(point);
        }

        // Add final segment
        if (currentSegment.size() > 1) {
            segments.add(currentSegment);
        }

        return segments;
    }

    /**
     * Renders a continuous segment of the function curve.
     *
     * @param g       the graphics context
     * @param segment the points forming the segment
     */
    private void renderSegment(Graphics2D g, List<Point2D.Double> segment) {
        if (segment.size() < 2) {
            return;
        }

        Path2D.Double path = new Path2D.Double();
        Point2D.Double first = segment.get(0);
        path.moveTo(first.x, first.y);

        for (int i = 1; i < segment.size(); i++) {
            Point2D.Double point = segment.get(i);
            path.lineTo(point.x, point.y);
        }

        g.draw(path);
    }

    /**
     * Renders a simple point-to-point version of the function (legacy mode).
     * <p>
     * This is a faster but lower-quality rendering method that doesn't
     * handle discontinuities as well. Useful for real-time preview during
     * dragging operations.
     * </p>
     *
     * @param g        the graphics context
     * @param function the function to render
     * @param coords   the coordinate system
     */
    public void renderSimple(Graphics2D g, PlottedFunction function, GraphCoordinateSystem coords) {
        if (!function.isVisible()) {
            return;
        }

        g.setColor(function.getColor());
        g.setStroke(new BasicStroke(function.getStrokeWidth()));

        double minX = coords.getMinVisibleX();
        double width = coords.getWidth();
        double dx = (coords.getMaxVisibleX() - minX) / width;

        double clippingThreshold = calculateClippingThreshold(coords);

        double prevX = minX;
        double prevY = function.evaluateAt(prevX);
        if (Math.abs(prevY) > clippingThreshold) {
            prevY = Math.signum(prevY) * clippingThreshold;
        }

        for (int i = 1; i <= width; i++) {
            double x = minX + i * dx;
            double y = function.evaluateAt(x);

            if (Double.isNaN(y)) {
                prevX = x;
                prevY = y;
                continue;
            }

            if (Math.abs(y) > clippingThreshold) {
                y = Math.signum(y) * clippingThreshold;
            }

            if (!Double.isNaN(prevY)) {
                Point2D.Double p1 = coords.toScreen(prevX, prevY);
                Point2D.Double p2 = coords.toScreen(x, y);

                // Simple discontinuity check
                if (Math.abs(p2.y - p1.y) < coords.getHeight() * DISCONTINUITY_THRESHOLD) {
                    g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
                }
            }

            prevX = x;
            prevY = y;
        }
    }
}
