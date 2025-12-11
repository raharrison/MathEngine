package uk.co.ryanharrison.mathengine.unitconversion.units;

import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;

import java.util.List;

/**
 * A unit group for simple units with linear conversion factors.
 * <p>
 * Simple units are converted using multiplication and division of conversion factors.
 * For example, converting meters to feet: result = amount * meterFactor / footFactor.
 * </p>
 * <p>
 * This class is thread-safe for read operations. Subclasses that provide mutable
 * units (e.g., {@link uk.co.ryanharrison.mathengine.unitconversion.units.currency.Currency})
 * must handle thread safety for write operations.
 * </p>
 *
 * @see SimpleUnit
 * @see ComplexUnitGroup
 */
public class SimpleUnitGroup extends AbstractUnitGroup<SimpleUnit> {

    protected SimpleUnitGroup(String name, List<SimpleUnit> units) {
        super(name, units);
    }

    /**
     * Creates a new simple unit group with the specified name and units.
     *
     * @param name  the display name for this unit group
     * @param units the units to include in this group
     * @return a new simple unit group
     * @throws NullPointerException if name or units is null
     */
    public static SimpleUnitGroup of(String name, List<SimpleUnit> units) {
        return new SimpleUnitGroup(name, units);
    }

    @Override
    public ConversionResult convert(BigRational amount, String from, String to) {
        SimpleUnit fromUnit = findUnit(from);
        if (fromUnit == null) {
            return ConversionResult.partial(null, null, amount);
        }

        SimpleUnit toUnit = findUnit(to);
        if (toUnit == null) {
            return ConversionResult.partial(fromUnit, null, amount);
        }

        BigRational result = amount
                .multiply(fromUnit.getConversionFactor())
                .divide(toUnit.getConversionFactor());
        return ConversionResult.success(fromUnit, toUnit, amount, result, getName());
    }
}
