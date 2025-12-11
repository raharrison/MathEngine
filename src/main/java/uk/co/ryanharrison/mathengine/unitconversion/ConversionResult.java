package uk.co.ryanharrison.mathengine.unitconversion;

import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.units.Unit;

import java.util.Objects;

/**
 * Immutable result of a unit conversion operation.
 * <p>
 * A conversion result contains the source unit, target unit, input value,
 * converted result, and the unit group name. Results can be either successful
 * (all fields populated) or partial (when units are not found or incompatible).
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * ConversionEngine engine = ConversionEngine.loadDefaults();
 * ConversionResult result = engine.convert(100, "meters", "feet");
 *
 * if (result.isSuccessful()) {
 *     System.out.println(result.result()); // BigRational result
 *     System.out.println(result.fromUnit().getSingular()); // "meter"
 *     System.out.println(result.toUnit().getSingular()); // "foot"
 *     System.out.println(result.groupName()); // "length"
 * }
 * }</pre>
 *
 * @param fromUnit   the source unit
 * @param toUnit     the target unit
 * @param inputValue the original input value
 * @param result     the converted result (null for partial/failed conversions)
 * @param groupName  the name of the unit group (null for partial/failed conversions)
 */
public record ConversionResult(Unit fromUnit, Unit toUnit, BigRational inputValue, BigRational result, String groupName) {

    /**
     * Creates a successful conversion result with all fields populated.
     *
     * @param fromUnit   the source unit
     * @param toUnit     the target unit
     * @param inputValue the original input value
     * @param result     the converted result
     * @param groupName  the name of the unit group
     * @return a successful conversion result
     * @throws NullPointerException if any parameter is null
     */
    public static ConversionResult success(Unit fromUnit, Unit toUnit, BigRational inputValue,
                                           BigRational result, String groupName) {
        Objects.requireNonNull(fromUnit, "From unit cannot be null");
        Objects.requireNonNull(toUnit, "To unit cannot be null");
        Objects.requireNonNull(inputValue, "Input value cannot be null");
        Objects.requireNonNull(result, "Result cannot be null");
        Objects.requireNonNull(groupName, "Group name cannot be null");
        return new ConversionResult(fromUnit, toUnit, inputValue, result, groupName);
    }

    /**
     * Creates a partial conversion result when the conversion cannot be completed.
     * <p>
     * Partial results occur when:
     * </p>
     * <ul>
     *     <li>Source unit is not found (fromUnit is null)</li>
     *     <li>Target unit is not found (toUnit is null)</li>
     *     <li>Units are incompatible</li>
     * </ul>
     *
     * @param fromUnit   the source unit (may be null)
     * @param toUnit     the target unit (may be null)
     * @param inputValue the original input value
     * @return a partial conversion result
     */
    public static ConversionResult partial(Unit fromUnit, Unit toUnit, BigRational inputValue) {
        return new ConversionResult(fromUnit, toUnit, inputValue, null, null);
    }

    /**
     * Checks if this conversion was successful.
     *
     * @return true if the conversion succeeded (result is not null), false otherwise
     */
    public boolean isSuccessful() {
        return result != null;
    }

    @Override
    public String toString() {
        if (!isSuccessful()) {
            return "Conversion failed";
        }

        double absResult = result.abs().doubleValue();
        String fromForm = Math.abs(inputValue.doubleValue()) == 1.0
                ? fromUnit.getSingular()
                : fromUnit.getPlural();
        String toForm = absResult == 1.0
                ? toUnit.getSingular()
                : toUnit.getPlural();

        return String.format("%s %s = %s %s", inputValue.doubleValue(), fromForm, result.doubleValue(), toForm);
    }

}
