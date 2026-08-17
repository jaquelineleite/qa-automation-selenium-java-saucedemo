package br.com.qa.tests;

import br.com.qa.pages.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseTest extends BaseTest {

    @Test
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
                "standard_user",
                "secret_sauce"
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
                "Jaqueline",
                "QA",
                "18150-000"
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
