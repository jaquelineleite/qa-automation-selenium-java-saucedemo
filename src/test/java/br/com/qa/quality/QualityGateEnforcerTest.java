package br.com.qa.quality;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QualityGateEnforcerTest {

    @Test
    void shouldNotBlockWhenGatePassesAndEnforcementIsEnabled() {
        QualityGateResult result =
                new QualityGateResult(true, List.of());

        assertDoesNotThrow(
                () -> QualityGateEnforcer.enforce(result, true)
        );
    }

    @Test
    void shouldNotBlockWhenGateFailsAndEnforcementIsDisabled() {
        QualityGateResult result =
                new QualityGateResult(
                        false,
                        List.of("Overall pass rate below minimum")
                );

        assertDoesNotThrow(
                () -> QualityGateEnforcer.enforce(result, false)
        );
    }

    @Test
    void shouldBlockWhenGateFailsAndEnforcementIsEnabled() {
        QualityGateResult result =
                new QualityGateResult(
                        false,
                        List.of("Overall pass rate below minimum")
                );

        assertThrows(
                IllegalStateException.class,
                () -> QualityGateEnforcer.enforce(result, true)
        );
    }
}
