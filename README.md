# QA Automation - Selenium Java - SauceDemo

[![Automated Tests](https://github.com/jaquelineleite/qa-automation-selenium-java-saucedemo/actions/workflows/automated-tests.yml/badge.svg)](https://github.com/jaquelineleite/qa-automation-selenium-java-saucedemo/actions/workflows/automated-tests.yml)

Projeto de automação de testes Web desenvolvido com **Java, Selenium WebDriver, JUnit 5 e Maven**, utilizando **Page Object Pattern** e práticas voltadas à manutenção, isolamento, investigação de falhas e execução contínua.

A suíte automatiza fluxos críticos de uma aplicação de e-commerce, contemplando cenários principais, alternativos e de exceção.

Atualmente o projeto possui **12 testes automatizados**, execução cross-browser em **Google Chrome, Mozilla Firefox e Microsoft Edge**, modo headless, paralelismo controlado, captura automática de evidências em falhas, relatório HTML e pipeline de CI/CD.

---

## Tecnologias

- Java 17
- Selenium WebDriver 4.50.0
- JUnit 5
- Maven
- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Selenium Manager
- Page Object Pattern
- ThreadLocal WebDriver
- Maven Surefire
- Git e GitHub
- GitHub Actions

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

A mesma suíte de **12 testes automatizados** é executada nos três navegadores suportados:

- Google Chrome
- Mozilla Firefox
- Microsoft Edge

A matriz cross-browser foi validada no pipeline de Pull Request e novamente na branch `main`, com os três jobs concluídos com sucesso.

Resultado esperado por navegador:

```text
Tests run: 12
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

---

## Estratégia de Testes

A automação contempla três grupos principais.

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

src/test/resources/
└── junit-platform.properties
```

---

## Arquitetura da Automação

O projeto separa responsabilidades para facilitar manutenção, evolução e investigação de falhas.

```text
Testes / Assertions
        ↓
Page Objects
        ↓
BasePage
        ↓
Selenium WebDriver
        ↓
DriverFactory
```

### DriverFactory

Centraliza a criação e o encerramento do WebDriver.

A implementação utiliza `ThreadLocal<WebDriver>` para manter uma instância de navegador associada a cada thread de execução e remove essa referência durante o encerramento do driver.

A `DriverFactory` oferece suporte a **Google Chrome, Mozilla Firefox e Microsoft Edge**, mantendo centralizada a criação e o ciclo de vida dos diferentes WebDrivers.

### BaseTest

Centraliza o ciclo de vida dos testes, incluindo preparação da execução, acesso ao WebDriver e encerramento do navegador.

### BasePage

Concentra comportamentos reutilizáveis do Selenium, incluindo:

- esperas explícitas;
- espera por elementos visíveis e clicáveis;
- espera por alteração de URL;
- ações reutilizáveis de clique e preenchimento;
- fallback de clique via JavaScript quando a navegação esperada não ocorre;
- validação reutilizável de carregamento das páginas.

Timeouts esperados durante verificações de estado são tratados de forma específica, evitando transformar indiscriminadamente erros técnicos inesperados em um simples resultado `false`.

### Page Objects

Encapsulam seletores e comportamentos específicos das páginas.

As validações de carregamento reutilizam a estratégia centralizada da `BasePage`, reduzindo duplicação e mantendo um comportamento consistente entre as páginas.

### Testes

Os testes representam os cenários automatizados e mantêm as validações relacionadas ao comportamento esperado da aplicação.

---

## Execução Paralela Controlada

O projeto utiliza paralelismo controlado através do JUnit 5.

Configuração atual:

```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=same_thread
junit.jupiter.execution.parallel.mode.classes.default=concurrent
junit.jupiter.execution.parallel.config.strategy=fixed
junit.jupiter.execution.parallel.config.fixed.parallelism=2
```

Com essa estratégia:

- classes de teste diferentes podem executar concorrentemente;
- métodos pertencentes à mesma classe permanecem sequenciais;
- o paralelismo é limitado a duas threads;
- cada thread mantém sua própria referência de WebDriver através de `ThreadLocal`.

Essa configuração foi adotada de forma controlada, preservando a previsibilidade da suíte e permitindo avaliar o comportamento da automação sob concorrência.

---

## Execução dos Testes

### Pré-requisitos

- Java 17 ou superior
- Maven
- Google Chrome, Mozilla Firefox ou Microsoft Edge para execução local no navegador correspondente

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

O nome da evidência contém informações que auxiliam na identificação da execução:

```text
teste-thread-id-timestamp.png
```

A identificação da thread e o timestamp com milissegundos ajudam a diferenciar evidências geradas em execuções concorrentes.

A captura de evidência foi validada também através de falha controlada, verificando a geração do screenshot sem ocultar a falha original do teste.

---

## Execução Headless

Por padrão, os testes são executados em modo headless, adequado para GitHub Codespaces e pipelines CI/CD.

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

O navegador pode ser informado através da propriedade `browser`.

```text
-Dbrowser=chrome
-Dbrowser=firefox
-Dbrowser=edge
```

A implementação oferece suporte a **Chrome, Firefox e Edge**, utilizando a mesma suíte automatizada.

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
- separação de responsabilidades
- esperas explícitas
- ausência de `Thread.sleep()`
- centralização de comportamentos reutilizáveis
- centralização do ciclo de vida do WebDriver
- isolamento do WebDriver por thread
- paralelismo controlado
- testes independentes
- validação centralizada de carregamento das páginas
- tratamento específico de timeout nas verificações de estado
- execução headless
- screenshots automáticos em falhas
- identificação das evidências por teste, thread e timestamp
- relatório HTML
- priorização de cenários por risco
- CI/CD como mecanismo de feedback e validação automatizada

---

## Integração Contínua

O projeto possui pipeline configurado com **GitHub Actions**, executado automaticamente em `push` e `pull_request` para a branch `main`.

O workflow utiliza uma **matriz cross-browser** para executar a mesma suíte em **Google Chrome, Mozilla Firefox e Microsoft Edge**.

Para cada navegador, o pipeline realiza:

- checkout do repositório;
- configuração do Java 17;
- execução da suíte com `mvn clean verify -Dbrowser=<browser>`;
- geração e armazenamento do relatório de testes em artifact específico por navegador (`test-report-<browser>`);
- armazenamento de screenshots em artifact específico por navegador (`failure-screenshots-<browser>`) quando houver falhas.

A estratégia utiliza `fail-fast: false`, permitindo que os jobs dos demais navegadores continuem mesmo se um deles apresentar falha.

Essa execução fornece validação automatizada cross-browser e feedback contínuo sobre as alterações enviadas ao repositório.

O status da execução pode ser acompanhado pelo badge disponível no início deste README.

---

## Próximas Evoluções

- ampliação da cobertura com testes de API;
- inclusão de testes de performance;
- publicação navegável do relatório de testes;
- evolução da estratégia de dados para cenários com maior concorrência.

---

## Autor

**Jaqueline Fernandes de Andrade**

QA | Quality Assurance | Test Automation

GitHub: [jaquelineleite](https://github.com/jaquelineleite)