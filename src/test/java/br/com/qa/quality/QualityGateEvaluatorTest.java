package br.com.qa.quality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QualityGateEvaluatorTest {

    private final QualityGateEvaluator evaluator =
            new QualityGateEvaluator();

    private final QualityGatePolicy policy =
            QualityGatePolicy.defaultPolicy();

    @Test
    void shouldPassWhenAllQualityCriteriaAreMet() {

        QualityMetrics metrics = new QualityMetrics(
                12,
                12,
                0,
                100.0,
                3,
                3,
                0,
                100.0
        );

        QualityGateResult result =
                evaluator.evaluate(metrics, policy);

        assertTrue(result.passed());
        assertTrue(result.reasons().isEmpty());
    }

    @Test
    void shouldFailWhenOverallPassRateIsBelowMinimum() {

        QualityMetrics metrics = new QualityMetrics(
                12,
                11,
                1,
                91.67,
                3,
                3,
                0,
                100.0
        );

        QualityGateResult result =
                evaluator.evaluate(metrics, policy);

        assertFalse(result.passed());
        assertEquals(1, result.reasons().size());

        assertTrue(
                result.reasons().get(0)
                        .contains("Overall pass rate")
        );
    }

    @Test
    void shouldFailWhenCriticalJourneyFails() {

        QualityMetrics metrics = new QualityMetrics(
                12,
                11,
                1,
                91.67,
                3,
                2,
                1,
                66.67
        );

        QualityGateResult result =
                evaluator.evaluate(metrics, policy);

        assertFalse(result.passed());
        assertEquals(3, result.reasons().size());

        assertTrue(
                result.reasons().stream()
                        .anyMatch(reason ->
                                reason.contains("Overall pass rate"))
        );

        assertTrue(
                result.reasons().stream()
                        .anyMatch(reason ->
                                reason.contains("Critical pass rate"))
        );

        assertTrue(
                result.reasons().stream()
                        .anyMatch(reason ->
                                reason.contains("Critical failures"))
        );
    }

    @Test
    void shouldNotFailCriticalPassRateWhenNoCriticalTestsWereExecuted() {

        QualityMetrics metrics = new QualityMetrics(
                3,
                3,
                0,
                100.0,
                0,
                0,
                0,
                0.0
        );

        QualityGateResult result =
                evaluator.evaluate(metrics, policy);

        assertTrue(result.passed());
        assertTrue(result.reasons().isEmpty());
    }
}
