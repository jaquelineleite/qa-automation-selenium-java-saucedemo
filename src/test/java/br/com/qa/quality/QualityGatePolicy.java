package br.com.qa.quality;

public record QualityGatePolicy(
        double minimumOverallPassRate,
        double minimumCriticalPassRate,
        int maximumCriticalFailures
) {

    public static QualityGatePolicy defaultPolicy() {
        return new QualityGatePolicy(
                95.0,
                100.0,
                0
        );
    }
}
