# Conceitos de Testes e Qualidade de Software

## 1. Qual a diferença entre Teste de Software e Qualidade de Software?

Teste de Software é o conjunto de atividades realizadas para verificar e validar se uma aplicação atende aos requisitos definidos e apresenta o comportamento esperado.

Os testes também têm como objetivo identificar defeitos, inconsistências, riscos e comportamentos inesperados antes que eles impactem o usuário final.

Qualidade de Software possui uma abrangência maior. Ela envolve todo o ciclo de desenvolvimento e busca prevenir problemas, melhorar processos e garantir que o produto entregue gere valor e atenda às necessidades técnicas e de negócio.

Dessa forma, os testes fazem parte da estratégia de qualidade, mas qualidade não se limita apenas à execução de testes.

Uma estratégia de qualidade pode envolver, por exemplo:

- definição clara de requisitos e critérios de aceite;
- participação de QA desde o refinamento;
- análise de riscos;
- revisão de código;
- testes manuais e automatizados;
- integração contínua;
- monitoramento;
- análise de métricas;
- prevenção de defeitos;
- melhoria contínua dos processos.

A responsabilidade pela qualidade deve ser compartilhada entre todos os integrantes do time.

---

## 2. Como seria um processo ideal de testes?

Um processo de testes deve começar antes da implementação da funcionalidade.

### Refinamento e análise dos requisitos

O QA participa do entendimento da necessidade de negócio, identifica dúvidas, inconsistências e possíveis riscos.

Também apoia a definição de critérios de aceite claros e testáveis.

### Planejamento dos testes

São definidos:

- escopo;
- tipos de teste;
- riscos;
- prioridades;
- ambientes;
- massa de dados;
- ferramentas;
- critérios de entrada e saída.

### Elaboração dos cenários

Os cenários devem considerar:

- fluxo principal;
- fluxos alternativos;
- cenários de exceção;
- regras de negócio;
- integrações;
- riscos identificados.

### Preparação do ambiente e massa

O ambiente deve estar disponível e configurado para permitir a execução dos testes.

Também devem ser preparadas massas adequadas para os cenários planejados.

### Automação

Devem ser priorizados para automação os cenários que tragam maior retorno, como:

- regressões frequentes;
- fluxos críticos;
- cenários repetitivos;
- funcionalidades estáveis;
- testes executados frequentemente em pipeline.

### Execução

Os testes são executados e os resultados registrados com evidências e rastreabilidade.

### Gestão de defeitos

Quando um problema é identificado, deve ser registrado contendo informações suficientes para reprodução e investigação.

### Reteste e regressão

Após a correção de um defeito, é realizado o reteste.

Também devem ser executados testes de regressão para verificar se a alteração não afetou outras funcionalidades.

### Encerramento

Ao final, os resultados devem ser avaliados considerando:

- testes executados;
- testes aprovados;
- testes reprovados;
- defeitos encontrados;
- riscos remanescentes;
- cobertura;
- critérios de saída.

Essas informações apoiam a tomada de decisão sobre a liberação da versão.

---

## 3. Qual a diferença entre o processo de testes em metodologias Ágeis e no modelo Cascata?

### Modelo Ágil

Em metodologias ágeis, a qualidade é trabalhada continuamente durante o desenvolvimento.

O QA participa de atividades como:

- refinamentos;
- planejamento;
- definição dos critérios de aceite;
- análise de riscos;
- desenvolvimento dos cenários;
- automação;
- execução dos testes;
- acompanhamento da entrega.

Os testes acontecem de forma incremental durante as Sprints.

Isso permite encontrar problemas mais cedo e obter feedback rapidamente.

A automação e a integração contínua também são importantes para permitir regressões frequentes.

### Modelo Cascata

No modelo Cascata, as etapas normalmente são realizadas de maneira sequencial.

Um fluxo comum é:

Requisitos → Análise → Desenvolvimento → Testes → Implantação

Nesse modelo, a etapa de testes tende a ocorrer depois que uma parte significativa do desenvolvimento já foi concluída.

Isso pode aumentar o custo de correções, pois problemas nos requisitos ou na implementação podem ser identificados mais tarde.

### Principal diferença

No modelo Ágil, os testes e a qualidade fazem parte de todo o ciclo de desenvolvimento.

No modelo Cascata, os testes geralmente aparecem como uma fase específica após o desenvolvimento.

Independentemente da metodologia utilizada, o objetivo do QA continua sendo reduzir riscos e contribuir para a entrega de um produto confiável e com qualidade.
