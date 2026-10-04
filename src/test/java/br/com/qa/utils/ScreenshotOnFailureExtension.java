package br.com.qa.utils;

import br.com.qa.config.DriverFactory;
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

        LOGGER.error(
                "TEST FAILURE test={} error={}",
                testName,
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
