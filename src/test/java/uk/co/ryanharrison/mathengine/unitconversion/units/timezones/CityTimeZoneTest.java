package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CityTimeZoneTest {

    // ==================== Construction ====================

    @Test
    void constructorCreatesTimeZoneWithValidValues() {
        CityTimeZone timeZone = new CityTimeZone("London", 0);

        assertThat(timeZone.cityName()).isEqualTo("London");
        assertThat(timeZone.offsetSeconds()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({
            "London, 0",
            "New York, -18000",       // -5 hours
            "Tokyo, 32400",           // +9 hours
            "Los Angeles, -28800",    // -8 hours
            "Sydney, 36000"           // +10 hours
    })
    void constructorAcceptsVariousOffsets(String cityName, int offsetSeconds) {
        CityTimeZone timeZone = new CityTimeZone(cityName, offsetSeconds);

        assertThat(timeZone.cityName()).isEqualTo(cityName);
        assertThat(timeZone.offsetSeconds()).isEqualTo(offsetSeconds);
    }

    @Test
    void constructorRejectsNullCityName() {
        assertThatThrownBy(() -> new CityTimeZone(null, 0))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("City name");
    }

    // ==================== Equality and HashCode ====================

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        CityTimeZone timeZone1 = new CityTimeZone("London", 0);
        CityTimeZone timeZone2 = new CityTimeZone("London", 0);

        assertThat(timeZone1).isEqualTo(timeZone2);
        assertThat(timeZone1.hashCode()).isEqualTo(timeZone2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentOffsets() {
        CityTimeZone timeZone1 = new CityTimeZone("London", 0);
        CityTimeZone timeZone2 = new CityTimeZone("London", 3600);

        assertThat(timeZone1).isNotEqualTo(timeZone2);
    }

    @Test
    void equalsReturnsFalseForDifferentCities() {
        CityTimeZone timeZone1 = new CityTimeZone("London", 0);
        CityTimeZone timeZone2 = new CityTimeZone("Paris", 0);

        assertThat(timeZone1).isNotEqualTo(timeZone2);
    }

    // ==================== toString ====================

    @Test
    void toStringContainsCityNameAndOffset() {
        CityTimeZone timeZone = new CityTimeZone("London", 3600);

        String string = timeZone.toString();

        assertThat(string).contains("London");
        assertThat(string).contains("3600");
    }
}
