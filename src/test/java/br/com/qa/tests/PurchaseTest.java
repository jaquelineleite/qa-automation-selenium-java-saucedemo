package br.com.qa.tests;

import br.com.qa.data.TestData;

import br.com.qa.pages.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("regression")
class PurchaseTest extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("Deve realizar compra com sucesso")
    void shouldCompletePurchaseSuccessfully() {

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        CheckoutOverviewPage overviewPage =
                new CheckoutOverviewPage(driver);
        CheckoutCompletePage completePage =
                new CheckoutCompletePage(driver);

        loginPage.login(
                TestData.STANDARD_USER.username(),
                TestData.STANDARD_USER.password()
        );

        assertTrue(inventoryPage.isLoaded());

        inventoryPage.addBackpackToCart();

        assertEquals(
                "1",
                inventoryPage.getCartItemCount()
        );

        inventoryPage.openCart();

        assertTrue(cartPage.isLoaded());

        assertEquals(
                "Sauce Labs Backpack",
                cartPage.getProductName()
        );

        cartPage.proceedToCheckout();

        assertTrue(checkoutPage.isLoaded());

        checkoutPage.fillCustomerData(
                TestData.VALID_CHECKOUT.firstName(),
                TestData.VALID_CHECKOUT.lastName(),
                TestData.VALID_CHECKOUT.postalCode()
        );

        checkoutPage.continueCheckout();

        assertTrue(overviewPage.isLoaded());

        assertEquals(
                "Sauce Labs Backpack",
                overviewPage.getProductName()
        );

        overviewPage.finishPurchase();

        assertTrue(completePage.isLoaded());

        assertEquals(
                "Thank you for your order!",
                completePage.getSuccessMessage()
        );
    }
}
