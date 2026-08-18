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



## 5. Distribuição e Organização das 34 Horas

Considerando a criticidade do projeto e o histórico de rollback, a estratégia principal será utilizar execução paralela para preservar a maior cobertura possível.

A redução de 48 para 34 horas não deve resultar automaticamente na eliminação da regressão completa.

### QA 1 — Testes Funcionais e Exploratórios

Responsável pela execução dos 20 casos funcionais e dos testes exploratórios planejados.

Estimativa:

- testes funcionais: 16 horas;
- testes exploratórios: 12 horas;
- total: 28 horas.

### QA 2 — Regressão Completa

Responsável pela execução da regressão completa do sistema, iniciando pelos fluxos de maior criticidade.

Estimativa:

- regressão completa: 20 horas.

### Execução em Paralelo

As duas frentes serão iniciadas simultaneamente.

- QA 1 conclui sua frente em aproximadamente 28 horas;
- QA 2 conclui a regressão em aproximadamente 20 horas;
- o maior caminho permanece em 28 horas, dentro da janela disponível de 34 horas.

As aproximadamente 6 horas restantes serão utilizadas para retestes, investigação de defeitos, validação de correções, contingências, consolidação dos resultados e decisão de Go/No-Go.

Essa abordagem permite preservar os testes funcionais, os testes exploratórios e a regressão completa, reduzindo o risco diante do histórico de rollback.

### Plano de Contingência

Caso apenas um QA esteja disponível, a execução será priorizada por risco na seguinte ordem:

1. smoke test;
2. funcionalidades alteradas na sprint;
3. fluxos críticos de negócio;
4. regressão das áreas impactadas;
5. defeitos reincidentes;
6. cenários negativos de maior impacto;
7. testes exploratórios direcionados.

Qualquer cenário não executado deverá ser registrado como risco residual e comunicado antes da decisão de liberação.

---

## 6. Paralelização

A paralelização será utilizada para preservar a cobertura planejada dentro da janela disponível de 34 horas.

A divisão principal será:

### QA 1

Responsável por:

- 20 casos de testes funcionais;
- fluxos principal, alternativos e de exceção;
- testes exploratórios;
- registro de evidências;
- retestes quando necessário.

### QA 2

Responsável por:

- regressão completa do sistema;
- priorização inicial dos fluxos críticos;
- acompanhamento da automação;
- registro de falhas e evidências.

### Desenvolvedor

Pode apoiar com:

- análise de logs;
- investigação técnica;
- correção de defeitos;
- testes unitários;
- análise de causa raiz.

### Product Owner ou Representante de Negócio

Pode apoiar na validação de:

- regras críticas;
- critérios de aceite;
- cenários de alto impacto para o negócio;
- riscos que influenciem a decisão de Go/No-Go.

A responsabilidade pela qualidade permanece compartilhada pelo time, enquanto a coordenação e definição da estratégia de testes ficam sob responsabilidade do QA responsável pela entrega.

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