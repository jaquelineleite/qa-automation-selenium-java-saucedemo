package br.com.qa.quality;

import java.util.List;

public record QualityGateResult(
        boolean passed,
        List<String> reasons
) {

    public QualityGateResult {
        reasons = List.copyOf(reasons);
    }
}
