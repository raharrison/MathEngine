package uk.co.ryanharrison.mathengine.differential;

/**
 * Represents the three commonly used forms of of differences for use when
 * estimating derivatives numerically
 * <p>
 * The central direction tends to be the most accurate
 *
 * @author Ryan Harrison
 *
 */
public enum DifferencesDirection {
    Forward, Central, Backward
}
