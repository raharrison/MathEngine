package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Class representing an {@link IntegrationMethod} that uses quadratic
 * interpolation to estimate the integral
 *
 * @author Ryan Harrison
 *
 */
public class SimpsonIntegrator extends IntegrationMethod {
    /**
     * Construct a new {@link SimpsonIntegrator} with the specified target
     * {@link Function}
     *
     * @param function The function to estimate the integral for
     */
    public SimpsonIntegrator(Function function) {
        super(function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double integrate() {
        Function f = this.targetFunction;

        double lower = this.lower;
        double upper = this.upper;

        double range = upper - lower;
        double sum1 = f.evaluateAt(lower + range / (iterations * 2.0));
        double sum2 = 0.0;

        for (int i = 1; i < iterations; i++) {
            double x1 = lower + range * (i + 0.5) / iterations;
            sum1 += f.evaluateAt(x1);
            double x2 = lower + range * i / iterations;
            sum2 += f.evaluateAt(x2);
        }

        return (f.evaluateAt(lower) + f.evaluateAt(upper) + sum1 * 4.0 + sum2 * 2.0) * range
                / (iterations * 6.0);
    }

}
