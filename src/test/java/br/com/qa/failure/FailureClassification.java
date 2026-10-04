package br.com.qa.failure;

public record FailureClassification(
        FailureCategory category,
        String reason
) {
}
