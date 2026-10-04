# QA Automation - Selenium Java - SauceDemo

[![Automated Tests](https://github.com/jaquelineleite/qa-automation-selenium-java-saucedemo/actions/workflows/automated-tests.yml/badge.svg)](https://github.com/jaquelineleite/qa-automation-selenium-java-saucedemo/actions/workflows/automated-tests.yml)

Projeto de Quality Engineering voltado à automação Web com **Java, Selenium WebDriver, JUnit 5 e Maven**.

O projeto utiliza o SauceDemo como aplicação de referência para demonstrar não apenas execução automatizada de testes, mas também decisões relacionadas a **arquitetura, risco, CI/CD, observabilidade, investigação de falhas, métricas e governança de qualidade**.

A suíte possui **12 execuções de testes de produto**, organizadas entre smoke e regressão, com suporte a **Google Chrome, Mozilla Firefox e Microsoft Edge**.

---

## O que este projeto demonstra

O objetivo do projeto é aplicar práticas de engenharia de qualidade em uma suíte Web de tamanho controlado.

Entre os principais conceitos demonstrados estão:

- arquitetura de automação com separação de responsabilidades;
- gerenciamento centralizado do WebDriver;
- configuração centralizada;
- isolamento do WebDriver por thread;
- execução paralela controlada;
- estratégia de testes baseada em risco;
- separação entre smoke e regressão;
- execução cross-browser;
- CI/CD com estratégia diferente para Pull Request e branch principal;
- dados de teste centralizados;
- logging estruturado;
- captura automática de evidências;
- classificação estruturada de falhas;
- observabilidade de falhas no ciclo de vida dos testes;
- métricas da execução;
- Quality Gate baseado em política;
- gestão de comportamento intermitente;
- critérios de entrada e saída;
- análise de risco residual;
- apoio técnico à decisão de Go/No-Go.

---

## Tecnologias

- Java 17
- Selenium WebDriver 4.50.0
- JUnit 5
- Maven
- Maven Surefire
- SLF4J
- Logback
- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Selenium Manager
- Git
- GitHub
- GitHub Actions

---

## Estratégia de Qualidade

A estratégia procura equilibrar velocidade de feedback, cobertura e risco.

Os testes de produto recebem a tag `regression`.

Os cenários considerados críticos também recebem a tag `smoke`.

### Smoke

O smoke contém os principais fluxos necessários para fornecer feedback rápido sobre comportamentos críticos:

- login válido;
- adição de produto ao carrinho;
- conclusão da compra.

Atualmente o smoke corresponde a **3 execuções**.

### Regression

A regressão contempla todos os cenários automatizados de produto.

Atualmente corresponde a **12 execuções**.

A separação permite aplicar estratégias diferentes no pipeline sem executar indiscriminadamente toda a matriz de regressão a cada alteração.

---

## Cobertura Automatizada

### Autenticação

- login com credenciais válidas;
- login sem usuário;
- login sem senha;
- login com credenciais inválidas;
- login com usuário bloqueado.

### Carrinho

- adicionar produto;
- validar produto adicionado;
- validar contador;
- remover produto;
- continuar comprando.

### Checkout

- realizar compra completa;
- validar produto no resumo;
- finalizar compra;
- validar confirmação;
- impedir checkout sem nome;
- impedir checkout sem sobrenome;
- impedir checkout sem CEP.

---

## Arquitetura

A automação separa responsabilidades entre configuração, dados, páginas, testes, observabilidade e avaliação dos sinais de qualidade.

```text
Test Strategy
     |
Smoke / Regression
     |
Product Tests
     |
Page Objects
     |
BasePage
     |
Selenium WebDriver
     |
DriverFactory
     |
TestConfig / TestData
```

### DriverFactory

Centraliza a criação, acesso e encerramento do WebDriver.
Utiliza ThreadLocal<WebDriver> para manter a referência do navegador associada à thread de execução.
Oferece suporte a Chrome, Firefox e Edge.
### TestConfig
Centraliza configurações da execução:
- URL base;
- navegador;
- modo headless;
- timeout de carregamento.
As configurações podem ser alteradas por propriedades da execução sem espalhar valores pelo código.
### TestData
Centraliza dados reutilizáveis e reduz duplicação entre cenários.
Dados específicos de determinado cenário podem permanecer próximos ao teste quando isso melhora sua legibilidade.
### BaseTest
Centraliza o ciclo de vida comum dos testes Web:
- inicialização do navegador;
- navegação inicial;
- acesso ao WebDriver;
- encerramento da sessão.
### BasePage
Concentra comportamentos reutilizáveis do Selenium, incluindo esperas explícitas, interações e validações de carregamento.
### Page Objects
Encapsulam seletores e comportamentos das páginas.
As assertions permanecem nos testes, separando interação com a interface da validação do comportamento esperado.

## Paralelismo e Isolamento

O projeto utiliza paralelismo controlado através do JUnit 5.

```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=same_thread
junit.jupiter.execution.parallel.mode.classes.default=concurrent
junit.jupiter.execution.parallel.config.strategy=fixed
junit.jupiter.execution.parallel.config.fixed.parallelism=2
```

Classes diferentes podem executar concorrentemente, enquanto métodos da mesma classe permanecem sequenciais.
O ThreadLocal<WebDriver> evita compartilhar a mesma referência de WebDriver entre threads.
O paralelismo é tratado como decisão de engenharia e não como solução automática para reduzir o tempo do pipeline. Antes de aumentá-lo, devem ser considerados isolamento, dados, dependências e estabilidade da suíte.
## Observabilidade
Uma falha automatizada precisa fornecer informação suficiente para investigação.
### Structured Logging
SLF4J e Logback registram eventos relevantes da execução, incluindo:
- início e término dos testes;
- criação e encerramento do WebDriver;
- falhas;
- captura de screenshots;
- métricas;
- resultado do Quality Gate.
### Screenshots
Falhas geram tentativa automática de captura de screenshot.
A geração da evidência é tratada de forma a não substituir a exceção original caso a própria captura apresente problema.
### Lifecycle Failure Observability
A extensão de falhas observa não apenas exceções originadas no corpo do teste, mas também falhas relacionadas ao ciclo de vida JUnit, incluindo BeforeEach e AfterEach.
Isso permite preservar contexto quando uma execução falha durante preparação ou encerramento.

## Classificação de Falhas

O projeto possui classificação estruturada para apoiar a investigação de falhas.

Categorias atuais:

```text
SETUP_OR_INFRASTRUCTURE
ASSERTION
BROWSER_INTERACTION
TEARDOWN
UNKNOWN
```

A classificação fornece contexto inicial para análise.
Ela não representa automaticamente a causa raiz e também não determina, isoladamente, se existe defeito no produto ou teste flaky.
A investigação deve considerar stack trace, logs, evidências, etapa da execução, recorrência e reprodutibilidade.
## Métricas de Qualidade
As execuções de produto geram métricas como:
- total executado;
- aprovados;
- falhos;
- pass rate;
- total crítico;
- críticos aprovados;
- críticos falhos;
- critical pass rate;
- duração da execução.
A tag regression identifica os testes considerados nas métricas de produto.
A tag smoke identifica os cenários críticos.
Testes internos das regras do framework não são utilizados para compor as métricas de produto.
As métricas representam sinais da execução automatizada e não devem ser interpretadas isoladamente como percentual absoluto de qualidade do produto.
## Quality Gate
O projeto possui uma política explícita de Quality Gate.
Política atual deste laboratório:
Overall pass rate >= 95%
Critical pass rate = 100%
Critical failures = 0

Esses valores representam uma decisão deste projeto e não um padrão universal.
O resultado da avaliação é registrado como:
QUALITY GATE status=PASSED

ou:
QUALITY GATE status=FAILED

O Quality Gate funciona como sinal técnico para apoiar a avaliação de risco.
Ele não identifica automaticamente a causa de uma falha e não substitui análise de contexto, cobertura, defeitos conhecidos, testes não executados ou riscos residuais.
A falha dos testes continua sendo tratada pelo Maven/Surefire no pipeline. O Quality Gate implementado no projeto registra a avaliação da política de qualidade e não deve ser confundido com o mecanismo que, isoladamente, bloqueia o pipeline.

## CI/CD Baseada em Risco

O workflow é executado em:

- Pull Requests para `main`;
- push para `main`;
- execução manual.

### Pull Request

Para fornecer feedback rápido sem abandonar cobertura cross-browser, são executados:

```text
smoke      - Chrome
smoke      - Firefox
smoke      - Edge
regression - Chrome
```

A estratégia utiliza fail-fast: false, permitindo observar o resultado dos demais jobs mesmo quando um deles falha.
### Main / Execução Manual
Na branch principal e em execução manual é realizada a regressão cross-browser:
regression - Chrome
regression - Firefox
regression - Edge

Os relatórios Surefire são preservados como artifacts.
Screenshots são armazenados quando houver falha e evidências disponíveis.
## Gestão de Instabilidade
O projeto diferencia conceitos que frequentemente são tratados como equivalentes.
Uma falha intermitente não é automaticamente:
- defeito do produto;
- teste flaky;
- problema de infraestrutura.
Da mesma forma, uma execução que falha e passa posteriormente no mesmo commit representa evidência de comportamento intermitente, mas não determina sozinha sua causa.
A investigação considera:
- logs;
- stack trace;
- screenshots;
- etapa da falha;
- classificação;
- navegador;
- ambiente;
- recorrência;
- reprodutibilidade.
### Retry
Retry não é utilizado como correção genérica para instabilidade.
Aplicá-lo indiscriminadamente pode esconder problemas de sincronização, dados, dependências ou ambiente.
### Quarentena
Quarentena deve ser uma medida controlada e temporária, com evidência, responsável, impacto conhecido e critério de saída.

## Governança da Qualidade

Os sinais produzidos pela automação apoiam decisões de engenharia, mas não substituem julgamento técnico.

Uma decisão de Go/No-Go deve considerar, entre outros fatores:

- resultado da automação;
- criticidade das falhas;
- cobertura executada;
- cenários não executados;
- defeitos conhecidos;
- resultados exploratórios;
- riscos residuais;
- contexto de negócio.

Um Quality Gate aprovado não significa automaticamente ausência de risco.

Um Quality Gate reprovado também não determina sozinho a causa ou a decisão final de release.

O papel de QA/QE inclui consolidar evidências, tornar riscos visíveis e fornecer recomendação técnica.

A responsabilidade pela qualidade é compartilhada pelo time.

---

## Estrutura do Projeto

```text
src/test/java/br/com/qa/
├── config/
│   ├── DriverFactory.java
│   └── TestConfig.java
├── data/
│   ├── CheckoutData.java
│   ├── TestData.java
│   └── UserCredentials.java
├── failure/
│   ├── FailureCategory.java
│   ├── FailureClassification.java
│   ├── FailureClassifier.java
│   └── FailureClassifierTest.java
├── metrics/
│   └── QualityMetricsExtension.java
├── pages/
│   ├── BasePage.java
│   ├── CartPage.java
│   ├── CheckoutCompletePage.java
│   ├── CheckoutOverviewPage.java
│   ├── CheckoutPage.java
│   ├── InventoryPage.java
│   └── LoginPage.java
├── quality/
│   ├── QualityGateEvaluator.java
│   ├── QualityGateEvaluatorTest.java
│   ├── QualityGatePolicy.java
│   ├── QualityGateResult.java
│   └── QualityMetrics.java
├── tests/
│   ├── AlternativeFlowTest.java
│   ├── BaseTest.java
│   ├── CartTest.java
│   ├── CheckoutValidationTest.java
│   ├── LoginTest.java
│   └── PurchaseTest.java
└── utils/
    ├── ScreenshotOnFailureExtension.java
    └── ScreenshotUtils.java

src/test/resources/
├── META-INF/services/org.junit.jupiter.api.extension.Extension
├── junit-platform.properties
└── logback-test.xml
```

---

## Execução

### Pré-requisitos

- Java 17 ou superior;
- Maven;
- navegador correspondente para execução local.

Clone o projeto:

```bash
git clone https://github.com/jaquelineleite/qa-automation-selenium-java-saucedemo.git
cd qa-automation-selenium-java-saucedemo
```

### Regression - Chrome
mvn clean verify -Dgroups=regression -Dbrowser=chrome

### Smoke - Chrome
mvn clean verify -Dgroups=smoke -Dbrowser=chrome

### Regression - Firefox
mvn clean verify -Dgroups=regression -Dbrowser=firefox

### Regression - Edge
mvn clean verify -Dgroups=regression -Dbrowser=edge

### Execução com navegador visível
mvn clean verify -Dgroups=regression -Dbrowser=chrome -Dheadless=false

## Evidências e Relatórios
O relatório HTML é gerado em:
target/reports/surefire.html

Os relatórios nativos do Surefire ficam em:
target/surefire-reports/

Screenshots de falhas são armazenados em:
screenshots/

No GitHub Actions, relatórios e evidências aplicáveis são preservados como artifacts da execução.
## Documentação de QA
O projeto possui documentação complementar sobre estratégia e governança de qualidade:
- [Conceitos de Testes e Qualidade](docs/01-conceitos-de-testes.md)
- [Plano de Testes](docs/02-plano-de-testes.md)
- [Estratégia de Testes](docs/03-estrategia-de-testes.md)
- [Gerenciamento de Incidentes](docs/04-gerenciamento-de-incidentes.md)
- [Gestão de Instabilidade da Automação](docs/05-gestao-de-instabilidade-da-automacao.md)
## Decisões e Limitações
O escopo deste laboratório é deliberadamente focado em automação Web.
### Quality Gate
O Quality Gate fornece um sinal técnico da execução. Ele não realiza, isoladamente, a decisão de release.
### Failure Classification
A classificação de falhas auxilia a investigação e a análise de causa, mas uma categoria não representa automaticamente a causa raiz.
### Instabilidade
Comportamento intermitente exige evidências, recorrência e investigação.
Uma falha seguida de sucesso em nova execução do mesmo commit é evidência de intermitência, mas não é suficiente para determinar automaticamente a causa ou classificar um teste como flaky.
### Retry
Retry não foi adotado como solução genérica para falhas intermitentes.
### Histórico de Métricas
As métricas representam a execução corrente.
O projeto não implementa persistência ou análise histórica de tendências. Essa capacidade exigiria uma estratégia própria de armazenamento, identificação e agregação dos resultados entre execuções.
### Escopo
Testes de API, mobile e performance não fazem parte deste laboratório Web.
Essas capacidades podem ser demonstradas em projetos específicos ao respectivo contexto, evitando adicionar complexidade sem necessidade.
## Princípios Aplicados
Falhou?
   |
Preservar evidência
   |
Classificar o sinal
   |
Investigar
   |
Buscar causa
   |
Avaliar recorrência e risco
   |
Corrigir / mitigar / aceitar risco
   |
Validar novamente

O objetivo não é apenas fazer testes passarem, mas produzir feedback confiável para apoiar decisões de engenharia.
## Autor
Jaqueline Fernandes de Andrade
QA | Quality Engineering | Test Automation
