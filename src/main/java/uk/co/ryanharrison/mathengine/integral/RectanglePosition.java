package uk.co.ryanharrison.mathengine.integral;

/**
 * Enumeration representing the position of rectangle within the integral.
 * <p>
 * For use in the RectangularIntegrator which uses rectangles to estimate the
 * integral of a function
 *
 * @author Ryan Harrison
 *
 */
public enum RectanglePosition {
    Left, Midpoint, Right;
}
