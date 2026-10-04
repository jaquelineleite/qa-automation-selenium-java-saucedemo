package br.com.qa.utils;

import br.com.qa.config.DriverFactory;
import br.com.qa.failure.FailureClassification;
import br.com.qa.failure.FailureClassifier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotOnFailureExtension
        implements TestExecutionExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    ScreenshotOnFailureExtension.class
            );

    @Override
    public void handleTestExecutionException(
            ExtensionContext context,
            Throwable throwable
    ) throws Throwable {

        String testName =
                context.getRequiredTestClass().getSimpleName()
                        + "-"
                        + context.getRequiredTestMethod().getName();

        FailureClassification classification =
                FailureClassifier.classify(throwable);

        LOGGER.error(
                "TEST FAILURE test={} category={} reason={} error={}",
                testName,
                classification.category(),
                classification.reason(),
                throwable.getMessage()
        );

        try {
            WebDriver driver = DriverFactory.getDriver();

            ScreenshotUtils.takeScreenshot(
                    driver,
                    testName
            );

        } catch (Exception exception) {

            LOGGER.error(
                    "SCREENSHOT CAPTURE FAILED test={} error={}",
                    testName,
                    exception.getMessage(),
                    exception
            );
        }

        throw throwable;
    }
}
