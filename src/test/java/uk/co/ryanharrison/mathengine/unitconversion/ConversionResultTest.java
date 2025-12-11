package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConversionResultTest {

    // ==================== Construction - Success ====================

    @Test
    void successCreatesResultWithAllFields() {
        SimpleUnit fromUnit = createUnit("meter", "meters", 1.0);
        SimpleUnit toUnit = createUnit("foot", "feet", 0.3048);
        BigRational input = BigRational.of(100);
        BigRational result = BigRational.of(328.084);
        String groupName = "length";

        ConversionResult conversionResult = ConversionResult.success(
                fromUnit, toUnit, input, result, groupName
        );

        assertThat(conversionResult.fromUnit()).isEqualTo(fromUnit);
        assertThat(conversionResult.toUnit()).isEqualTo(toUnit);
        assertThat(conversionResult.inputValue()).isEqualTo(input);
        assertThat(conversionResult.result()).isEqualTo(result);
        assertThat(conversionResult.groupName()).isEqualTo(groupName);
        assertThat(conversionResult.isSuccessful()).isTrue();
    }

    @Test
    void successRejectsNullFromUnit() {
        assertThatThrownBy(() -> ConversionResult.success(
                null,
                createUnit("foot", "feet", 0.3048),
                BigRational.of(100),
                BigRational.of(328.084),
                "length"
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("From unit");
    }

    @Test
    void successRejectsNullToUnit() {
        assertThatThrownBy(() -> ConversionResult.success(
                createUnit("meter", "meters", 1.0),
                null,
                BigRational.of(100),
                BigRational.of(328.084),
                "length"
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("To unit");
    }

    @Test
    void successRejectsNullInputValue() {
        assertThatThrownBy(() -> ConversionResult.success(
                createUnit("meter", "meters", 1.0),
                createUnit("foot", "feet", 0.3048),
                null,
                BigRational.of(328.084),
                "length"
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Input value");
    }

    @Test
    void successRejectsNullResult() {
        assertThatThrownBy(() -> ConversionResult.success(
                createUnit("meter", "meters", 1.0),
                createUnit("foot", "feet", 0.3048),
                BigRational.of(100),
                null,
                "length"
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Result");
    }

    @Test
    void successRejectsNullGroupName() {
        assertThatThrownBy(() -> ConversionResult.success(
                createUnit("meter", "meters", 1.0),
                createUnit("foot", "feet", 0.3048),
                BigRational.of(100),
                BigRational.of(328.084),
                null
        )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Group name");
    }

    // ==================== Construction - Partial ====================

    @Test
    void partialCreatesResultWithNullResultAndGroupName() {
        SimpleUnit fromUnit = createUnit("meter", "meters", 1.0);
        SimpleUnit toUnit = createUnit("foot", "feet", 0.3048);
        BigRational input = BigRational.of(100);

        ConversionResult conversionResult = ConversionResult.partial(fromUnit, toUnit, input);

        assertThat(conversionResult.fromUnit()).isEqualTo(fromUnit);
        assertThat(conversionResult.toUnit()).isEqualTo(toUnit);
        assertThat(conversionResult.inputValue()).isEqualTo(input);
        assertThat(conversionResult.result()).isNull();
        assertThat(conversionResult.groupName()).isNull();
        assertThat(conversionResult.isSuccessful()).isFalse();
    }

    @Test
    void partialAllowsNullFromUnit() {
        SimpleUnit toUnit = createUnit("foot", "feet", 0.3048);
        BigRational input = BigRational.of(100);

        ConversionResult conversionResult = ConversionResult.partial(null, toUnit, input);

        assertThat(conversionResult.fromUnit()).isNull();
        assertThat(conversionResult.isSuccessful()).isFalse();
    }

    @Test
    void partialAllowsNullToUnit() {
        SimpleUnit fromUnit = createUnit("meter", "meters", 1.0);
        BigRational input = BigRational.of(100);

        ConversionResult conversionResult = ConversionResult.partial(fromUnit, null, input);

        assertThat(conversionResult.toUnit()).isNull();
        assertThat(conversionResult.isSuccessful()).isFalse();
    }

    // ==================== Status Check ====================

    @Test
    void isSuccessfulReturnsTrueForSuccessResults() {
        ConversionResult result = ConversionResult.success(
                createUnit("meter", "meters", 1.0),
                createUnit("foot", "feet", 0.3048),
                BigRational.of(100),
                BigRational.of(328.084),
                "length"
        );

        assertThat(result.isSuccessful()).isTrue();
    }

    @Test
    void isSuccessfulReturnsFalseForPartialResults() {
        ConversionResult result = ConversionResult.partial(
                createUnit("meter", "meters", 1.0),
                null,
                BigRational.of(100)
        );

        assertThat(result.isSuccessful()).isFalse();
    }

    // ==================== toString ====================

    @Test
    void toStringForSuccessfulConversionUsesPluralForms() {
        ConversionResult result = ConversionResult.success(
                createUnit("meter", "meters", 1.0),
                createUnit("foot", "feet", 0.3048),
                BigRational.of(100),
                BigRational.of(328.084),
                "length"
        );

        String string = result.toString();
        assertThat(string).contains("100");
        assertThat(string).contains("meters");
        assertThat(string).contains("328.084");
        assertThat(string).contains("feet");
    }

    @Test
    void toStringForSuccessfulConversionUsesSingularForOne() {
        ConversionResult result = ConversionResult.success(
                createUnit("meter", "meters", 1.0),
                createUnit("foot", "feet", 0.3048),
                BigRational.of(1),
                BigRational.of(3.28084),
                "length"
        );

        String string = result.toString();
        assertThat(string).contains("1");
        assertThat(string).contains("meter");
        assertThat(string).contains("3.28084");
        assertThat(string).contains("feet");
    }

    @Test
    void toStringForSuccessfulConversionUsesSingularForResultOfOne() {
        ConversionResult result = ConversionResult.success(
                createUnit("foot", "feet", 0.3048),
                createUnit("meter", "meters", 1.0),
                BigRational.of(3.28084),
                BigRational.of(1),
                "length"
        );

        String string = result.toString();
        assertThat(string).contains("3.28084");
        assertThat(string).contains("feet");
        assertThat(string).contains("1");
        assertThat(string).contains("meter");
    }

    @Test
    void toStringForFailedConversionShowsFailureMessage() {
        ConversionResult result = ConversionResult.partial(
                createUnit("meter", "meters", 1.0),
                null,
                BigRational.of(100)
        );

        assertThat(result.toString()).isEqualTo("Conversion failed");
    }

    // ==================== Equality ====================

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        SimpleUnit fromUnit = createUnit("meter", "meters", 1.0);
        SimpleUnit toUnit = createUnit("foot", "feet", 0.3048);
        BigRational input = BigRational.of(100);
        BigRational result = BigRational.of(328.084);
        String groupName = "length";

        ConversionResult result1 = ConversionResult.success(fromUnit, toUnit, input, result, groupName);
        ConversionResult result2 = ConversionResult.success(fromUnit, toUnit, input, result, groupName);

        assertThat(result1).isEqualTo(result2);
        assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
    }

    // ==================== Helper Methods ====================

    private SimpleUnit createUnit(String singular, String plural, double conversionFactor) {
        return SimpleUnit.builder()
                .singular(singular)
                .plural(plural)
                .conversionFactor(conversionFactor)
                .build();
    }
}
