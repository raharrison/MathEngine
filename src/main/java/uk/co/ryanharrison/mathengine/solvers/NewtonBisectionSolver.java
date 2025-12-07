package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod;
import uk.co.ryanharrison.mathengine.differential.symbolic.Differentiator;

/**
 * Class representing a {@link RootBracketingMethod} that uses a mixture of the
 * bisection and Newton Raphson's algorithms to find the roots of the target
 * {@link Function} between a set of bounds
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
public final class NewtonBisectionSolver extends RootBracketingMethod {
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
     * Construct a new {@link NewtonBisectionSolver} with the specified target
     * {@link Function}
     * <p>
     * This constructor will set the object to use numerical differentiation
     *
     * @param targetFunction The function to estimate the roots of
     */
    public NewtonBisectionSolver(Function targetFunction) {
        super(targetFunction);
        this.setDifferentiationMethod(DifferentiationMethod.Numerical);
    }

    /**
     * Construct a new {@link NewtonBisectionSolver} with the specified target
     * {@link Function} and derivative of the target {@link Function}
     *
     * @param targetFunction     The function to estimate the roots of
     * @param derivativeFunction The derivative of the target function
     */
    public NewtonBisectionSolver(Function targetFunction, Function derivativeFunction) {
        super(targetFunction);
        this.setDerivativefunction(derivativeFunction);
        this.setDifferentiationMethod(DifferentiationMethod.Predefined);
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
     * Estimate the root of the target {@link Function} between the upper and
     * lower bounds through a mixture of the bisection and Newton Raphson's
     * algorithms
     * <p>
     * We first consider the Newton-Raphson step. If that would predict a next
     * point that is outside of our bracketed range, then we do a bisection step
     * instead by choosing the midpoint of the range to be the next point. We
     * then evaluate the function at the next point and, depending on the sign
     * of that evaluation, replace one of the bounding points with the new
     * point. This keeps the root bracketed, while allowing us to benefit from
     * the speed of Newton-Raphson.
     * <p>
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If a root was not found within the required number of
     *                                  iterations within the specified tolerance
     */
    @Override
    public double solve() {
        super.checkRootFindingParams();

        Function f = this.targetFunction;

        double a = upperBound;
        double b = lowerBound;

        double fa = f.evaluateAt(a);

        // Calculate the midpoint of the bracket
        double x = 0.5 * (a + b);

        int iterations = 1;

        while (iterations <= this.iterations) {
            double fx = f.evaluateAt(x);

            // Return the current estimation if it is within the set tolerance
            // and the convergence criteria is set to accept this
            if (Math.abs(fx) < tolerance
                    && convergenceCriteria == ConvergenceCriteria.WithinTolerance) {
                return x;
            }

            // Bisection step
            if (fa * fx < 0.0D) {
                b = x;
            } else {
                a = x;
            }

            if (Double.isInfinite(x))
                throw new RuntimeException("Algorithm has diverged");

            double dfx = evaluateDerivativeAt(x);

            // Newton Raphson step
            double dx = -fx / dfx;

            x = x + dx;

            // If it predicts a point outside of the range, then bisect instead
            if ((b - x) * (x - a) < 0.0D) {
                dx = 0.5 * (b - a);
                x = a + dx;
            }

            // Return the current estimation if it is within the set tolerance
            // and the convergence criteria is set to accept this
            if (Math.abs(dx) < tolerance
                    && convergenceCriteria == ConvergenceCriteria.WithinTolerance) {
                return x;
            }

            iterations++;
        }

        // If the number of iterations has been used and the convergence
        // criteria is set to accept this, return the estimation
        if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations) {
            return x;
        }

        throw new UnsupportedOperationException(
                "Unable to find the root within specified tolerance after " + iterations
                        + " iterations");
    }
}
