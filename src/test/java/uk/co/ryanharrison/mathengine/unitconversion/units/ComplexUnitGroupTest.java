package uk.co.ryanharrison.mathengine.unitconversion.units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class ComplexUnitGroupTest {

    // ==================== Construction ====================

    @Test
    void ofCreatesGroupWithUnits() {
        ComplexUnit celsius = createCelsius();
        ComplexUnit fahrenheit = createFahrenheit();

        ComplexUnitGroup group = ComplexUnitGroup.of("temperature", List.of(celsius, fahrenheit));

        assertThat(group.getName()).isEqualTo("temperature");
        assertThat(group.getUnits()).containsExactly(celsius, fahrenheit);
    }

    // ==================== Getters ====================

    @Test
    void getNameReturnsCorrectName() {
        ComplexUnitGroup group = createTemperatureGroup();
        assertThat(group.getName()).isEqualTo("temperature");
    }

    @Test
    void getUnitsReturnsAllUnits() {
        ComplexUnit celsius = createCelsius();
        ComplexUnit fahrenheit = createFahrenheit();
        ComplexUnitGroup group = ComplexUnitGroup.of("temperature", List.of(celsius, fahrenheit));

        assertThat(group.getUnits()).containsExactly(celsius, fahrenheit);
    }

    @Test
    void getAllUnitNamesReturnsPluralForms() {
        ComplexUnitGroup group = createTemperatureGroup();

        Set<String> names = group.getAllUnitNames();

        assertThat(names).contains("celsius", "fahrenheit", "kelvin");
    }

    @Test
    void getAllAliasesReturnsAllAliasesFromAllUnits() {
        ComplexUnitGroup group = createTemperatureGroup();

        Set<String> aliases = group.getAllAliases();

        assertThat(aliases).contains(
                "celsius", "c",
                "fahrenheit", "f",
                "kelvin", "k"
        );
    }

    // ==================== Conversion - Success ====================

    @ParameterizedTest
    @CsvSource({
            "0, celsius, fahrenheit, 32.0",
            "100, celsius, fahrenheit, 212.0",
            "32, fahrenheit, celsius, 0.0",
            "212, fahrenheit, celsius, 100.0",
            "0, celsius, kelvin, 273.15",
            "273.15, kelvin, celsius, 0.0"
    })
    void convertPerformsCorrectConversions(double amount, String from, String to, double expected) {
        ComplexUnitGroup group = createTemperatureGroup();

        ConversionResult result = group.convert(BigRational.of(amount), from, to);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.result().doubleValue()).isCloseTo(expected, within(0.01));
        assertThat(result.groupName()).isEqualTo("temperature");
    }

    @Test
    void convertIsCaseInsensitive() {
        ComplexUnitGroup group = createTemperatureGroup();

        ConversionResult result1 = group.convert(BigRational.of(0), "CELSIUS", "FAHRENHEIT");
        ConversionResult result2 = group.convert(BigRational.of(0), "Celsius", "Fahrenheit");
        ConversionResult result3 = group.convert(BigRational.of(0), "celsius", "fahrenheit");

        assertThat(result1.isSuccessful()).isTrue();
        assertThat(result2.isSuccessful()).isTrue();
        assertThat(result3.isSuccessful()).isTrue();
        assertThat(result1.result().doubleValue()).isCloseTo(32.0, within(0.01));
        assertThat(result2.result().doubleValue()).isCloseTo(32.0, within(0.01));
        assertThat(result3.result().doubleValue()).isCloseTo(32.0, within(0.01));
    }

    @Test
    void convertMatchesAgainstAliases() {
        ComplexUnitGroup group = createTemperatureGroup();

        ConversionResult result = group.convert(BigRational.of(0), "c", "f");

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.fromUnit().getSingular()).isEqualTo("celsius");
        assertThat(result.toUnit().getSingular()).isEqualTo("fahrenheit");
    }

    @Test
    void convertFromUnitToItselfReturnsInputValue() {
        ComplexUnitGroup group = createTemperatureGroup();

        ConversionResult result = group.convert(BigRational.of(100), "celsius", "celsius");

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.result()).isEqualTo(BigRational.of(100));
    }

    @Test
    void convertWithNoEquationReturnsInputValueUnchanged() {
        ComplexUnit unit1 = ComplexUnit.builder()
                .singular("unit1")
                .variable("x")
                .build();

        ComplexUnit unit2 = ComplexUnit.builder()
                .singular("unit2")
                .variable("y")
                .build();

        ComplexUnitGroup group = ComplexUnitGroup.of("test", List.of(unit1, unit2));

        ConversionResult result = group.convert(BigRational.of(100), "unit1", "unit2");

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.result()).isEqualTo(BigRational.of(100));
    }

    // ==================== Conversion - Partial Results ====================

    @Test
    void convertReturnsPartialWhenFromUnitNotFound() {
        ComplexUnitGroup group = createTemperatureGroup();

        ConversionResult result = group.convert(BigRational.of(100), "rankine", "fahrenheit");

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.fromUnit()).isNull();
        assertThat(result.toUnit()).isNull();
        assertThat(result.inputValue()).isEqualTo(BigRational.of(100));
    }

    @Test
    void convertReturnsPartialWhenToUnitNotFound() {
        ComplexUnitGroup group = createTemperatureGroup();

        ConversionResult result = group.convert(BigRational.of(100), "celsius", "rankine");

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.fromUnit()).isNotNull();
        assertThat(result.fromUnit().getSingular()).isEqualTo("celsius");
        assertThat(result.toUnit()).isNull();
        assertThat(result.inputValue()).isEqualTo(BigRational.of(100));
    }

    // ==================== Update ====================

    @Test
    void updateDoesNothingForComplexUnitGroup() {
        ComplexUnitGroup group = createTemperatureGroup();
        List<ComplexUnit> beforeUnits = group.getUnits();

        group.update();

        assertThat(group.getUnits()).isEqualTo(beforeUnits);
    }

    // ==================== Equality and HashCode ====================

    @Test
    void equalsReturnsTrueForIdenticalGroups() {
        ComplexUnit celsius = createCelsius();
        ComplexUnit fahrenheit = createFahrenheit();

        ComplexUnitGroup group1 = ComplexUnitGroup.of("temperature", List.of(celsius, fahrenheit));
        ComplexUnitGroup group2 = ComplexUnitGroup.of("temperature", List.of(celsius, fahrenheit));

        assertThat(group1).isEqualTo(group2);
        assertThat(group1.hashCode()).isEqualTo(group2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentNames() {
        ComplexUnit celsius = createCelsius();

        ComplexUnitGroup group1 = ComplexUnitGroup.of("temperature", List.of(celsius));
        ComplexUnitGroup group2 = ComplexUnitGroup.of("heat", List.of(celsius));

        assertThat(group1).isNotEqualTo(group2);
    }

    @Test
    void equalsReturnsFalseForDifferentUnits() {
        ComplexUnit celsius = createCelsius();
        ComplexUnit fahrenheit = createFahrenheit();

        ComplexUnitGroup group1 = ComplexUnitGroup.of("temperature", List.of(celsius));
        ComplexUnitGroup group2 = ComplexUnitGroup.of("temperature", List.of(fahrenheit));

        assertThat(group1).isNotEqualTo(group2);
    }

    // ==================== toString ====================

    @Test
    void toStringReturnsGroupName() {
        ComplexUnitGroup group = createTemperatureGroup();
        assertThat(group.toString()).isEqualTo("temperature");
    }

    // ==================== Helper Methods ====================

    private ComplexUnitGroup createTemperatureGroup() {
        return ComplexUnitGroup.of("temperature", List.of(
                createCelsius(),
                createFahrenheit(),
                createKelvin()
        ));
    }

    private ComplexUnit createCelsius() {
        return ComplexUnit.builder()
                .singular("celsius")
                .plural("celsius")
                .alias("c")
                .variable("c")
                .conversionEquation("fahrenheit", "c * 9/5 + 32")
                .conversionEquation("f", "c * 9/5 + 32")
                .conversionEquation("kelvin", "c + 273.15")
                .conversionEquation("k", "c + 273.15")
                .build();
    }

    private ComplexUnit createFahrenheit() {
        return ComplexUnit.builder()
                .singular("fahrenheit")
                .plural("fahrenheit")
                .alias("f")
                .variable("f")
                .conversionEquation("celsius", "(f - 32) * 5/9")
                .conversionEquation("c", "(f - 32) * 5/9")
                .conversionEquation("kelvin", "(f - 32) * 5/9 + 273.15")
                .conversionEquation("k", "(f - 32) * 5/9 + 273.15")
                .build();
    }

    private ComplexUnit createKelvin() {
        return ComplexUnit.builder()
                .singular("kelvin")
                .plural("kelvin")
                .alias("k")
                .variable("k")
                .conversionEquation("celsius", "k - 273.15")
                .conversionEquation("c", "k - 273.15")
                .conversionEquation("fahrenheit", "(k - 273.15) * 9/5 + 32")
                .conversionEquation("f", "(k - 273.15) * 9/5 + 32")
                .build();
    }
}
