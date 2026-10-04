package br.com.qa.quality;

public record QualityMetrics(
        int total,
        int passed,
        int failed,
        double passRate,
        int criticalTotal,
        int criticalPassed,
        int criticalFailed,
        double criticalPassRate
) {
}
