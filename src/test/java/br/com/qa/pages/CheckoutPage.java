package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class CheckoutPage extends BasePage {

    private final By firstNameInput =
            By.id("first-name");

    private final By lastNameInput =
            By.id("last-name");

    private final By postalCodeInput =
            By.id("postal-code");

    private final By continueButton =
            By.id("continue");

    private final By errorMessage =
            By.cssSelector("[data-test='error']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isPageLoaded(
                "checkout-step-one.html",
                firstNameInput
        );
    }


    private void fillField(
            By locator,
            String value
    ) {
        WebElement element = waitForClickable(locator);

        element.click();
        element.clear();
        element.sendKeys(value);

        synchronizeReactInput(
                element,
                value
        );

        wait.until(currentDriver -> {
            WebElement currentElement =
                    currentDriver.findElement(locator);

            String currentValue =
                    (String) ((JavascriptExecutor) currentDriver)
                            .executeScript(
                                    "return arguments[0].value;",
                                    currentElement
                            );

            return value.equals(currentValue);
        });
    }


    private void synchronizeReactInput(
            WebElement element,
            String value
    ) {

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                """
                const element = arguments[0];
                const value = arguments[1];

                const setter =
                    Object.getOwnPropertyDescriptor(
                        HTMLInputElement.prototype,
                        'value'
                    ).set;

                setter.call(element, '');

                element.dispatchEvent(
                    new Event('input', { bubbles: true })
                );

                setter.call(element, value);

                element.dispatchEvent(
                    new Event('input', { bubbles: true })
                );

                element.dispatchEvent(
                    new Event('change', { bubbles: true })
                );
                """,
                element,
                value
        );
    }

    public CheckoutPage fillFirstName(
            String firstName
    ) {

        fillField(
                firstNameInput,
                firstName
        );

        return this;
    }

    public CheckoutPage fillLastName(
            String lastName
    ) {

        fillField(
                lastNameInput,
                lastName
        );

        return this;
    }

    public CheckoutPage fillPostalCode(
            String postalCode
    ) {

        fillField(
                postalCodeInput,
                postalCode
        );

        return this;
    }

    public void fillCustomerData(
            String firstName,
            String lastName,
            String postalCode
    ) {

        fillFirstName(firstName);
        fillLastName(lastName);
        fillPostalCode(postalCode);
    }

    private void submitFormWithJavaScript() {

        WebElement button =
                waitForClickable(continueButton);

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "arguments[0].closest('form').requestSubmit();",
                button
        );
    }

    public void continueCheckout() {
        submitFormWithJavaScript();
        waitForUrlContaining("checkout-step-two.html");
        validateSuccessfulCheckout();
    }


    private void validateSuccessfulCheckout() {

        if (!driver
                .findElements(errorMessage)
                .isEmpty()) {

            String error =
                    getText(errorMessage);

            throw new IllegalStateException(
                    "Checkout não avançou. Aplicação retornou: "
                            + error
            );
        }

        if (!driver
                .getCurrentUrl()
                .contains(
                        "checkout-step-two.html"
                )) {

            throw new IllegalStateException(
                    "Página Checkout Overview não foi carregada."
            );
        }
    }

    public void submitExpectingValidationError() {
        submitFormWithJavaScript();
        waitForVisibility(errorMessage);
    }


    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
