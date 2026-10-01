package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
            waitForUrlContaining("cart.html");
            return isDisplayed(pageTitle);

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
        clickAndWaitForUrl(
                checkoutButton,
                "checkout-step-one.html"
        );
    }

    public void continueShopping() {
        clickAndWaitForUrl(
                continueShoppingButton,
                "inventory.html"
        );
    }
}