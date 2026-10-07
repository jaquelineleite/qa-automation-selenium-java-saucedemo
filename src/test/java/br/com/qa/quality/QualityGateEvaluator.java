package br.com.qa.quality;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QualityGateEvaluator {

    public QualityGateResult evaluate(
            QualityMetrics metrics,
            QualityGatePolicy policy
    ) {

        List<String> reasons = new ArrayList<>();

        if (metrics.passRate()
                < policy.minimumOverallPassRate()) {

            reasons.add(String.format(
                    Locale.ROOT,
                    "Overall pass rate %.2f%% is below minimum %.2f%%",
                    metrics.passRate(),
                    policy.minimumOverallPassRate()
            ));
        }

        if (metrics.criticalTotal() > 0
                && metrics.criticalPassRate()
                < policy.minimumCriticalPassRate()) {

            reasons.add(String.format(
                    Locale.ROOT,
                    "Critical pass rate %.2f%% is below minimum %.2f%%",
                    metrics.criticalPassRate(),
                    policy.minimumCriticalPassRate()
            ));
        }

        if (metrics.criticalFailed()
                > policy.maximumCriticalFailures()) {

            reasons.add(String.format(
                    Locale.ROOT,
                    "Critical failures %d exceed maximum %d",
                    metrics.criticalFailed(),
                    policy.maximumCriticalFailures()
            ));
        }

        return new QualityGateResult(
                reasons.isEmpty(),
                reasons
        );
    }
}
