package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
        try {
            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-step-one.html"
                    )
            );

            return isDisplayed(firstNameInput);

        } catch (Exception exception) {
            return false;
        }
    }

    private void fillField(By locator, String value) {

        WebElement element = waitForClickable(locator);

        /*
         * Primeiro utiliza a interação padrão do Selenium.
         */
        element.click();
        element.clear();
        element.sendKeys(value);

        /*
         * Sincroniza o valor com inputs controlados pelo React.
         * O setter nativo + evento input garante que o onChange
         * da aplicação seja disparado.
         */
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

    public CheckoutPage fillFirstName(String firstName) {
        fillField(firstNameInput, firstName);
        return this;
    }

    public CheckoutPage fillLastName(String lastName) {
        fillField(lastNameInput, lastName);
        return this;
    }

    public CheckoutPage fillPostalCode(String postalCode) {
        fillField(postalCodeInput, postalCode);
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

    private boolean waitForCheckoutResult(Duration duration) {

        try {
            new WebDriverWait(driver, duration)
                    .until(currentDriver ->
                            currentDriver
                                    .getCurrentUrl()
                                    .contains(
                                            "checkout-step-two.html"
                                    )
                                    ||
                            !currentDriver
                                    .findElements(errorMessage)
                                    .isEmpty()
                    );

            return true;

        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void continueCheckout() {

        WebElement button =
                waitForClickable(continueButton);

        // 1ª tentativa: clique Selenium normal
        button.click();

        if (!waitForCheckoutResult(
                Duration.ofSeconds(2))) {

            // 2ª tentativa: teclado
            button =
                    waitForClickable(continueButton);

            button.sendKeys(Keys.ENTER);
        }

        if (!waitForCheckoutResult(
                Duration.ofSeconds(2))) {

            // 3ª tentativa: submete o próprio formulário
            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            button =
                    waitForClickable(continueButton);

            js.executeScript(
                    "arguments[0].closest('form').requestSubmit();",
                    button
            );
        }

        if (!waitForCheckoutResult(
                Duration.ofSeconds(10))) {

            throw new IllegalStateException(
                    "Checkout não respondeu após o envio. URL atual: "
                            + driver.getCurrentUrl()
            );
        }

        if (!driver.findElements(errorMessage).isEmpty()) {

            String error =
                    driver.findElement(errorMessage)
                            .getText();

            throw new IllegalStateException(
                    "Checkout não avançou. Aplicação retornou: "
                            + error
            );
        }

        if (!driver.getCurrentUrl()
                .contains("checkout-step-two.html")) {

            throw new IllegalStateException(
                    "Página Checkout Overview não foi carregada."
            );
        }
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
