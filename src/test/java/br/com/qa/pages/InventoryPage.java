package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
            waitForUrlContaining("inventory.html");
            return isDisplayed(pageTitle);

        } catch (Exception exception) {
            return false;
        }
    }

    public InventoryPage addBackpackToCart() {

        click(backpackAddButton);

        boolean badgeDisplayed =
                waitForCondition(
                        ExpectedConditions
                                .visibilityOfElementLocated(
                                        shoppingCartBadge
                                ),
                        Duration.ofSeconds(3)
                );

        if (!badgeDisplayed) {

            clickWithJavaScript(
                    backpackAddButton
            );

            waitForVisibility(
                    shoppingCartBadge
            );
        }

        return this;
    }

    public InventoryPage removeBackpackFromCart() {

        click(backpackRemoveButton);

        boolean badgeRemoved =
                waitForCondition(
                        currentDriver ->
                                currentDriver
                                        .findElements(
                                                shoppingCartBadge
                                        )
                                        .isEmpty(),
                        Duration.ofSeconds(3)
                );

        if (!badgeRemoved) {

            clickWithJavaScript(
                    backpackRemoveButton
            );

            wait.until(
                    currentDriver ->
                            currentDriver
                                    .findElements(
                                            shoppingCartBadge
                                    )
                                    .isEmpty()
            );
        }

        return this;
    }

    public boolean isCartBadgeDisplayed() {
        return !driver
                .findElements(shoppingCartBadge)
                .isEmpty();
    }

    public String getCartItemCount() {
        return getText(shoppingCartBadge);
    }

    public void openCart() {
        clickAndWaitForUrl(
                shoppingCartLink,
                "cart.html"
        );
    }
}