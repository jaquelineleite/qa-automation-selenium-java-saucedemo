package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutOverviewPage extends BasePage {

    private final By productName =
            By.cssSelector("[data-test='inventory-item-name']");

    private final By finishButton =
            By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-step-two.html"
                    )
            );

            return isDisplayed(productName)
                    && isDisplayed(finishButton);

        } catch (Exception exception) {
            return false;
        }
    }

    public String getProductName() {
        return getText(productName);
    }

    public void finishPurchase() {

        WebElement button =
                waitForClickable(finishButton);

        button.click();

        try {
            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(3)
            ).until(
                    ExpectedConditions.urlContains(
                            "checkout-complete.html"
                    )
            );

        } catch (TimeoutException exception) {

            button = waitForClickable(finishButton);

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].click();",
                    button
            );

            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-complete.html"
                    )
            );
        }
    }
}
