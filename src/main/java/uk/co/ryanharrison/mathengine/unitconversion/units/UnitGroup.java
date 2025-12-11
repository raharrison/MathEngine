package uk.co.ryanharrison.mathengine.unitconversion.units;

import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;

import java.util.List;
import java.util.Set;

/**
 * A collection of related units with conversion capabilities.
 * <p>
 * Unit groups organize units of the same measurement type (e.g., length, mass, temperature)
 * and provide conversion logic between them.  Different implementations support different
 * conversion strategies (linear factors, formulas, dynamic rates).
 * </p>
 *
 * @param <T> the specific unit type managed by this group
 * @see SimpleUnitGroup
 * @see ComplexUnitGroup
 */
public interface UnitGroup<T extends Unit> {
    /**
     * Returns the name of this unit group.
     *
     * @return the group name (e.g., "length", "mass", "currency")
     */
    String getName();

    /**
     * Returns all units in this group.
     *
     * @return immutable list of units
     */
    List<T> getUnits();

    /**
     * Returns the plural names of all units in this group.
     *
     * @return unmodifiable set of unit names
     */
    Set<String> getAllUnitNames();

    /**
     * Returns all unit aliases (singular, plural, and custom) in this group.
     *
     * @return unmodifiable set of all aliases
     */
    Set<String> getAllAliases();

    /**
     * Converts an amount from one unit to another within this group.
     *
     * @param amount the amount to convert
     * @param from   the source unit name
     * @param to     the target unit name
     * @return the conversion result (may be partial if units not found)
     */
    ConversionResult convert(BigRational amount, String from, String to);

    /**
     * Updates dynamic data for this unit group (if applicable).
     * <p>
     * For example, currency groups update exchange rates, timezone groups update offset data.
     * Groups with static data have a no-op implementation.
     * </p>
     */
    void update();
}
