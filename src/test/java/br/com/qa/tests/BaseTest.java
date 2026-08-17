package br.com.qa.tests;

import br.com.qa.config.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    protected WebDriver driver;

    private static final String BASE_URL =
            System.getProperty("baseUrl", "https://www.saucedemo.com/");

    @BeforeEach
    void setUp() {
        DriverFactory.startDriver();
        driver = DriverFactory.getDriver();

        driver.manage().deleteAllCookies();
        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }
}
