package br.com.qa.tests;

import br.com.qa.config.DriverFactory;
import br.com.qa.config.TestConfig;
import br.com.qa.utils.ScreenshotOnFailureExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    protected WebDriver driver;

    @RegisterExtension
    final ScreenshotOnFailureExtension screenshotOnFailure =
            new ScreenshotOnFailureExtension();

    @BeforeEach
    void setUp() {

        DriverFactory.startDriver();

        driver = DriverFactory.getDriver();

        driver.get(
                TestConfig.getBaseUrl()
        );
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }
}
