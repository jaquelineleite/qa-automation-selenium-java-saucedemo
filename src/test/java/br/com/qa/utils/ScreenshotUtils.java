package br.com.qa.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIRECTORY =
            Path.of("screenshots");

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private ScreenshotUtils() {
        // Impede instanciação da classe utilitária.
    }

    public static Path takeScreenshot(
            WebDriver driver,
            String testName
    ) {

        if (driver == null) {
            return null;
        }

        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);

            String sanitizedTestName =
                    testName.replaceAll("[^a-zA-Z0-9-_]", "_");

            String fileName =
                    sanitizedTestName
                            + "-"
                            + LocalDateTime.now().format(FORMATTER)
                            + ".png";

            Path destination =
                    SCREENSHOT_DIRECTORY.resolve(fileName);

            File source =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            Files.copy(
                    source.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                    "[EVIDÊNCIA] Screenshot salvo em: "
                            + destination
            );

            return destination;

        } catch (IOException exception) {

            System.err.println(
                    "[EVIDÊNCIA] Não foi possível gerar screenshot: "
                            + exception.getMessage()
            );

            return null;
        }
    }
}
