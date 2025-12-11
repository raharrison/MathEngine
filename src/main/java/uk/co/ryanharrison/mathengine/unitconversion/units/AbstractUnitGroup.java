package uk.co.ryanharrison.mathengine.unitconversion.units;

import java.util.*;

/**
 * Abstract base class for {@link UnitGroup} implementations, providing common functionality
 * for managing collections of units.
 * <p>
 * This class handles the standard unit group operations including:
 * </p>
 * <ul>
 *     <li>Unit storage and retrieval</li>
 *     <li>Unit name and alias management</li>
 *     <li>Unit lookup by string matching</li>
 *     <li>Standard object methods (equals, hashCode, toString)</li>
 * </ul>
 *
 * <p>
 * Subclasses must implement {@link #convert} to define conversion behavior specific
 * to their unit type (linear factor-based, formula-based, etc.).
 * </p>
 *
 * @param <T> the specific unit type managed by this group
 */
abstract class AbstractUnitGroup<T extends Unit> implements UnitGroup<T> {
    private final String name;
    private final List<T> units;

    /**
     * Constructs a new unit group with the specified name and units.
     *
     * @param name  the display name for this unit group (e.g., "length", "temperature")
     * @param units the immutable list of units in this group
     * @throws NullPointerException if name or units is null
     */
    AbstractUnitGroup(String name, List<T> units) {
        Objects.requireNonNull(name, "Group name cannot be null");
        Objects.requireNonNull(units, "Units list cannot be null");
        this.name = name;
        this.units = List.copyOf(units);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public List<T> getUnits() {
        return units;
    }

    @Override
    public Set<String> getAllUnitNames() {
        Set<String> names = new HashSet<>();
        for (T unit : getUnits()) {
            names.add(unit.getPlural());
        }
        return Collections.unmodifiableSet(names);
    }

    @Override
    public Set<String> getAllAliases() {
        Set<String> aliases = new HashSet<>();
        for (T unit : getUnits()) {
            aliases.addAll(unit.getAllAliases());
        }
        return Collections.unmodifiableSet(aliases);
    }

    /**
     * Finds a unit in this group by matching against the input string.
     * <p>
     * The search is case-insensitive and matches against the singular form,
     * plural form, and all registered aliases of each unit.
     * </p>
     *
     * @param input the string to match against unit names
     * @return the matching unit, or null if no match found
     */
    protected T findUnit(String input) {
        if (input == null) {
            return null;
        }
        for (T unit : getUnits()) {
            if (unit.matches(input)) {
                return unit;
            }
        }
        return null;
    }

    @Override
    public void update() {
        // Default implementation does nothing - override in subclasses that support updates
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractUnitGroup<?> that)) return false;
        return name.equals(that.name) && units.equals(that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, units);
    }

    @Override
    public String toString() {
        return name;
    }
}
