package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage extends BasePage {

    private final By pageTitle =
            By.cssSelector("[data-test='title']");

    private final By cartItem =
            By.cssSelector("[data-test='inventory-item']");

    private final By backpackName =
            By.cssSelector("[data-test='inventory-item-name']");

    private final By backpackPrice =
            By.cssSelector("[data-test='inventory-item-price']");

    private final By checkoutButton =
            By.id("checkout");

    private final By continueShoppingButton =
            By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains("cart.html")
            );

            waitForVisibility(pageTitle);

            return true;

        } catch (Exception exception) {
            return false;
        }
    }

    public boolean hasItem() {
        return isDisplayed(cartItem);
    }

    public String getProductName() {
        return getText(backpackName);
    }

    public String getProductPrice() {
        return getText(backpackPrice);
    }

    public void proceedToCheckout() {
        var checkoutElement = waitForClickable(checkoutButton);

        checkoutElement.click();

        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(
                            ExpectedConditions.urlContains(
                                    "checkout-step-one.html"
                            )
                    );

        } catch (TimeoutException exception) {

            checkoutElement = waitForClickable(checkoutButton);

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].click();",
                    checkoutElement
            );

            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-step-one.html"
                    )
            );
        }
    }

    public void continueShopping() {

        WebElement button =
                waitForClickable(continueShoppingButton);

        button.click();

        try {
            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(3)
            ).until(
                    ExpectedConditions.urlContains(
                            "inventory.html"
                    )
            );

        } catch (TimeoutException exception) {

            button =
                    waitForClickable(continueShoppingButton);

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].click();",
                    button
            );

            wait.until(
                    ExpectedConditions.urlContains(
                            "inventory.html"
                    )
            );
        }
    }
}
