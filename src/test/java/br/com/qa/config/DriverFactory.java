package br.com.qa.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    private static final Duration PAGE_LOAD_TIMEOUT =
            Duration.ofSeconds(30);

    private DriverFactory() {
        /*
         * Classe utilitária responsável pelo ciclo de vida
         * do WebDriver. Não deve ser instanciada.
         */
    }

    public static void startDriver() {

        if (DRIVER.get() != null) {
            return;
        }

        String browser =
                System.getProperty(
                        "browser",
                        "chrome"
                );

        boolean headless =
                Boolean.parseBoolean(
                        System.getProperty(
                                "headless",
                                "true"
                        )
                );

        WebDriver driver =
                createDriver(
                        browser,
                        headless
                );

        driver.manage()
                .timeouts()
                .pageLoadTimeout(
                        PAGE_LOAD_TIMEOUT
                );

        DRIVER.set(driver);
    }

    private static WebDriver createDriver(
            String browser,
            boolean headless
    ) {

        if (!browser.equalsIgnoreCase("chrome")) {
            throw new IllegalArgumentException(
                    "Navegador não suportado: "
                            + browser
            );
        }

        return createChromeDriver(
                headless
        );
    }

    private static WebDriver createChromeDriver(
            boolean headless
    ) {

        ChromeOptions options =
                new ChromeOptions();

        if (headless) {
            options.addArguments(
                    "--headless=new"
            );
        }

        options.addArguments(
                "--window-size=1920,1080",
                "--disable-dev-shm-usage",
                "--no-sandbox"
        );

        /*
         * Não fixa uma versão específica do Chrome.
         *
         * O Selenium Manager pode resolver automaticamente
         * navegador/driver compatíveis com o ambiente.
         */
        return new ChromeDriver(
                options
        );
    }

    public static WebDriver getDriver() {

        WebDriver driver =
                DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver não foi iniciado. "
                            + "Execute startDriver() primeiro."
            );
        }

        return driver;
    }

    public static void quitDriver() {

        WebDriver driver =
                DRIVER.get();

        if (driver == null) {
            return;
        }

        try {
            driver.quit();
        } finally {
            /*
             * Remove a referência associada à thread,
             * evitando reutilização indevida do driver.
             */
            DRIVER.remove();
        }
    }
}