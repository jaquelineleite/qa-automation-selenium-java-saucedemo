package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
            waitForUrlContaining(
                    "checkout-step-two.html"
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
        clickAndWaitForUrl(
                finishButton,
                "checkout-complete.html"
        );
    }
}