package br.com.qa.tests;

import br.com.qa.data.TestData;

import br.com.qa.pages.CartPage;
import br.com.qa.pages.InventoryPage;
import br.com.qa.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("regression")
class CartTest extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("Deve adicionar produto ao carrinho")
    void shouldAddProductToCart() {

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.login(
                TestData.STANDARD_USER.username(),
                TestData.STANDARD_USER.password()
        );

        inventoryPage.addBackpackToCart();

        assertEquals(
                "1",
                inventoryPage.getCartItemCount(),
                "O carrinho deveria conter um produto."
        );

        inventoryPage.openCart();

        assertTrue(
                cartPage.isLoaded(),
                "A página do carrinho deveria ser exibida."
        );

        assertTrue(
                cartPage.hasItem(),
                "O produto deveria estar presente no carrinho."
        );

        assertEquals(
                "Sauce Labs Backpack",
                cartPage.getProductName(),
                "O produto adicionado ao carrinho não corresponde ao esperado."
        );
    }
}
