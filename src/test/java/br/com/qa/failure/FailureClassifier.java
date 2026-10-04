package br.com.qa.failure;

import org.opentest4j.AssertionFailedError;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.util.Arrays;

public final class FailureClassifier {

    private static final String BASE_TEST_CLASS = "br.com.qa.tests.BaseTest";
    private static final String SETUP_METHOD = "setUp";
    private static final String TEARDOWN_METHOD = "tearDown";

    private FailureClassifier() {
    }

    public static FailureClassification classify(Throwable throwable) {
        if (throwable == null) {
            return new FailureClassification(
                    FailureCategory.UNKNOWN,
                    "No failure information available"
            );
        }

        if (containsFrame(throwable, BASE_TEST_CLASS, SETUP_METHOD)
                && isBrowserOrInfrastructureFailure(throwable)) {
            return new FailureClassification(
                    FailureCategory.SETUP_OR_INFRASTRUCTURE,
                    "Browser or infrastructure failure detected during test setup"
            );
        }

        if (containsFrame(throwable, BASE_TEST_CLASS, TEARDOWN_METHOD)) {
            return new FailureClassification(
                    FailureCategory.TEARDOWN,
                    "Failure detected during test teardown"
            );
        }

        if (throwable instanceof AssertionFailedError || throwable instanceof AssertionError) {
            return new FailureClassification(
                    FailureCategory.ASSERTION,
                    "Test assertion failed"
            );
        }

        if (throwable instanceof WebDriverException) {
            return new FailureClassification(
                    FailureCategory.BROWSER_INTERACTION,
                    "WebDriver failure detected during test execution"
            );
        }

        return new FailureClassification(
                FailureCategory.UNKNOWN,
                "Failure does not match a known classification rule"
        );
    }

    private static boolean isBrowserOrInfrastructureFailure(Throwable throwable) {
        return throwable instanceof TimeoutException
                || throwable instanceof WebDriverException;
    }

    private static boolean containsFrame(
            Throwable throwable,
            String className,
            String methodName
    ) {
        return Arrays.stream(throwable.getStackTrace())
                .anyMatch(frame ->
                        frame.getClassName().equals(className)
                                && frame.getMethodName().equals(methodName));
    }
}
