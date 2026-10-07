package br.com.qa.metrics;

import br.com.qa.quality.QualityGateEnforcer;
import br.com.qa.quality.QualityGateEvaluator;
import br.com.qa.quality.QualityGatePolicy;
import br.com.qa.quality.QualityGateResult;
import br.com.qa.quality.QualityMetrics;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class QualityMetricsExtension
        implements TestWatcher, BeforeAllCallback {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(QualityMetricsExtension.class);

    private static final String PRODUCT_TEST_TAG = "regression";
    private static final String CRITICAL_TAG = "smoke";

    private static final String QUALITY_GATE_ENFORCE_PROPERTY =
            "quality.gate.enforce";

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(
                    QualityMetricsExtension.class
            );

    @Override
    public void beforeAll(ExtensionContext context) {
        getMetricsReport(context);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        if (!isProductTest(context)) {
            return;
        }

        getMetricsReport(context).recordSuccess(
                isCritical(context)
        );
    }

    @Override
    public void testFailed(
            ExtensionContext context,
            Throwable cause
    ) {
        if (!isProductTest(context)) {
            return;
        }

        getMetricsReport(context).recordFailure(
                isCritical(context)
        );
    }

    private MetricsReport getMetricsReport(
            ExtensionContext context
    ) {
        return context.getRoot()
                .getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        MetricsReport.class,
                        key -> new MetricsReport(),
                        MetricsReport.class
                );
    }

    private boolean isProductTest(ExtensionContext context) {
        return context.getTags().contains(PRODUCT_TEST_TAG);
    }

    private boolean isCritical(ExtensionContext context) {
        return context.getTags().contains(CRITICAL_TAG);
    }

    private static final class MetricsReport
            implements ExtensionContext.Store.CloseableResource {

        private final AtomicInteger total =
                new AtomicInteger();

        private final AtomicInteger passed =
                new AtomicInteger();

        private final AtomicInteger failed =
                new AtomicInteger();

        private final AtomicInteger criticalTotal =
                new AtomicInteger();

        private final AtomicInteger criticalPassed =
                new AtomicInteger();

        private final AtomicInteger criticalFailed =
                new AtomicInteger();

        private final long startTime =
                System.nanoTime();

        private void recordSuccess(boolean critical) {
            total.incrementAndGet();
            passed.incrementAndGet();

            if (critical) {
                criticalTotal.incrementAndGet();
                criticalPassed.incrementAndGet();
            }
        }

        private void recordFailure(boolean critical) {
            total.incrementAndGet();
            failed.incrementAndGet();

            if (critical) {
                criticalTotal.incrementAndGet();
                criticalFailed.incrementAndGet();
            }
        }

        @Override
        public void close() {

            int totalValue = total.get();
            int passedValue = passed.get();
            int failedValue = failed.get();

            int criticalTotalValue = criticalTotal.get();
            int criticalPassedValue = criticalPassed.get();
            int criticalFailedValue = criticalFailed.get();

            double passRate =
                    calculateRate(
                            passedValue,
                            totalValue
                    );

            double criticalPassRate =
                    calculateRate(
                            criticalPassedValue,
                            criticalTotalValue
                    );

            double durationSeconds =
                    (System.nanoTime() - startTime)
                            / 1_000_000_000.0;

            LOGGER.info(
                    "QUALITY SUMMARY total={} passed={} failed={} passRate={} " +
                            "criticalTotal={} criticalPassed={} criticalFailed={} " +
                            "criticalPassRate={} durationSeconds={}",
                    totalValue,
                    passedValue,
                    failedValue,
                    format(passRate),
                    criticalTotalValue,
                    criticalPassedValue,
                    criticalFailedValue,
                    format(criticalPassRate),
                    format(durationSeconds)
            );

            if (totalValue > 0) {
                evaluateQualityGate(
                        totalValue,
                        passedValue,
                        failedValue,
                        passRate,
                        criticalTotalValue,
                        criticalPassedValue,
                        criticalFailedValue,
                        criticalPassRate
                );
            }
        }

        private static void evaluateQualityGate(
                int total,
                int passed,
                int failed,
                double passRate,
                int criticalTotal,
                int criticalPassed,
                int criticalFailed,
                double criticalPassRate
        ) {
            QualityMetrics metrics = new QualityMetrics(
                    total,
                    passed,
                    failed,
                    passRate,
                    criticalTotal,
                    criticalPassed,
                    criticalFailed,
                    criticalPassRate
            );

            QualityGateEvaluator evaluator =
                    new QualityGateEvaluator();

            QualityGateResult result = evaluator.evaluate(
                    metrics,
                    QualityGatePolicy.defaultPolicy()
            );

            if (result.passed()) {
                LOGGER.info(
                        "QUALITY GATE status=PASSED"
                );
            } else {
                LOGGER.warn(
                        "QUALITY GATE status=FAILED reasons={}",
                        result.reasons()
                );

                QualityGateEnforcer.enforce(
                        result,
                        isQualityGateEnforced()
                );
            }
        }

        private static boolean isQualityGateEnforced() {
            return Boolean.parseBoolean(
                    System.getProperty(
                            QUALITY_GATE_ENFORCE_PROPERTY,
                            "false"
                    )
            );
        }

        private static double calculateRate(
                int successful,
                int total
        ) {
            return total == 0
                    ? 0.0
                    : (successful * 100.0) / total;
        }

        private static String format(double value) {
            return String.format(
                    Locale.ROOT,
                    "%.2f",
                    value
            );
        }
    }
}
