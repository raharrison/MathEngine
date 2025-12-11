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

class SimpleUnitGroupTest {

    // ==================== Construction ====================

    @Test
    void ofCreatesGroupWithUnits() {
        SimpleUnit meter = createUnit("meter", "meters", 1.0);
        SimpleUnit foot = createUnit("foot", "feet", 0.3048);

        SimpleUnitGroup group = SimpleUnitGroup.of("length", List.of(meter, foot));

        assertThat(group.getName()).isEqualTo("length");
        assertThat(group.getUnits()).containsExactly(meter, foot);
    }

    // ==================== Getters ====================

    @Test
    void getNameReturnsCorrectName() {
        SimpleUnitGroup group = createLengthGroup();
        assertThat(group.getName()).isEqualTo("length");
    }

    @Test
    void getUnitsReturnsAllUnits() {
        SimpleUnit meter = createUnit("meter", "meters", 1.0);
        SimpleUnit foot = createUnit("foot", "feet", 0.3048);
        SimpleUnitGroup group = SimpleUnitGroup.of("length", List.of(meter, foot));

        assertThat(group.getUnits()).containsExactly(meter, foot);
    }

    @Test
    void getAllUnitNamesReturnsPluralForms() {
        SimpleUnitGroup group = createLengthGroup();

        Set<String> names = group.getAllUnitNames();

        assertThat(names).contains("meters", "feet", "inches");
    }

    @Test
    void getAllAliasesReturnsAllAliasesFromAllUnits() {
        SimpleUnitGroup group = createLengthGroup();

        Set<String> aliases = group.getAllAliases();

        assertThat(aliases).contains(
                "meter", "meters", "m",
                "foot", "feet", "ft",
                "inch", "inches", "in"
        );
    }

    // ==================== Conversion - Success ====================

    @ParameterizedTest
    @CsvSource({
            "100, meters, feet, 328.084",
            "1, meters, feet, 3.28084",
            "10, feet, meters, 3.048",
            "12, inches, feet, 1.0",
            "1, feet, inches, 12.0"
    })
    void convertPerformsCorrectConversions(double amount, String from, String to, double expected) {
        SimpleUnitGroup group = createLengthGroup();

        ConversionResult result = group.convert(BigRational.of(amount), from, to);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.result().doubleValue()).isCloseTo(expected, within(0.001));
        assertThat(result.groupName()).isEqualTo("length");
    }

    @Test
    void convertIsCaseInsensitive() {
        SimpleUnitGroup group = createLengthGroup();

        ConversionResult result1 = group.convert(BigRational.of(100), "METERS", "FEET");
        ConversionResult result2 = group.convert(BigRational.of(100), "Meters", "Feet");
        ConversionResult result3 = group.convert(BigRational.of(100), "meters", "feet");

        assertThat(result1.isSuccessful()).isTrue();
        assertThat(result2.isSuccessful()).isTrue();
        assertThat(result3.isSuccessful()).isTrue();
        assertThat(result1.result()).isEqualTo(result2.result());
        assertThat(result2.result()).isEqualTo(result3.result());
    }

    @Test
    void convertMatchesAgainstAliases() {
        SimpleUnitGroup group = createLengthGroup();

        ConversionResult result = group.convert(BigRational.of(100), "m", "ft");

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.fromUnit().getSingular()).isEqualTo("meter");
        assertThat(result.toUnit().getSingular()).isEqualTo("foot");
    }

    @Test
    void convertFromUnitToItselfReturnsInputValue() {
        SimpleUnitGroup group = createLengthGroup();

        ConversionResult result = group.convert(BigRational.of(100), "meters", "meters");

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.result()).isEqualTo(BigRational.of(100));
    }

    // ==================== Conversion - Partial Results ====================

    @Test
    void convertReturnsPartialWhenFromUnitNotFound() {
        SimpleUnitGroup group = createLengthGroup();

        ConversionResult result = group.convert(BigRational.of(100), "kilometers", "feet");

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.fromUnit()).isNull();
        assertThat(result.toUnit()).isNull();
        assertThat(result.inputValue()).isEqualTo(BigRational.of(100));
    }

    @Test
    void convertReturnsPartialWhenToUnitNotFound() {
        SimpleUnitGroup group = createLengthGroup();

        ConversionResult result = group.convert(BigRational.of(100), "meters", "kilometers");

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.fromUnit()).isNotNull();
        assertThat(result.fromUnit().getSingular()).isEqualTo("meter");
        assertThat(result.toUnit()).isNull();
        assertThat(result.inputValue()).isEqualTo(BigRational.of(100));
    }

    // ==================== Update ====================

    @Test
    void updateDoesNothingForSimpleUnitGroup() {
        SimpleUnitGroup group = createLengthGroup();
        List<SimpleUnit> beforeUnits = group.getUnits();

        group.update();

        assertThat(group.getUnits()).isEqualTo(beforeUnits);
    }

    // ==================== Equality and HashCode ====================

    @Test
    void equalsReturnsTrueForIdenticalGroups() {
        SimpleUnit meter = createUnit("meter", "meters", 1.0);
        SimpleUnit foot = createUnit("foot", "feet", 0.3048);

        SimpleUnitGroup group1 = SimpleUnitGroup.of("length", List.of(meter, foot));
        SimpleUnitGroup group2 = SimpleUnitGroup.of("length", List.of(meter, foot));

        assertThat(group1).isEqualTo(group2);
        assertThat(group1.hashCode()).isEqualTo(group2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentNames() {
        SimpleUnit meter = createUnit("meter", "meters", 1.0);

        SimpleUnitGroup group1 = SimpleUnitGroup.of("length", List.of(meter));
        SimpleUnitGroup group2 = SimpleUnitGroup.of("distance", List.of(meter));

        assertThat(group1).isNotEqualTo(group2);
    }

    @Test
    void equalsReturnsFalseForDifferentUnits() {
        SimpleUnit meter = createUnit("meter", "meters", 1.0);
        SimpleUnit foot = createUnit("foot", "feet", 0.3048);

        SimpleUnitGroup group1 = SimpleUnitGroup.of("length", List.of(meter));
        SimpleUnitGroup group2 = SimpleUnitGroup.of("length", List.of(foot));

        assertThat(group1).isNotEqualTo(group2);
    }

    // ==================== toString ====================

    @Test
    void toStringReturnsGroupName() {
        SimpleUnitGroup group = createLengthGroup();
        assertThat(group.toString()).isEqualTo("length");
    }

    // ==================== Helper Methods ====================

    private SimpleUnitGroup createLengthGroup() {
        SimpleUnit meter = createUnit("meter", "meters", "m", 1.0);
        SimpleUnit foot = createUnit("foot", "feet", "ft", 0.3048);
        SimpleUnit inch = createUnit("inch", "inches", "in", 0.0254);
        return SimpleUnitGroup.of("length", List.of(meter, foot, inch));
    }

    private SimpleUnit createUnit(String singular, String plural, double conversionFactor) {
        return SimpleUnit.builder()
                .singular(singular)
                .plural(plural)
                .conversionFactor(conversionFactor)
                .build();
    }

    private SimpleUnit createUnit(String singular, String plural, String alias, double conversionFactor) {
        return SimpleUnit.builder()
                .singular(singular)
                .plural(plural)
                .alias(alias)
                .conversionFactor(conversionFactor)
                .build();
    }
}
