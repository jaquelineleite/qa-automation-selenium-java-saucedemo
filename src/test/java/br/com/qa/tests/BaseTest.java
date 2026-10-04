package br.com.qa.tests;

import br.com.qa.config.DriverFactory;
import br.com.qa.config.TestConfig;
import br.com.qa.utils.ScreenshotOnFailureExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BaseTest.class);

    protected WebDriver driver;

    @RegisterExtension
    final ScreenshotOnFailureExtension screenshotOnFailure =
            new ScreenshotOnFailureExtension();

    @BeforeEach
    void setUp(TestInfo testInfo) {

        LOGGER.info(
                "TEST START class={} test={} browser={} headless={}",
                testInfo.getTestClass()
                        .map(Class::getSimpleName)
                        .orElse("unknown"),
                testInfo.getTestMethod()
                        .map(method -> method.getName())
                        .orElse("unknown"),
                TestConfig.getBrowser(),
                TestConfig.isHeadless()
        );

        DriverFactory.startDriver();

        driver = DriverFactory.getDriver();

        driver.get(
                TestConfig.getBaseUrl()
        );
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {

        try {
            DriverFactory.quitDriver();
        } finally {
            LOGGER.info(
                    "TEST END class={} test={}",
                    testInfo.getTestClass()
                            .map(Class::getSimpleName)
                            .orElse("unknown"),
                    testInfo.getTestMethod()
                            .map(method -> method.getName())
                            .orElse("unknown")
            );
        }
    }
}
