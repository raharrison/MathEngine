package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an equation solving method that polishes or refines an
 * initial point until it becomes sufficiently close to the root of a function
 *
 * @author Ryan Harrison
 *
 */
public abstract class RootPolishingMethod extends EquationSolver {
    /**
     * An initial point or guess of the root of the function that will be
     * polished to make more accurate
     */
    protected double initialGuess;

    /**
     * Construct a new {@link RootPolishingMethod} with the specified target
     * {@link Function}
     *
     * @param targetFunction The function to find the root of
     */
    public RootPolishingMethod(Function targetFunction) {
        this.targetFunction = targetFunction;
        this.initialGuess = 1;
    }

    /**
     * Get the initial guess point of the root of the target function
     *
     * @return The initial guess of the root of the target function
     */
    public double getInitialGuess() {
        return this.initialGuess;
    }

    /**
     * Set the initial guess point of the root of the target function
     *
     * @param initialGuess The new initial guess of the root
     */
    public void setInitialGuess(double initialGuess) {
        this.initialGuess = initialGuess;
    }

    /**
     * Attempt to find all of the roots of the target function between the upper
     * and lower bounds
     *
     * @param upper The upper bound to search for roots in
     * @param lower The lower bound to search for roots in
     * @return A list of estimated roots of the target function between the
     * upper and lower bounds
     */
    public List<Double> solveAll(double upper, double lower) {
        super.checkRootFindingParams();

        // calculate a series of brackets that may contain roots. These will act
        // as our initial guesses
        // as they are already close to a possible root
        List<RootInterval> bracks = findBrackets(targetFunction, lower, upper, iterations);
        List<Double> roots = new ArrayList<Double>();

        double tGuess = initialGuess;

        for (RootInterval interval : bracks) {
            // use the midpoint of the interval as our guess to polish
            initialGuess = (interval.getUpper() + interval.getLower()) / 2;

            // apply the current root polishing method to the guess to get an
            // answer
            double result = solve();

            // add the estimated root to the answers list of not already present
            if (!roots.contains(result)) {
                roots.add(result);
            }
        }

        initialGuess = tGuess;

        return roots;
    }
}
