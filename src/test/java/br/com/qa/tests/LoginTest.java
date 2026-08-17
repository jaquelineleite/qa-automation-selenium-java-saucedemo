package br.com.qa.tests;

import br.com.qa.pages.InventoryPage;
import br.com.qa.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest extends BaseTest {

    @Test
    @DisplayName("Deve realizar login com credenciais válidas")
    void shouldLoginWithValidCredentials() {

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);

        loginPage.login(
                "standard_user",
                "secret_sauce"
        );

        assertTrue(
                inventoryPage.isLoaded(),
                "A página de produtos deveria ser exibida após o login."
        );
    }

    @ParameterizedTest(name = "{index} - Usuário: {0}")
    @CsvSource(
            value = {
                    "'NULL' | secret_sauce | Epic sadface: Username is required",
                    "standard_user | 'NULL' | Epic sadface: Password is required",
                    "invalid_user | invalid_password | Epic sadface: Username and password do not match any user in this service",
                    "locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out."
            },
            delimiter = '|',
            nullValues = "NULL"
    )
    @DisplayName("Deve validar cenários de exceção no login")
    void shouldValidateLoginExceptions(
            String username,
            String password,
            String expectedMessage
    ) {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                username == null ? "" : username,
                password == null ? "" : password
        );

        assertTrue(
                loginPage.isErrorMessageDisplayed(),
                "A mensagem de erro deveria ser exibida."
        );

        assertEquals(
                expectedMessage,
                loginPage.getErrorMessage(),
                "A mensagem de erro apresentada não corresponde ao esperado."
        );
    }
}
