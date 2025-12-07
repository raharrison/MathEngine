package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Class representing an {@link IntegrationMethod} that uses trapezia to
 * estimate the integral
 *
 * @author Ryan Harrison
 *
 */
public class TrapeziumIntegrator extends IntegrationMethod {
    /**
     * Construct a new {@link TrapeziumIntegrator} with the specified target
     * {@link Function}
     *
     * @param function The function to estimate the integral for
     */
    public TrapeziumIntegrator(Function function) {
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

        double sum = 0.0;

        for (int i = 1; i < iterations; i++) {
            double x = lower + range * i / iterations;
            sum += f.evaluateAt(x);
        }

        sum += (f.evaluateAt(lower) + f.evaluateAt(upper)) / 2.0;
        return sum * range / iterations;
    }

}
