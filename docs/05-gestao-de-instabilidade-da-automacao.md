# Gestão de Instabilidade da Automação

## 1. Objetivo

Definir critérios para identificar, investigar, registrar e tratar comportamentos instáveis observados durante a execução dos testes automatizados.

O objetivo não é fazer uma execução falha tornar-se verde artificialmente, mas aumentar a confiabilidade dos sinais fornecidos pela automação e apoiar decisões baseadas em evidências.

Uma falha intermitente não deve ser automaticamente classificada como defeito do produto nem como teste flaky.

---

## 2. Princípio

Quando uma execução automatizada falha, a primeira pergunta não deve ser:

> Como fazer esse teste passar?

A investigação deve buscar responder:

> O que essa falha representa e quais evidências sustentam essa conclusão?

A falha pode estar relacionada a:

- comportamento do produto;
- assertion;
- interação com navegador;
- preparação do teste;
- infraestrutura;
- dados compartilhados;
- sincronização;
- teardown;
- ambiente;
- ou causa ainda desconhecida.

---

## 3. Conceitos

### Suspeita de Instabilidade

Uma ocorrência isolada que apresenta comportamento diferente do esperado, mas ainda não possui histórico suficiente para determinar recorrência ou causa.

### Comportamento Intermitente

Situação em que, sob condições equivalentes, uma execução pode falhar e posteriormente passar sem uma alteração que explique diretamente a mudança de resultado.

A intermitência é uma característica observada do comportamento e não determina, sozinha, sua causa raiz.

### Teste Flaky

Teste cuja instabilidade possui evidências relacionadas ao próprio teste, sua implementação, seus dados, sincronização, isolamento ou dependências controladas pelo framework de testes.

Um teste não deve ser classificado como flaky apenas porque passou em um rerun.

### Instabilidade de Browser, Ambiente ou Infraestrutura

Falha cuja evidência aponta para fatores externos ao comportamento funcional validado pelo teste, como inicialização do navegador, comunicação com o driver, runner, indisponibilidade de ambiente ou falhas ocorridas durante o setup.

Essa classificação também não representa automaticamente a causa raiz definitiva.

---

## 4. Evidências para Investigação

Uma investigação deve considerar, quando disponíveis:

- teste afetado;
- suíte executada;
- navegador;
- ambiente;
- commit;
- etapa em que ocorreu a falha;
- tipo da exceção;
- categoria da falha;
- mensagem de erro;
- stack trace;
- logs;
- screenshot;
- relatório de execução;
- resultado de execuções anteriores;
- resultado de uma nova execução do mesmo commit;
- quantidade de testes afetados;
- existência de padrão entre as falhas.

A decisão deve utilizar o conjunto das evidências e não apenas uma mensagem isolada.

---

## 5. Processo de Investigação

### Primeira Ocorrência

Na primeira ocorrência:

1. preservar as evidências;
2. identificar a categoria da falha;
3. localizar a etapa em que ocorreu;
4. verificar se outros testes apresentaram o mesmo comportamento;
5. analisar logs, stack trace e artefatos;
6. evitar alterações imediatas sem evidência da causa.

Uma única ocorrência não é suficiente para classificar automaticamente um teste como flaky.

### Falha seguida de sucesso no mesmo commit

Quando uma execução falha e o mesmo commit passa posteriormente sem alteração de código:

1. registrar o comportamento como evidência de intermitência;
2. comparar as duas execuções;
3. verificar navegador, ambiente, etapa e exceção;
4. procurar ocorrências anteriores;
5. manter a causa como não comprovada enquanto as evidências forem insuficientes.

O sucesso do rerun não apaga a falha original.

---

## 6. Classificação e RCA

A classificação automática é um sinal para investigação e não substitui a análise de causa raiz.

As categorias utilizadas pelo projeto ajudam a separar diferentes tipos de ocorrência:

- `SETUP_OR_INFRASTRUCTURE`;
- `ASSERTION`;
- `BROWSER_INTERACTION`;
- `TEARDOWN`;
- `UNKNOWN`.

Uma categoria indica onde ou como a falha foi observada.

Ela não prova, isoladamente, por que a falha aconteceu.

A análise de causa raiz deve correlacionar classificação, histórico, logs, ambiente, comportamento do teste e demais evidências disponíveis.

---

## 7. Retry

Retry não deve ser utilizado como correção automática para testes instáveis.

Um retry indiscriminado pode:

- esconder defeitos reais;
- reduzir a confiança na suíte;
- mascarar problemas de sincronização;
- ocultar dependências entre testes;
- aumentar o tempo de execução;
- transformar falhas recorrentes em resultados aparentemente verdes.

Quando utilizado de forma excepcional, deve existir uma justificativa explícita e a falha original deve continuar rastreável.

Retry é uma mitigação ou recurso de diagnóstico, não uma demonstração de que a causa foi corrigida.

---

## 8. Quarentena

A quarentena deve ser considerada somente quando uma instabilidade conhecida comprometer repetidamente a confiabilidade do pipeline e não puder ser corrigida imediatamente.

Uma quarentena deve possuir:

- justificativa;
- evidências;
- responsável;
- impacto conhecido;
- acompanhamento;
- critério para remoção da quarentena.

Testes críticos não devem ser silenciosamente removidos da estratégia de qualidade.

A quarentena é temporária e não substitui a correção da causa.

---

## 9. Critérios de Decisão

### Investigar

Quando:

- ocorre uma falha nova;
- a causa ainda não está clara;
- existe comportamento intermitente;
- diferentes testes apresentam o mesmo padrão;
- a falha ocorre em setup, browser ou infraestrutura.

### Bloquear

Pode ser necessário quando:

- um fluxo crítico apresenta falha reproduzível;
- existe risco relevante para o negócio;
- a confiabilidade da execução é insuficiente para apoiar a liberação;
- a causa representa risco não aceito pelo time.

### Acompanhar

Pode ser adequado quando:

- existe evidência de intermitência;
- o impacto está conhecido;
- não existe evidência suficiente para atribuir causa;
- novas execuções podem fornecer dados úteis para a investigação.

### Quarentenar

Somente quando:

- a instabilidade é recorrente;
- está identificada e rastreada;
- prejudica continuamente o sinal do pipeline;
- existe decisão explícita do time;
- há plano para tratamento.

---

## 10. Exemplo Real do Projeto

Durante uma regressão no navegador Edge, múltiplos testes apresentaram `TimeoutException` durante o `BaseTest.setUp()`.

A mensagem observada indicava timeout na comunicação com o renderer do navegador.

Na primeira execução:

- parte da regressão falhou;
- as falhas ocorreram durante o setup;
- as métricas registraram redução da taxa de aprovação;
- o Quality Gate apresentou status `FAILED`.

O mesmo commit foi executado novamente sem alteração de código e a execução posterior foi aprovada.

A conclusão suportada pelas evidências foi:

- houve comportamento intermitente;
- a falha ocorreu durante o setup;
- não havia evidência suficiente para classificar os testes funcionais afetados como flaky;
- não havia evidência suficiente para afirmar uma causa raiz específica;
- retry automático não foi adicionado.

O incidente também revelou uma lacuna de observabilidade: falhas de lifecycle não estavam sendo tratadas pelo mecanismo existente de classificação.

Como melhoria, a extensão de observabilidade passou a considerar falhas de `BeforeEach` e `AfterEach`, permitindo que ocorrências futuras desse tipo sejam classificadas e registradas sem mascarar a exceção original.

---

### 10.1 Investigação com causa raiz comprovada — Firefox

Em uma execução cross-browser no Firefox, os testes falharam durante o `BaseTest.setUp()` antes de alcançar os fluxos funcionais da aplicação.

O sintoma observado foi uma `SessionNotCreatedException`, indicando que o processo do Firefox era encerrado durante a criação da sessão do WebDriver.

A classificação da falha apontou `SETUP_OR_INFRASTRUCTURE`. Essa classificação foi utilizada como sinal para direcionar a investigação, e não como conclusão automática sobre a causa raiz.

A investigação foi realizada de forma incremental:

1. a falha foi observada na execução completa com Firefox;
2. o mesmo cenário foi executado isoladamente e sem paralelismo;
3. a falha continuou ocorrendo, reduzindo a hipótese de concorrência como causa;
4. o ambiente de execução e os binários utilizados pelo Selenium foram inspecionados;
5. o Firefox gerenciado pelo Selenium foi executado diretamente;
6. a execução revelou ausência da biblioteca de sistema `libgtk-3.so.0`;
7. a dependência GTK3 foi instalada no ambiente;
8. o mesmo teste isolado foi executado novamente com sucesso;
9. a suíte completa foi executada novamente no Firefox com sucesso.

Após a correção do ambiente:

- o teste isolado executou com `1` teste aprovado, sem falhas ou erros;
- a execução completa apresentou `24` testes aprovados, sem falhas ou erros;
- o paralelismo permaneceu limitado aos dois workers configurados;
- nenhuma alteração funcional nos testes foi necessária para corrigir o problema;
- retry não foi utilizado para mascarar a falha.

Nesse incidente, `SessionNotCreatedException` representava o sintoma observado. A causa raiz comprovada foi a ausência da dependência de sistema necessária para inicialização do Firefox naquele ambiente de execução.

O caso demonstra a importância de separar falha funcional, falha da automação e falha de infraestrutura. Também demonstra que a classificação automática auxilia a investigação, mas a causa raiz deve ser estabelecida a partir de evidências adicionais.

---

## 11. Responsabilidade de Quality Engineering

A gestão de instabilidade não consiste apenas em manter o pipeline verde.

Quality Engineering deve:

- preservar sinais confiáveis;
- diferenciar sintomas de causas;
- evitar conclusões sem evidência;
- tornar falhas investigáveis;
- acompanhar recorrência;
- comunicar risco;
- propor melhorias estruturais;
- evitar mecanismos que apenas escondam problemas.

A confiabilidade da automação faz parte da qualidade do processo de engenharia.

---

## 12. Conclusão

Uma suíte confiável não é aquela que nunca apresenta falhas.

É aquela em que uma falha fornece informações suficientes para investigação e tomada de decisão.

Comportamentos intermitentes devem ser tratados com evidências, histórico e análise de risco.

Retry e quarentena podem existir em contextos específicos, mas não devem substituir investigação, análise de causa raiz e melhoria contínua.
