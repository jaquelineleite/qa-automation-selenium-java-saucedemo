# Estratégia de Testes

## 1. Objetivo

Definir uma estratégia de testes para uma entrega com restrição de tempo, priorizando riscos de negócio e garantindo cobertura adequada dos fluxos mais críticos.

O cenário considera uma redução do tempo inicialmente planejado de 48 horas para 34 horas disponíveis.

A estratégia não consiste apenas em remover testes, mas em reorganizar a execução de acordo com risco, criticidade, impacto e possibilidade de paralelização.

---

## 2. Cenário

Planejamento inicial:

- 20 casos de testes funcionais: 16 horas;
- testes exploratórios: 12 horas;
- regressão completa: 20 horas.

Total planejado:

48 horas.

Novo tempo disponível:

34 horas.

Redução:

14 horas.

Como existe histórico de necessidade de rollback, reduzir indiscriminadamente a regressão representaria um risco elevado.

---

## 3. Abordagem Baseada em Risco

A execução será priorizada considerando:

- impacto financeiro;
- impacto para o usuário;
- criticidade da funcionalidade;
- frequência de utilização;
- áreas alteradas na versão;
- histórico de defeitos;
- dependências entre sistemas;
- possibilidade de rollback;
- complexidade da alteração.

Os cenários de maior impacto serão executados primeiro.

---

## 4. Priorização

### Prioridade 1 — Fluxos Críticos

Execução obrigatória:

- autenticação;
- funcionalidades diretamente alteradas;
- fluxos financeiros ou transacionais;
- persistência de dados críticos;
- integrações essenciais;
- fluxo principal do usuário;
- smoke test;
- cenários relacionados a defeitos críticos anteriores.

Esses cenários devem ser executados antes de qualquer decisão de liberação.

### Prioridade 2 — Alta

- principais fluxos alternativos;
- validações negativas relevantes;
- integrações secundárias;
- cenários com histórico frequente de defeitos.

### Prioridade 3 — Média/Baixa

- cenários pouco utilizados;
- validações cosméticas;
- caminhos de baixo impacto;
- testes que podem ser executados após a entrega sem comprometer o negócio.

---

## 5. Distribuição das 34 Horas

A distribuição proposta é:

### Testes funcionais — 12 horas

Execução dos casos relacionados às alterações e aos fluxos críticos.

Os 20 casos inicialmente previstos serão priorizados por risco.

Os casos de menor criticidade poderão ser postergados quando necessário.

### Testes exploratórios — 6 horas

Os testes exploratórios serão direcionados principalmente para:

- áreas alteradas;
- integrações;
- cenários de maior risco;
- comportamentos não cobertos pelos casos formais.

A sessão exploratória será focada e baseada em charter.

### Regressão — 16 horas

A regressão será priorizada para:

- funcionalidades críticas;
- áreas impactadas pela alteração;
- fluxos com histórico de defeitos;
- integrações;
- jornadas principais.

Sempre que possível, testes automatizados devem executar em paralelo às validações manuais.

Total:

34 horas.

---

## 6. Paralelização

Para reduzir o tempo total sem comprometer excessivamente a cobertura, as atividades podem ser realizadas em paralelo.

Exemplo:

### QA 1

- testes funcionais das alterações;
- retestes;
- validação dos critérios de aceite.

### QA 2

- regressão crítica;
- execução e acompanhamento da automação.

### Desenvolvedor

Pode apoiar com:

- análise de logs;
- investigação técnica;
- correção de defeitos;
- testes unitários;
- validações técnicas.

### Product Owner ou representante de negócio

Pode apoiar na validação de:

- regras críticas;
- cenários de alto impacto;
- comportamento esperado do produto.

A responsabilidade pela qualidade deve ser compartilhada pelo time.

---

## 7. Smoke Test

Antes da regressão mais ampla deve ser executado um smoke test para verificar se a versão possui condições mínimas de continuar sendo testada.

Exemplos:

- aplicação acessível;
- autenticação funcionando;
- principais páginas carregando;
- serviços críticos disponíveis;
- fluxo principal minimamente operacional.

Se o smoke apresentar falha bloqueadora, a execução deve ser interrompida e o problema comunicado ao time.

---

## 8. Uso da Automação

A automação deve ser utilizada para reduzir o tempo gasto em cenários repetitivos e regressivos.

Devem ser priorizados:

- smoke tests;
- fluxos críticos;
- regressões frequentes;
- cenários estáveis;
- validações repetitivas.

A automação pode ser executada enquanto os QAs realizam testes manuais e exploratórios.

Isso permite aumentar a cobertura dentro da janela disponível.

---

## 9. Critérios de Go / No-Go

### Go

A versão pode ser recomendada para liberação quando:

- smoke test estiver aprovado;
- fluxos críticos estiverem aprovados;
- regressão crítica estiver satisfatória;
- não houver defeitos bloqueadores;
- defeitos de alta severidade estiverem avaliados;
- riscos remanescentes forem conhecidos e aceitos pelo time responsável.

### No-Go

A recomendação será não liberar quando existir:

- defeito bloqueador;
- falha em fluxo crítico;
- risco de perda ou corrupção de dados;
- falha crítica em integração;
- impossibilidade de executar cobertura mínima necessária;
- risco elevado sem mitigação;
- instabilidade que comprometa a utilização do produto.

A decisão final deve ser baseada em evidências e risco de negócio, e não somente na quantidade de testes aprovados.

---

## 10. Tratamento de Riscos Residuais

Testes que não puderem ser executados dentro das 34 horas devem ser explicitamente registrados.

Para cada item não executado devem ser informados:

- cenário;
- motivo;
- criticidade;
- risco;
- impacto;
- recomendação.

Essas informações devem ser comunicadas antes da decisão de liberação.

---

## 11. Comunicação

Durante a execução serão comunicados:

- progresso dos testes;
- percentual de execução;
- defeitos críticos;
- bloqueios;
- riscos;
- cobertura pendente;
- resultado da regressão;
- recomendação de Go/No-Go.

Problemas críticos devem ser informados imediatamente, sem aguardar o encerramento completo da execução.

---

## 12. Conclusão

Diante da redução de 48 para 34 horas, a estratégia proposta utiliza priorização baseada em risco, paralelização, automação, smoke test e regressão direcionada.

O objetivo é preservar a cobertura das funcionalidades mais críticas e fornecer informações suficientes para uma decisão consciente sobre a liberação da versão.

A redução de prazo não elimina os riscos. Por isso, qualquer cobertura não executada deve permanecer visível e ser considerada na decisão final.