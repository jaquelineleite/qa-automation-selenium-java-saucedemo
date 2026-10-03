package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final Duration DEFAULT_TIMEOUT =
            Duration.ofSeconds(10);

    private static final Duration ACTION_TIMEOUT =
            Duration.ofSeconds(3);

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                DEFAULT_TIMEOUT
        );
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    protected void waitForUrlContaining(String urlFragment) {
        wait.until(
                ExpectedConditions.urlContains(urlFragment)
        );
    }

    protected boolean waitForCondition(
            ExpectedCondition<?> condition,
            Duration duration
    ) {
        try {
            new WebDriverWait(driver, duration)
                    .until(condition);

            return true;

        } catch (TimeoutException exception) {
            return false;
        }
    }

    protected boolean isPageLoaded(
            String expectedUrlFragment,
            By identifyingElement
    ) {

        boolean expectedUrlLoaded =
                waitForCondition(
                        ExpectedConditions.urlContains(
                                expectedUrlFragment
                        ),
                        DEFAULT_TIMEOUT
                );

        return expectedUrlLoaded
                && isDisplayed(identifyingElement);
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void clickWithJavaScript(By locator) {

        WebElement element =
                waitForClickable(locator);

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "arguments[0].click();",
                element
        );
    }

    protected void clickAndWaitForUrl(
            By locator,
            String expectedUrlFragment
    ) {

        click(locator);

        boolean navigationCompleted =
                waitForCondition(
                        ExpectedConditions.urlContains(
                                expectedUrlFragment
                        ),
                        ACTION_TIMEOUT
                );

        if (!navigationCompleted) {

            clickWithJavaScript(locator);

            waitForUrlContaining(
                    expectedUrlFragment
            );
        }
    }

    protected void type(By locator, String text) {

        WebElement element =
                waitForVisibility(locator);

        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisibility(locator)
                .getText();
    }

    protected boolean isDisplayed(By locator) {

        try {
            waitForVisibility(locator);
            return true;

        } catch (TimeoutException exception) {
            return false;
        }
    }
}