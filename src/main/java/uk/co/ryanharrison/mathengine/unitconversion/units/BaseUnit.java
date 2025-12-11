package uk.co.ryanharrison.mathengine.unitconversion.units;

import java.util.*;

/**
 * Abstract base class for {@link Unit} implementations.
 * <p>
 * Provides common functionality for managing singular/plural forms and aliases,
 * with case-insensitive matching. Subclasses add specific conversion logic.
 * </p>
 *
 * @see SimpleUnit
 * @see ComplexUnit
 */
abstract class BaseUnit implements Unit {
    private final String singular;
    private final String plural;
    private final List<String> aliases;

    protected BaseUnit(String singular, String plural, List<String> aliases) {
        Objects.requireNonNull(singular, "Singular form cannot be null");
        Objects.requireNonNull(plural, "Plural form cannot be null");
        Objects.requireNonNull(aliases, "Aliases list cannot be null");

        this.singular = singular;
        this.plural = plural;
        this.aliases = List.copyOf(aliases);
    }

    @Override
    public final String getSingular() {
        return singular;
    }

    @Override
    public final String getPlural() {
        return plural;
    }

    @Override
    public final Set<String> getAllAliases() {
        Set<String> allAliases = new HashSet<>();
        allAliases.add(singular);
        if (!singular.equalsIgnoreCase(plural)) {
            allAliases.add(plural);
        }
        allAliases.addAll(aliases);
        return Collections.unmodifiableSet(allAliases);
    }

    @Override
    public final boolean matches(String input) {
        if (input == null) {
            return false;
        }
        String normalized = input.toLowerCase();
        return singular.equalsIgnoreCase(normalized)
                || plural.equalsIgnoreCase(normalized)
                || aliases.stream().anyMatch(alias -> alias.equalsIgnoreCase(normalized));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUnit that)) return false;
        return singular.equals(that.singular)
                && plural.equals(that.plural)
                && aliases.equals(that.aliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(singular, plural, aliases);
    }

    @Override
    public String toString() {
        return plural;
    }
}