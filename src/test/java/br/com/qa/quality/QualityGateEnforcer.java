package br.com.qa.quality;

public final class QualityGateEnforcer {

    private QualityGateEnforcer() {
    }

    public static void enforce(
            QualityGateResult result,
            boolean enabled
    ) {
        if (enabled && !result.passed()) {
            throw new IllegalStateException(
                    "Quality Gate failed: " + result.reasons()
            );
        }
    }
}
