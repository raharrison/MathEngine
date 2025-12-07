package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod;
import uk.co.ryanharrison.mathengine.differential.symbolic.Differentiator;

/**
 * Class representing a {@link RootPolishingMethod} that uses the Newton Raphson
 * method to estimate the roots of the target function.
 * <p>
 * This solver requires the presence of the derivative of the target function to
 * solve for roots.
 * <p>
 * This may be through numerical differentiation, symbolic differentiation, or
 * through another function from the user.
 *
 * @author Ryan Harrison
 *
 */
public final class NewtonRaphsonSolver extends RootPolishingMethod {
    /**
     * The derivative of the target function
     */
    private Function derivativefunction;

    /**
     * The method in which values of the derivative of the target function will
     * be obtained
     */
    private DifferentiationMethod differentiationMethod;

    /**
     * Construct a new {@link NewtonRaphsonSolver} with the specified target
     * {@link Function}
     * <p>
     * This constructor will set the object to use numerical differentiation
     *
     * @param targetFunction The function to estimate the roots of
     */
    public NewtonRaphsonSolver(Function targetFunction) {
        super(targetFunction);
        this.setDifferentiationMethod(DifferentiationMethod.Numerical);
    }

    /**
     * Construct a new {@link NewtonRaphsonSolver} with the specified target
     * {@link Function} and derivative of the target {@link Function}
     *
     * @param targetFunction     The function to estimate the roots of
     * @param derivativeFunction The derivative of the target function
     */
    public NewtonRaphsonSolver(Function targetFunction, Function derivativeFunction) {
        super(targetFunction);
        this.setDerivativefunction(derivativeFunction);
        this.setDifferentiationMethod(DifferentiationMethod.Predefined);
    }

    /**
     * Get the derivative of the target function
     *
     * @return The derivative of the target function or null if none has been
     * set
     */
    public Function getDerivativefunction() {
        return derivativefunction;
    }

    /**
     * Get the method in which values of the derivative of the target function
     * will be obtained
     *
     * @return The method in which values of the derivative of the target
     * function will be obtained
     */
    public DifferentiationMethod getDifferentiationMethod() {
        return this.differentiationMethod;
    }

    /**
     * Set the derivative of the target function
     *
     * @param derivativefunction The derivative of the target function
     */
    public void setDerivativefunction(Function derivativefunction) {
        this.derivativefunction = derivativefunction;
    }

    /**
     * Set the method in which values of the derivative of the target function
     * will be obtained.
     * <p>
     * <p>
     * If the new method is symbolic, any predefined derivative function will be
     * overwritten by the new symbolic evaluation
     *
     * @param newMethod The new differentiation method
     * @throws IllegalArgumentException If the method is to use a predefined derivative function
     *                                  yet the function has not yet been defined
     */
    public void setDifferentiationMethod(DifferentiationMethod newMethod) {
        if (newMethod == DifferentiationMethod.Predefined && this.derivativefunction == null)
            throw new IllegalArgumentException("Predefined deriviative function has not been set");
        else if (newMethod == DifferentiationMethod.Symbolic)
            this.derivativefunction = new Differentiator().differentiate(targetFunction, false);

        this.differentiationMethod = newMethod;
    }

    /**
     * Evaluates the derivative of the target function at the specified point
     * using the current {@link DifferentiationMethod}
     *
     * @param d The point to evaluate at
     * @return The value of the derivative function at d
     */
    private double evaluateDerivativeAt(double d) {
        if (this.differentiationMethod == DifferentiationMethod.Numerical) {
            ExtendedCentralDifferenceMethod exd = new ExtendedCentralDifferenceMethod(
                    targetFunction);
            exd.setTargetPoint(d);
            return exd.deriveFirst();
        } else {
            return derivativefunction.evaluateAt(d);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If a root was not found within the required number of
     *                                  iterations within the specified tolerance
     */
    @Override
    public double solve() {
        super.checkRootFindingParams();

        Function f = this.targetFunction;

        int iteration = 1;
        double xm = initialGuess;

        while (iteration <= this.iterations) {
            if (Double.isInfinite(xm))
                throw new RuntimeException("Algorithm has diverged");

            xm = xm - f.evaluateAt(xm) / evaluateDerivativeAt(xm);

            // Return the current estimation if it is within the set tolerance
            // and the convergence criteria is set to accept this
            if (Math.abs(f.evaluateAt(xm)) < tolerance
                    && convergenceCriteria == ConvergenceCriteria.WithinTolerance) {
                return xm;
            }

            iteration++;
        }

        // If the number of iterations has been used and the convergence
        // criteria is set to accept this, return the estimation
        if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations) {
            return xm;
        }

        throw new UnsupportedOperationException(
                "Unable to find the root within specified tolerance after " + iterations
                        + " iterations");
    }
}
