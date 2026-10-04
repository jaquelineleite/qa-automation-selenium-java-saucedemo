package br.com.qa.metrics;

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

    private static final String CRITICAL_TAG = "smoke";

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(
                    QualityMetricsExtension.class
            );

    private static final AtomicInteger TOTAL =
            new AtomicInteger();

    private static final AtomicInteger PASSED =
            new AtomicInteger();

    private static final AtomicInteger FAILED =
            new AtomicInteger();

    private static final AtomicInteger CRITICAL_TOTAL =
            new AtomicInteger();

    private static final AtomicInteger CRITICAL_PASSED =
            new AtomicInteger();

    private static final AtomicInteger CRITICAL_FAILED =
            new AtomicInteger();

    @Override
    public void beforeAll(ExtensionContext context) {

        ExtensionContext root = context.getRoot();

        root.getStore(NAMESPACE).getOrComputeIfAbsent(
                MetricsReport.class,
                key -> new MetricsReport(),
                MetricsReport.class
        );
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        TOTAL.incrementAndGet();
        PASSED.incrementAndGet();

        if (isCritical(context)) {
            CRITICAL_TOTAL.incrementAndGet();
            CRITICAL_PASSED.incrementAndGet();
        }
    }

    @Override
    public void testFailed(
            ExtensionContext context,
            Throwable cause
    ) {
        TOTAL.incrementAndGet();
        FAILED.incrementAndGet();

        if (isCritical(context)) {
            CRITICAL_TOTAL.incrementAndGet();
            CRITICAL_FAILED.incrementAndGet();
        }
    }

    private boolean isCritical(ExtensionContext context) {
        return context.getTags().contains(CRITICAL_TAG);
    }

    private static final class MetricsReport
            implements ExtensionContext.Store.CloseableResource {

        private final long startTime =
                System.nanoTime();

        @Override
        public void close() {

            int total = TOTAL.get();
            int passed = PASSED.get();
            int failed = FAILED.get();

            int criticalTotal = CRITICAL_TOTAL.get();
            int criticalPassed = CRITICAL_PASSED.get();
            int criticalFailed = CRITICAL_FAILED.get();

            double passRate =
                    calculateRate(passed, total);

            double criticalPassRate =
                    calculateRate(
                            criticalPassed,
                            criticalTotal
                    );

            double durationSeconds =
                    (System.nanoTime() - startTime)
                            / 1_000_000_000.0;

            LOGGER.info(
                    "QUALITY SUMMARY total={} passed={} failed={} passRate={} " +
                            "criticalTotal={} criticalPassed={} criticalFailed={} " +
                            "criticalPassRate={} durationSeconds={}",
                    total,
                    passed,
                    failed,
                    format(passRate),
                    criticalTotal,
                    criticalPassed,
                    criticalFailed,
                    format(criticalPassRate),
                    format(durationSeconds)
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
            return String.format(Locale.ROOT, "%.2f", value);
        }
    }
}
