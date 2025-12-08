package uk.co.ryanharrison.mathengine.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @Test
    void outerParenthesisRemoved() {
        Assertions.assertThat(Utils.removeOuterParenthesis("2+3")).isEqualTo("2+3");
        assertThat(Utils.removeOuterParenthesis("(2+3)")).isEqualTo("2+3");
        assertThat(Utils.removeOuterParenthesis("2^3 + (2 + 3)")).isEqualTo("2^3 + (2 + 3)");
        assertThat(Utils.removeOuterParenthesis("(2^3) + (2 + 3)")).isEqualTo("(2^3) + (2 + 3)");
        assertThat(Utils.removeOuterParenthesis("(2^3 + (2 + 3))")).isEqualTo("2^3 + (2 + 3)");
        assertThat(Utils.removeOuterParenthesis("((2^3) + (2 + 3))")).isEqualTo("(2^3) + (2 + 3)");
    }

}
