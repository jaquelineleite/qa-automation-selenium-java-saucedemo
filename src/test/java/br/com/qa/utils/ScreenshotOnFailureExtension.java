package br.com.qa.utils;

import br.com.qa.config.DriverFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.WebDriver;

public class ScreenshotOnFailureExtension
        implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(
            ExtensionContext context,
            Throwable throwable
    ) throws Throwable {

        try {
            WebDriver driver = DriverFactory.getDriver();

            String testName =
                    context.getRequiredTestClass().getSimpleName()
                            + "-"
                            + context.getRequiredTestMethod().getName();

            ScreenshotUtils.takeScreenshot(
                    driver,
                    testName
            );

        } catch (Exception exception) {

            System.err.println(
                    "[EVIDÊNCIA] Não foi possível capturar screenshot: "
                            + exception.getMessage()
            );
        }

        throw throwable;
    }
}
