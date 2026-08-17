package br.com.qa.tests;

import br.com.qa.pages.CartPage;
import br.com.qa.pages.CheckoutPage;
import br.com.qa.pages.InventoryPage;
import br.com.qa.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutValidationTest extends BaseTest {

    private CheckoutPage checkoutPage;

    @BeforeEach
    void navigateToCheckout() {

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.login(
                "standard_user",
                "secret_sauce"
        );

        assertTrue(inventoryPage.isLoaded());

        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        assertTrue(cartPage.isLoaded());

        cartPage.proceedToCheckout();

        checkoutPage = new CheckoutPage(driver);

        assertTrue(checkoutPage.isLoaded());
    }

    @Test
    @DisplayName("Deve impedir checkout sem nome")
    void shouldRequireFirstName() {

        checkoutPage.fillLastName("QA");
        checkoutPage.fillPostalCode("18150-000");

        checkoutPage.submitExpectingValidationError();

        assertEquals(
                "Error: First Name is required",
                checkoutPage.getErrorMessage()
        );
    }

    @Test
    @DisplayName("Deve impedir checkout sem sobrenome")
    void shouldRequireLastName() {

        checkoutPage.fillFirstName("Jaqueline");
        checkoutPage.fillPostalCode("18150-000");

        checkoutPage.submitExpectingValidationError();

        assertEquals(
                "Error: Last Name is required",
                checkoutPage.getErrorMessage()
        );
    }

    @Test
    @DisplayName("Deve impedir checkout sem CEP")
    void shouldRequirePostalCode() {

        checkoutPage.fillFirstName("Jaqueline");
        checkoutPage.fillLastName("QA");

        checkoutPage.submitExpectingValidationError();

        assertEquals(
                "Error: Postal Code is required",
                checkoutPage.getErrorMessage()
        );
    }
}
