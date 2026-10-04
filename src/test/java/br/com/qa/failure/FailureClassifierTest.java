package br.com.qa.failure;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FailureClassifierTest {

    @Test
    void shouldClassifyBrowserTimeoutDuringSetupAsInfrastructure() {
        TimeoutException failure = new TimeoutException("renderer timeout");
        failure.setStackTrace(new StackTraceElement[]{
                new StackTraceElement(
                        "br.com.qa.tests.BaseTest",
                        "setUp",
                        "BaseTest.java",
                        44
                )
        });

        FailureClassification classification =
                FailureClassifier.classify(failure);

        assertEquals(
                FailureCategory.SETUP_OR_INFRASTRUCTURE,
                classification.category()
        );
    }

    @Test
    void shouldClassifyAssertionFailureAsAssertion() {
        AssertionFailedError failure =
                new AssertionFailedError("expected true");

        FailureClassification classification =
                FailureClassifier.classify(failure);

        assertEquals(
                FailureCategory.ASSERTION,
                classification.category()
        );
    }

    @Test
    void shouldClassifyWebDriverFailureDuringExecutionAsBrowserInteraction() {
        WebDriverException failure =
                new WebDriverException("element interaction failed");

        FailureClassification classification =
                FailureClassifier.classify(failure);

        assertEquals(
                FailureCategory.BROWSER_INTERACTION,
                classification.category()
        );
    }

    @Test
    void shouldClassifyFailureDuringTeardown() {
        RuntimeException failure =
                new RuntimeException("cleanup failed");

        failure.setStackTrace(new StackTraceElement[]{
                new StackTraceElement(
                        "br.com.qa.tests.BaseTest",
                        "tearDown",
                        "BaseTest.java",
                        55
                )
        });

        FailureClassification classification =
                FailureClassifier.classify(failure);

        assertEquals(
                FailureCategory.TEARDOWN,
                classification.category()
        );
    }

    @Test
    void shouldReturnUnknownForUnclassifiedFailure() {
        RuntimeException failure =
                new RuntimeException("unexpected failure");

        FailureClassification classification =
                FailureClassifier.classify(failure);

        assertEquals(
                FailureCategory.UNKNOWN,
                classification.category()
        );
    }
}
