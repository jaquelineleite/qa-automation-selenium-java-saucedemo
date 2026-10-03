package br.com.qa.config;

import java.time.Duration;

public final class TestConfig {

    private static final String DEFAULT_BASE_URL =
            "https://www.saucedemo.com/";

    private static final String DEFAULT_BROWSER =
            "chrome";

    private static final boolean DEFAULT_HEADLESS =
            true;

    private static final Duration DEFAULT_PAGE_LOAD_TIMEOUT =
            Duration.ofSeconds(30);

    private TestConfig() {
        /*
         * Centraliza configurações de execução dos testes.
         * Não deve ser instanciada.
         */
    }

    public static String getBaseUrl() {
        return System.getProperty(
                "baseUrl",
                DEFAULT_BASE_URL
        );
    }

    public static String getBrowser() {
        return System.getProperty(
                "browser",
                DEFAULT_BROWSER
        );
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(
                System.getProperty(
                        "headless",
                        String.valueOf(
                                DEFAULT_HEADLESS
                        )
                )
        );
    }

    public static Duration getPageLoadTimeout() {
        return DEFAULT_PAGE_LOAD_TIMEOUT;
    }
}
