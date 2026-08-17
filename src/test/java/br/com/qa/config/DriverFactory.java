package br.com.qa.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
        // Impede a instanciação da classe utilitária.
    }

    public static void startDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        String browser = System.getProperty("browser", "chrome");
        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", "true")
        );

        if (!browser.equalsIgnoreCase("chrome")) {
            throw new IllegalArgumentException(
                    "Navegador não suportado: " + browser
            );
        }

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments(
                "--window-size=1920,1080",
                "--disable-dev-shm-usage",
                "--no-sandbox"
        );

        WebDriver driver = new ChromeDriver(options);

        driver.manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(30));

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver não foi iniciado. Execute startDriver() primeiro."
            );
        }

        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();

        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
