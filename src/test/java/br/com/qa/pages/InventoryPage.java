package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InventoryPage extends BasePage {

    private final By pageTitle =
            By.cssSelector("[data-test='title']");

    private final By backpackAddButton =
            By.id("add-to-cart-sauce-labs-backpack");

    private final By backpackRemoveButton =
            By.id("remove-sauce-labs-backpack");

    private final By shoppingCartLink =
            By.cssSelector("[data-test='shopping-cart-link']");

    private final By shoppingCartBadge =
            By.cssSelector("[data-test='shopping-cart-badge']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains("inventory.html")
            );

            return isDisplayed(pageTitle);

        } catch (Exception exception) {
            return false;
        }
    }

    public InventoryPage addBackpackToCart() {

        WebElement addButton =
                waitForClickable(backpackAddButton);

        addButton.click();

        try {
            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(3)
            ).until(
                    ExpectedConditions.visibilityOfElementLocated(
                            shoppingCartBadge
                    )
            );

        } catch (TimeoutException exception) {

            addButton =
                    waitForClickable(backpackAddButton);

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].click();",
                    addButton
            );

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            shoppingCartBadge
                    )
            );
        }

        return this;
    }

    public InventoryPage removeBackpackFromCart() {
        click(backpackRemoveButton);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        backpackAddButton
                )
        );

        return this;
    }

    public String getCartItemCount() {
        return getText(shoppingCartBadge);
    }

    public void openCart() {

        WebElement cartElement =
                waitForClickable(shoppingCartLink);

        cartElement.click();

        try {
            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(3)
            ).until(
                    ExpectedConditions.urlContains("cart.html")
            );

        } catch (TimeoutException exception) {

            cartElement =
                    waitForClickable(shoppingCartLink);

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].click();",
                    cartElement
            );

            wait.until(
                    ExpectedConditions.urlContains("cart.html")
            );
        }
    }
}
