package uk.co.ryanharrison.mathengine.unitconversion.units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ComplexUnitTest {

    // ==================== Construction ====================

    @Test
    void builderCreatesUnitWithAllProperties() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .plural("celsius")
                .alias("c")
                .alias("cel")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 9/5 + 32")
                .conversionEquation("kelvin", "c + 273.15")
                .build();

        assertThat(unit.getSingular()).isEqualTo("celsius");
        assertThat(unit.getPlural()).isEqualTo("celsius");
        assertThat(unit.getVariable()).isEqualTo("c");
        assertThat(unit.getAllAliases()).contains("celsius", "c", "cel");
    }

    @Test
    void builderDefaultsPluralToSingularIfNotSet() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .build();

        assertThat(unit.getSingular()).isEqualTo("celsius");
        assertThat(unit.getPlural()).isEqualTo("celsius");
    }

    @Test
    void builderDefaultsVariableToX() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .build();

        assertThat(unit.getVariable()).isEqualTo("x");
    }

    @Test
    void builderRejectsMissingSingular() {
        assertThatThrownBy(() -> ComplexUnit.builder().build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Singular form is required");
    }

    @Test
    void builderIgnoresNullAndEmptyConversionEquations() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .conversionEquation(null, "c * 9/5 + 32")
                .conversionEquation("fahrenheit", null)
                .conversionEquation("", "c + 273.15")
                .conversionEquation("kelvin", "")
                .conversionEquation("rankine", "c * 9/5 + 491.67")
                .build();

        ComplexUnit targetUnit = ComplexUnit.builder()
                .singular("rankine")
                .build();

        assertThat(unit.getConversionEquationFor(targetUnit)).isPresent();
    }

    @Test
    void builderIgnoresNullAndEmptyAliases() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .alias(null)
                .alias("")
                .alias("c")
                .build();

        assertThat(unit.getAllAliases()).contains("celsius", "c");
        assertThat(unit.getAllAliases().size()).isEqualTo(2);
    }

    @Test
    void builderIgnoresNullAndEmptyVariable() {
        ComplexUnit unit1 = ComplexUnit.builder()
                .singular("celsius")
                .variable(null)
                .build();

        ComplexUnit unit2 = ComplexUnit.builder()
                .singular("celsius")
                .variable("")
                .build();

        assertThat(unit1.getVariable()).isEqualTo("x");
        assertThat(unit2.getVariable()).isEqualTo("x");
    }

    // ==================== Getters and Basic Properties ====================

    @Test
    void getSingularReturnsCorrectValue() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .plural("celsius")
                .build();

        assertThat(unit.getSingular()).isEqualTo("celsius");
    }

    @Test
    void getPluralReturnsCorrectValue() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .plural("celsius")
                .build();

        assertThat(unit.getPlural()).isEqualTo("celsius");
    }

    @Test
    void getVariableReturnsCorrectValue() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .build();

        assertThat(unit.getVariable()).isEqualTo("c");
    }

    // ==================== Matching ====================

    @ParameterizedTest
    @CsvSource({
            "celsius, celsius",
            "celsius, CELSIUS",
            "celsius, Celsius",
            "celsius, c",
            "celsius, C"
    })
    void matchesIsCaseInsensitive(String input, String toMatch) {
        ComplexUnit unit = ComplexUnit.builder()
                .singular(input)
                .alias(input.substring(0, 1))
                .build();

        assertThat(unit.matches(toMatch)).isTrue();
    }

    @Test
    void matchesReturnsFalseForNonMatchingInput() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .alias("c")
                .build();

        assertThat(unit.matches("fahrenheit")).isFalse();
        assertThat(unit.matches("f")).isFalse();
        assertThat(unit.matches("kelvin")).isFalse();
    }

    @Test
    void matchesReturnsFalseForNull() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .build();

        assertThat(unit.matches(null)).isFalse();
    }

    // ==================== Conversion Equations ====================

    @Test
    void getConversionEquationForReturnsEquationWhenTargetMatches() {
        ComplexUnit celsius = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 9/5 + 32")
                .conversionEquation("kelvin", "c + 273.15")
                .build();

        ComplexUnit fahrenheit = ComplexUnit.builder()
                .singular("fahrenheit")
                .plural("fahrenheit")
                .build();

        Optional<String> equation = celsius.getConversionEquationFor(fahrenheit);

        assertThat(equation).isPresent();
        assertThat(equation.get()).isEqualTo("c * 9/5 + 32");
    }

    @Test
    void getConversionEquationForMatchesTargetAliases() {
        ComplexUnit celsius = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .conversionEquation("f", "c * 9/5 + 32")
                .build();

        ComplexUnit fahrenheit = ComplexUnit.builder()
                .singular("fahrenheit")
                .plural("fahrenheit")
                .alias("f")
                .build();

        Optional<String> equation = celsius.getConversionEquationFor(fahrenheit);

        assertThat(equation).isPresent();
        assertThat(equation.get()).isEqualTo("c * 9/5 + 32");
    }

    @Test
    void getConversionEquationForIsCaseInsensitive() {
        ComplexUnit celsius = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .conversionEquation("FAHRENHEIT", "c * 9/5 + 32")
                .build();

        ComplexUnit fahrenheit = ComplexUnit.builder()
                .singular("fahrenheit")
                .build();

        Optional<String> equation = celsius.getConversionEquationFor(fahrenheit);

        assertThat(equation).isPresent();
        assertThat(equation.get()).isEqualTo("c * 9/5 + 32");
    }

    @Test
    void getConversionEquationForReturnsEmptyWhenNoEquationExists() {
        ComplexUnit celsius = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 9/5 + 32")
                .build();

        ComplexUnit rankine = ComplexUnit.builder()
                .singular("rankine")
                .build();

        Optional<String> equation = celsius.getConversionEquationFor(rankine);

        assertThat(equation).isEmpty();
    }

    @Test
    void getConversionEquationForReturnsFirstMatchingAlias() {
        ComplexUnit celsius = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .conversionEquation("f", "c * 9/5 + 32")
                .conversionEquation("fahrenheit", "c * 1.8 + 32")
                .build();

        ComplexUnit fahrenheit = ComplexUnit.builder()
                .singular("fahrenheit")
                .alias("f")
                .build();

        Optional<String> equation = celsius.getConversionEquationFor(fahrenheit);

        assertThat(equation).isPresent();
        // Should match one of the equations (order depends on getAllAliases iteration)
        assertThat(equation.get()).matches("c \\* (9/5|1\\.8) \\+ 32");
    }

    // ==================== Equality and HashCode ====================

    @Test
    void equalsReturnsTrueForIdenticalUnits() {
        ComplexUnit unit1 = ComplexUnit.builder()
                .singular("celsius")
                .plural("celsius")
                .alias("c")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 9/5 + 32")
                .build();

        ComplexUnit unit2 = ComplexUnit.builder()
                .singular("celsius")
                .plural("celsius")
                .alias("c")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 9/5 + 32")
                .build();

        assertThat(unit1).isEqualTo(unit2);
        assertThat(unit1.hashCode()).isEqualTo(unit2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentConversionEquations() {
        ComplexUnit unit1 = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 9/5 + 32")
                .build();

        ComplexUnit unit2 = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 1.8 + 32")
                .build();

        assertThat(unit1).isNotEqualTo(unit2);
    }

    @Test
    void equalsReturnsFalseForDifferentVariables() {
        ComplexUnit unit1 = ComplexUnit.builder()
                .singular("celsius")
                .variable("c")
                .build();

        ComplexUnit unit2 = ComplexUnit.builder()
                .singular("celsius")
                .variable("x")
                .build();

        assertThat(unit1).isNotEqualTo(unit2);
    }

    @Test
    void equalsReturnsFalseForDifferentSingulars() {
        ComplexUnit unit1 = ComplexUnit.builder()
                .singular("celsius")
                .build();

        ComplexUnit unit2 = ComplexUnit.builder()
                .singular("fahrenheit")
                .build();

        assertThat(unit1).isNotEqualTo(unit2);
    }

    // ==================== toString ====================

    @Test
    void toStringReturnsPluralForm() {
        ComplexUnit unit = ComplexUnit.builder()
                .singular("celsius")
                .plural("celsius")
                .build();

        assertThat(unit.toString()).isEqualTo("celsius");
    }
}
