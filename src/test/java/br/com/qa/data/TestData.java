package br.com.qa.data;

public final class TestData {

    public static final UserCredentials STANDARD_USER =
            new UserCredentials(
                    "standard_user",
                    "secret_sauce"
            );

    public static final CheckoutData VALID_CHECKOUT =
            new CheckoutData(
                    "Jaqueline",
                    "QA",
                    "18150-000"
            );

    private TestData() {
        /*
         * Centraliza dados reutilizáveis dos cenários de teste.
         * Não deve ser instanciada.
         */
    }
}
