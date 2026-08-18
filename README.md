# QA Automation - Selenium Java - SauceDemo

Projeto de automação de testes Web desenvolvido com **Java, Selenium WebDriver, JUnit 5 e Maven**, utilizando o padrão **Page Object**.

A suíte automatiza fluxos críticos de uma aplicação de e-commerce, contemplando cenários principais, alternativos e de exceção.

Atualmente o projeto possui **12 testes automatizados**, execução headless, captura automática de screenshots em falhas e geração de relatório HTML.

---

## Tecnologias

- Java 17
- Selenium WebDriver 4
- JUnit 5
- Maven
- Google Chrome
- Page Object Pattern
- Maven Surefire
- Git e GitHub

---

## Cobertura Automatizada

### Autenticação

- Login com credenciais válidas
- Login sem usuário
- Login sem senha
- Login com credenciais inválidas
- Login com usuário bloqueado

### Carrinho

- Adicionar produto ao carrinho
- Validar produto adicionado
- Validar contador do carrinho
- Remover produto
- Continuar comprando a partir do carrinho

### Checkout

- Realizar compra completa
- Validar produto no resumo
- Finalizar compra
- Validar confirmação da compra
- Impedir checkout sem nome
- Impedir checkout sem sobrenome
- Impedir checkout sem CEP

---

## Resultado Atual

Última execução completa:

```text
Tests run: 12
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

---

## Estratégia de Testes

A automação contempla três grupos principais:

### Fluxo principal

Login → Produtos → Carrinho → Checkout → Dados do cliente → Resumo → Finalização → Confirmação

### Fluxos alternativos

- remoção de produto;
- retorno do carrinho para continuar comprando.

### Fluxos de exceção

- credenciais inválidas;
- usuário bloqueado;
- campos obrigatórios de login;
- campos obrigatórios do checkout.

---

## Estrutura do Projeto

```text
src/test/java/br/com/qa/
├── config/
│   └── DriverFactory.java
├── pages/
│   ├── BasePage.java
│   ├── LoginPage.java
│   ├── InventoryPage.java
│   ├── CartPage.java
│   ├── CheckoutPage.java
│   ├── CheckoutOverviewPage.java
│   └── CheckoutCompletePage.java
├── tests/
│   ├── BaseTest.java
│   ├── LoginTest.java
│   ├── CartTest.java
│   ├── PurchaseTest.java
│   ├── CheckoutValidationTest.java
│   └── AlternativeFlowTest.java
└── utils/
    ├── ScreenshotUtils.java
    └── ScreenshotOnFailureExtension.java
```

---

## Page Object Pattern

O projeto utiliza Page Object para separar elementos da interface, ações realizadas nas páginas e cenários de teste.

Essa abordagem reduz duplicação de código e facilita manutenção e evolução da automação.

---

## Execução dos Testes

### Pré-requisitos

- Java 17 ou superior
- Maven
- Google Chrome

Clone o projeto:

```bash
git clone https://github.com/jaquelineleite/qa-automation-selenium-java-saucedemo.git
```

Entre na pasta:

```bash
cd qa-automation-selenium-java-saucedemo
```

Execute toda a suíte:

```bash
mvn clean test
```

---

## Relatório HTML

Para executar os testes e gerar o relatório:

```bash
mvn clean verify
```

O relatório será gerado em:

```text
target/reports/surefire.html
```

---

## Screenshots em Falhas

Quando um teste falha, o framework captura automaticamente uma evidência antes do encerramento do navegador.

Os screenshots são armazenados em:

```text
screenshots/
```

---

## Execução Headless

Por padrão, os testes são executados em modo headless, sendo adequado para GitHub Codespaces e pipelines CI/CD.

```bash
mvn clean test
```

Em uma máquina local com ambiente gráfico disponível, também é possível executar exibindo o navegador:

```bash
mvn clean test -Dheadless=false
```

> Em ambientes sem interface gráfica, como GitHub Codespaces, utilize o modo headless.

---

## Configurações

A URL pode ser alterada através da propriedade:

```bash
-DbaseUrl=https://www.saucedemo.com/
```

Exemplo:

```bash
mvn clean test -DbaseUrl=https://www.saucedemo.com/
```

O navegador pode ser informado através de:

```bash
-Dbrowser=chrome
```

---

## Documentação de QA

Além da automação, o projeto possui documentação relacionada à estratégia de qualidade:

- [Conceitos de Testes e Qualidade](docs/01-conceitos-de-testes.md)
- [Plano de Testes](docs/02-plano-de-testes.md)
- [Estratégia de Testes](docs/03-estrategia-de-testes.md)
- [Gerenciamento de Incidentes](docs/04-gerenciamento-de-incidentes.md)

---

## Boas Práticas Aplicadas

- Page Object Pattern
- Esperas explícitas
- Ausência de `Thread.sleep()`
- Testes independentes
- Centralização de seletores
- Execução headless
- Screenshots automáticos em falhas
- Relatório HTML
- Priorização de cenários por risco
- Evidências de execução

---

## Próximas Evoluções

- execução em pipeline CI/CD;
- execução paralela;
- múltiplos navegadores;
- testes de API;
- testes de performance;
- publicação automática de relatórios;
- matriz de browsers.

---

## Autor

**Jaqueline Fernandes de Andrade**

QA | Quality Assurance | Test Automation

GitHub: [jaquelineleite](https://github.com/jaquelineleite)
