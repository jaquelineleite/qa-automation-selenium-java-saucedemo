package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By successMessage =
            By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isPageLoaded(
                "checkout-complete.html",
                successMessage
        );
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }
}
