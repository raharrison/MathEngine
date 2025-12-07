package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

/**
 * {@link NumericalDifferentiationMethod} to increase the accuracy of the
 * {@link DividedDifferenceMethod} through Richardson's Extrapolation
 *
 * @author Ryan Harrison
 *
 */
public class RichardsonExtrapolationMethod extends DividedDifferenceMethod {
    /**
     * Create a new {@link RichardsonExtrapolationMethod} instance with the
     * specified target function
     *
     * @param function The target function to numerically estimate the derivatives of
     */
    public RichardsonExtrapolationMethod(Function function) {
        super(function, DifferencesDirection.Central);
    }

    /**
     * Construct a new {@link RichardsonExtrapolationMethod} instance with the
     * specified target {@link Function} and differences direction
     * <p>
     * This constructor will default the {@link DifferencesDirection} to Central
     *
     * @param function  The target function to numerically estimate the derivatives of
     * @param direction The direction of the differences to use
     */
    public RichardsonExtrapolationMethod(Function function, DifferencesDirection direction) {
        super(function, direction);

    }

    /**
     * Construct a new {@link RichardsonExtrapolationMethod} instance with the
     * specified target {@link Function}, difference direction and change in x
     * <p>
     * This constructor will default the {@link DifferencesDirection} to Central
     *
     * @param function  The target function to numerically estimate the derivatives of
     * @param direction The direction of the differences to use
     * @param h         The change in the value of x to use when numerically
     *                  estimating the derivative using finite difference
     *                  approximations. This value should be sufficiently small to
     *                  increase accuracy but large enough to prevent rounding errors
     */
    public RichardsonExtrapolationMethod(Function function, DifferencesDirection direction, double h) {
        super(function, direction, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double deriveFirst() {
        double d1 = super.deriveFirst();

        double oldH = getH();
        setH(oldH / 2);

        double d2 = super.deriveFirst();
        setH(oldH);

        return (4 * d2 - d1) / 3.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double deriveFourth() {
        double d1 = super.deriveFourth();

        double oldH = getH();
        setH(oldH / 2);

        double d2 = super.deriveFourth();
        setH(oldH);

        return (4 * d2 - d1) / 3.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double deriveSecond() {
        double d1 = super.deriveSecond();

        double oldH = getH();
        setH(oldH / 2);

        double d2 = super.deriveSecond();
        setH(oldH);

        return (4 * d2 - d1) / 3.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double deriveThird() {
        double d1 = super.deriveThird();

        double oldH = getH();
        setH(oldH / 2);

        double d2 = super.deriveThird();
        setH(oldH);

        return (4 * d2 - d1) / 3.0;
    }

}
