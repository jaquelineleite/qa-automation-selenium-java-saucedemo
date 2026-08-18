# Plano de Testes

## 1. Objetivo

Este plano de testes tem como objetivo validar os principais fluxos funcionais de uma aplicação Web de e-commerce, considerando autenticação, carrinho e processo de compra.

A estratégia contempla cenários principais, alternativos e de exceção, utilizando Java, Selenium WebDriver, JUnit 5 e o padrão Page Object.

---

## 2. Escopo

### Autenticação

- login com credenciais válidas;
- login sem usuário;
- login sem senha;
- login com credenciais inválidas;
- login com usuário bloqueado.

### Catálogo e Carrinho

- adicionar produto ao carrinho;
- validar contador do carrinho;
- validar produto adicionado;
- remover produto;
- retornar do carrinho para continuar comprando.

### Checkout

- iniciar checkout;
- preencher dados do cliente;
- validar campos obrigatórios;
- validar produto no resumo da compra;
- finalizar compra;
- validar confirmação da compra.

---

## 3. Fora do Escopo

Não fazem parte desta implementação:

- testes de performance;
- testes de carga;
- testes de segurança;
- testes de acessibilidade;
- testes de API;
- testes mobile;
- validações de banco de dados;
- validações de infraestrutura.

Esses testes podem ser incorporados futuramente de acordo com riscos, requisitos e necessidades do produto.

---

## 4. Ambiente

Aplicação utilizada:

`https://www.saucedemo.com/`

Tecnologias:

- Java 17;
- Selenium WebDriver;
- JUnit 5;
- Maven;
- Google Chrome;
- Page Object Pattern.

Os testes são executados em modo headless, permitindo futura integração com pipelines de CI/CD.

### Massa de Dados / Dados Fictícios

Para execução dos testes serão utilizados dados controlados e fictícios, adequados aos cenários propostos.

#### Autenticação

| Cenário | Usuário | Senha |
|---|---|---|
| Usuário válido | standard_user | secret_sauce |
| Usuário bloqueado | locked_out_user | secret_sauce |
| Credenciais inválidas | usuario_invalido | senha_invalida |
| Usuário não informado | vazio | secret_sauce |
| Senha não informada | standard_user | vazio |

#### Checkout

| Campo | Dado fictício |
|---|---|
| Nome | Jaqueline |
| Sobrenome | QA |
| CEP | 18150-000 |

#### Produto

Produto utilizado como referência nos testes:

`Sauce Labs Backpack`

Os dados podem ser alterados ou ampliados de acordo com a necessidade dos cenários, mantendo independência entre os testes e evitando dependência de dados produzidos por execuções anteriores.

---

## 5. Estratégia de Testes

### Fluxo Principal

Validação da jornada completa de compra:

Login → Produtos → Carrinho → Checkout → Dados do cliente → Resumo → Finalização → Confirmação

### Fluxos Alternativos

- adicionar e remover produto;
- acessar o carrinho e retornar para continuar comprando.

### Fluxos de Exceção

- usuário obrigatório;
- senha obrigatória;
- credenciais inválidas;
- usuário bloqueado;
- nome obrigatório no checkout;
- sobrenome obrigatório;
- CEP obrigatório.

---

## 6. Casos Automatizados

A suíte possui atualmente 12 testes automatizados.

### LoginTest

1. Login com credenciais válidas.
2. Login sem usuário.
3. Login sem senha.
4. Login com credenciais inválidas.
5. Login com usuário bloqueado.

### CartTest

6. Adicionar produto ao carrinho e validar seu conteúdo.

### PurchaseTest

7. Realizar compra completa com sucesso.

### CheckoutValidationTest

8. Impedir checkout sem nome.
9. Impedir checkout sem sobrenome.
10. Impedir checkout sem CEP.

### AlternativeFlowTest

11. Remover produto após adicioná-lo ao carrinho.
12. Voltar às compras a partir do carrinho.

---

## 7. Critérios de Entrada

- aplicação disponível;
- ambiente acessível;
- usuário de teste disponível;
- navegador configurado;
- dependências Maven instaladas;
- projeto compilando sem erros.

---

## 8. Critérios de Saída

A execução será considerada satisfatória quando:

- fluxos críticos forem executados;
- fluxo principal estiver aprovado;
- não existirem defeitos bloqueadores;
- defeitos de alta severidade forem avaliados;
- riscos conhecidos estiverem documentados;
- regressão crítica estiver aprovada.

A decisão de liberar uma versão deve considerar impacto, severidade, criticidade e risco para o negócio.

---

## 9. Evidências

Quando um teste automatizado falha, o framework realiza captura automática de screenshot.

As evidências são armazenadas em:

`screenshots/`

O nome do arquivo contém a classe, o teste e o horário da execução.

---

## 10. Relatório de Execução

Para executar os testes:

`mvn clean test`

Para executar os testes e gerar o relatório HTML:

`mvn clean verify`

Relatório gerado em:

`target/reports/surefire.html`

---

## 11. Resultado Atual

Última regressão completa:

- Testes executados: 12
- Falhas: 0
- Erros: 0
- Ignorados: 0
- Resultado: BUILD SUCCESS

---

## 12. Riscos

Principais riscos considerados:

- indisponibilidade do ambiente;
- alterações na interface;
- mudanças nos seletores;
- dependência de massa compartilhada;
- instabilidades de navegação;
- mudanças futuras no comportamento da aplicação.

Para reduzir esses riscos foram utilizados Page Objects, seletores centralizados, esperas explícitas, isolamento dos testes e screenshots automáticos em falhas.

---

## 13. Criticidade

### Crítica

- autenticação;
- inclusão de produto no carrinho;
- checkout;
- finalização da compra.

### Alta

- validações obrigatórias;
- usuário bloqueado;
- integridade do carrinho.

### Média

- remover produto;
- continuar comprando.

A criticidade pode ser revisada conforme impacto financeiro, frequência de uso e regras de negócio.