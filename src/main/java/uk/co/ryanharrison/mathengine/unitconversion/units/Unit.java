package uk.co.ryanharrison.mathengine.unitconversion.units;

import java.util.Set;

/**
 * Represents a unit of measurement with singular/plural forms and aliases.
 * <p>
 * Units are the fundamental building blocks of the conversion system. Each unit
 * has a singular form (e.g., "meter"), a plural form (e.g., "meters"), and
 * optionally additional aliases for flexible matching.
 * </p>
 *
 * @see SimpleUnit
 * @see ComplexUnit
 */
public interface Unit {
    /**
     * Returns the singular form of this unit.
     *
     * @return the singular form (e.g., "meter", "foot")
     */
    String getSingular();

    /**
     * Returns the plural form of this unit.
     *
     * @return the plural form (e.g., "meters", "feet")
     */
    String getPlural();

    /**
     * Returns all aliases for this unit including singular, plural, and custom aliases.
     *
     * @return unmodifiable set of all unit aliases
     */
    Set<String> getAllAliases();

    /**
     * Checks if the given input string matches this unit.
     * <p>
     * Matching is case-insensitive and checks against the singular form,
     * plural form, and all registered aliases.
     * </p>
     *
     * @param input the string to match
     * @return true if input matches this unit, false otherwise
     */
    boolean matches(String input);
}
