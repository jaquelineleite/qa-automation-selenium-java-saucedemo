package br.com.qa.tests;

import br.com.qa.pages.CartPage;
import br.com.qa.pages.InventoryPage;
import br.com.qa.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlternativeFlowTest extends BaseTest {

    @Test
    @DisplayName("Deve permitir remover produto após adicioná-lo ao carrinho")
    void shouldRemoveProductFromCart() {

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);

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

        inventoryPage.removeBackpackFromCart();

        assertFalse(
                inventoryPage.isCartBadgeDisplayed()
        );
    }

    @Test
    @DisplayName("Deve permitir voltar às compras a partir do carrinho")
    void shouldContinueShoppingFromCart() {

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

        assertEquals(
                "Sauce Labs Backpack",
                cartPage.getProductName()
        );

        cartPage.continueShopping();

        assertTrue(inventoryPage.isLoaded());
    }
}
