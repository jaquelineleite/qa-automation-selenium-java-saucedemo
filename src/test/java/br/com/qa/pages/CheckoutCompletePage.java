package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePage {

    private final By successMessage =
            By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains("checkout-complete.html")
            );

            return isDisplayed(successMessage);

        } catch (Exception exception) {
            return false;
        }
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }
}
