package br.com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CheckoutPage extends BasePage {

    private static final Duration SUBMIT_TIMEOUT =
            Duration.ofSeconds(2);

    private static final Duration VALIDATION_TIMEOUT =
            Duration.ofSeconds(3);

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


    private void fillField(
            By locator,
            String value
    ) {

        WebElement element =
                waitForClickable(locator);

        /*
         * Primeiro utiliza a interação padrão do Selenium.
         */
        element.click();
        element.clear();
        element.sendKeys(value);

        /*
         * Alguns inputs controlados pelo React podem manter
         * estado interno diferente do valor apresentado no DOM.
         *
         * O setter nativo e os eventos input/change sincronizam
         * o valor com o mecanismo de eventos da aplicação.
         */
        synchronizeReactInput(
                element,
                value
        );

        /*
         * Relocaliza o elemento durante a espera em vez de
         * depender da referência WebElement original.
         */
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

    private boolean waitForCheckoutResult(
            Duration duration
    ) {

        return waitForCondition(
                currentDriver ->
                        currentDriver
                                .getCurrentUrl()
                                .contains(
                                        "checkout-step-two.html"
                                )
                                ||
                        !currentDriver
                                .findElements(errorMessage)
                                .isEmpty(),
                duration
        );
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

        /*
         * Estratégia principal:
         * interação real pelo WebDriver.
         */
        click(continueButton);

        /*
         * Se a aplicação não responder no intervalo esperado,
         * tenta submissão pelo teclado.
         */
        if (!waitForCheckoutResult(
                SUBMIT_TIMEOUT)) {

            WebElement button =
                    waitForClickable(
                            continueButton
                    );

            button.sendKeys(Keys.ENTER);
        }

        /*
         * Último fallback:
         * submissão explícita do formulário.
         */
        if (!waitForCheckoutResult(
                SUBMIT_TIMEOUT)) {

            submitFormWithJavaScript();
        }

        /*
         * Aguarda o resultado definitivo utilizando
         * o timeout padrão da BasePage.
         */
        boolean checkoutCompleted =
                waitForCheckoutResult(
                        Duration.ofSeconds(10)
                );

        if (!checkoutCompleted) {

            throw new IllegalStateException(
                    "Checkout não respondeu após o envio. URL atual: "
                            + driver.getCurrentUrl()
            );
        }

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

        click(continueButton);

        boolean validationDisplayed =
                waitForCondition(
                        ExpectedConditions
                                .visibilityOfElementLocated(
                                        errorMessage
                                ),
                        VALIDATION_TIMEOUT
                );

        if (!validationDisplayed) {

            submitFormWithJavaScript();

            waitForVisibility(
                    errorMessage
            );
        }
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
