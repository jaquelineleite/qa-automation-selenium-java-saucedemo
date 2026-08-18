# Gerenciamento de Incidentes

## 1. Objetivo

Definir uma forma padronizada para registrar, classificar, acompanhar e comunicar incidentes identificados durante a execução dos testes.

Um bom registro deve permitir que outra pessoa compreenda o problema, consiga reproduzi-lo e tenha informações suficientes para iniciar a investigação.

---

## 2. Informações mínimas de um incidente

Um incidente deve conter:

- ID;
- título;
- descrição;
- ambiente;
- pré-condições;
- passos para reprodução;
- resultado esperado;
- resultado atual;
- evidências;
- tipo;
- severidade;
- prioridade;
- status.

Quando aplicável, também podem ser incluídos:

- versão;
- navegador;
- massa de dados;
- logs;
- payloads;
- response;
- usuário utilizado;
- horário da ocorrência;
- funcionalidade impactada.

---

## 3. Diferença entre Severidade e Prioridade

### Severidade

Representa o impacto técnico e funcional do problema.

Exemplos:

- Blocker;
- High;
- Medium;
- Low.

### Prioridade

Representa a urgência com que o defeito deve ser tratado.

Um defeito pode possuir alta severidade e prioridade diferente dependendo do contexto de negócio.

Da mesma forma, um defeito de baixa severidade pode receber alta prioridade caso afete uma entrega importante, legislação, imagem da empresa ou experiência de um grande número de usuários.

---

## Observação sobre os incidentes simulados

Os incidentes apresentados a seguir são cenários fictícios criados exclusivamente para demonstrar o processo de registro, classificação, análise de severidade e gerenciamento de defeitos.

Eles não representam necessariamente defeitos reais encontrados durante a execução da automação na aplicação utilizada como referência.

---

# 4. Incidentes classificados por tipo

## BUG-001 — Erro de Implementação

### Título

Sistema permite finalizar checkout sem validar corretamente os dados obrigatórios.

### Tipo

Erro de Implementação.

### Severidade

High.

### Descrição

Durante o processo de checkout, o sistema permite avançar mesmo quando uma validação obrigatória não é aplicada corretamente.

### Pré-condição

Usuário autenticado e produto adicionado ao carrinho.

### Passos para reprodução

1. Acessar o carrinho.
2. Iniciar o checkout.
3. Deixar um campo obrigatório sem preenchimento.
4. Selecionar a opção para continuar.

### Resultado esperado

O sistema deve impedir a continuação do checkout e apresentar uma mensagem informando o campo obrigatório.

### Resultado atual

O sistema permite prosseguir sem realizar corretamente a validação.

### Evidência

Screenshot, vídeo ou log da execução.

### Impacto

Pode permitir processamento de dados incompletos e comprometer etapas posteriores da compra.

---

## BUG-002 — Erro de Arquitetura

### Título

Indisponibilidade de um serviço crítico impede o funcionamento completo do fluxo de compra.

### Tipo

Erro de Arquitetura.

### Severidade

Blocker.

### Descrição

O processo de compra possui dependência de um serviço crítico sem mecanismo adequado de contingência ou tratamento de indisponibilidade.

Quando o serviço fica indisponível, o fluxo principal deixa de funcionar.

### Passos para reprodução

1. Simular indisponibilidade do serviço dependente.
2. Realizar login.
3. Adicionar produto ao carrinho.
4. Iniciar o fluxo de compra.

### Resultado esperado

O sistema deve possuir tratamento adequado para indisponibilidade, apresentando uma mensagem controlada e evitando inconsistências.

### Resultado atual

A jornada é interrompida e o usuário não consegue concluir a operação.

### Impacto

Impossibilidade de finalizar compras.

### Observação

A classificação como erro de arquitetura considera que a causa está relacionada ao desenho da solução e à ausência de mecanismos adequados de resiliência.

---

## BUG-003 — Erro de Requisito

### Título

Regra de validação de campo obrigatório não está claramente definida.

### Tipo

Erro de Requisito.

### Severidade

Medium.

### Descrição

A documentação funcional não especifica claramente se determinado campo do checkout é obrigatório nem qual comportamento deve ocorrer quando ele não for preenchido.

### Resultado esperado

O requisito deve indicar:

- obrigatoriedade do campo;
- formato esperado;
- mensagem de validação;
- comportamento do sistema.

### Resultado atual

O comportamento esperado não pode ser determinado de forma objetiva com base no requisito disponível.

### Impacto

Pode gerar implementações diferentes, cenários de teste inconsistentes e retrabalho entre produto, desenvolvimento e QA.

---

# 5. Incidentes classificados por severidade

## BUG-004 — Blocker

### Título

Usuários não conseguem realizar login.

### Severidade

Blocker.

### Descrição

Todos os usuários válidos recebem erro ao tentar acessar a aplicação.

### Resultado esperado

Usuários com credenciais válidas devem acessar o sistema.

### Resultado atual

Nenhum usuário consegue autenticar.

### Impacto

A aplicação fica inutilizável para os usuários que dependem de autenticação.

### Recomendação

Bloquear a liberação até que o problema seja corrigido ou exista uma mitigação segura.

---

## BUG-005 — High

### Título

Produto selecionado não é apresentado corretamente no carrinho.

### Severidade

High.

### Descrição

Após selecionar um produto, o carrinho apresenta informação diferente da escolhida pelo usuário.

### Resultado esperado

O produto presente no carrinho deve ser exatamente o produto selecionado.

### Resultado atual

O carrinho apresenta produto ou informação incorreta.

### Impacto

Pode resultar em compra incorreta e impacto direto na experiência do usuário e no negócio.

---

## BUG-006 — Medium

### Título

Contador do carrinho não é atualizado imediatamente após remoção.

### Severidade

Medium.

### Descrição

Após remover um item, o contador do carrinho permanece temporariamente com a quantidade anterior.

### Resultado esperado

O contador deve refletir a quantidade atual de produtos.

### Resultado atual

A informação visual permanece desatualizada até uma nova interação ou atualização da página.

### Impacto

Gera inconsistência visual, porém o usuário ainda consegue continuar utilizando o sistema.

---

## BUG-007 — Low

### Título

Alinhamento visual inconsistente em mensagem informativa.

### Severidade

Low.

### Descrição

Uma mensagem apresentada na interface possui alinhamento diferente do padrão visual utilizado nas demais telas.

### Resultado esperado

O conteúdo deve seguir o padrão visual definido.

### Resultado atual

O texto apresenta pequena inconsistência de posicionamento.

### Impacto

Não impede a utilização da funcionalidade e não altera regras de negócio.

---

# 6. Fluxo de tratamento de incidentes

Um fluxo possível é:

Novo → Em análise → Confirmado → Em desenvolvimento → Pronto para reteste → Reteste → Fechado

Quando necessário, também podem existir estados como:

- Bloqueado;
- Duplicado;
- Não reproduzido;
- Rejeitado;
- Reaberto.

---

## 7. Reteste

Após a correção de um incidente, o QA deve executar novamente os passos que reproduziam o problema.

O objetivo é confirmar que o comportamento atual corresponde ao resultado esperado.

Se a correção não estiver adequada, o incidente deve ser reaberto com novas evidências.

---

## 8. Regressão

Além do reteste específico, deve ser avaliado o impacto da alteração em funcionalidades relacionadas.

A regressão deve considerar:

- área alterada;
- dependências;
- integrações;
- fluxos críticos;
- histórico de defeitos;
- risco da mudança.

---

## 9. Comunicação

Defeitos críticos devem ser comunicados assim que identificados.

Não é recomendado aguardar o encerramento completo da execução quando existir um problema que:

- bloqueie os testes;
- afete um fluxo crítico;
- possa provocar perda de dados;
- possua impacto financeiro relevante;
- coloque a entrega em risco.

A comunicação deve ser objetiva e baseada em evidências.

---

## 10. Conclusão

O gerenciamento adequado de incidentes contribui para rastreabilidade, comunicação entre as equipes e tomada de decisão baseada em risco.

A classificação por tipo ajuda na identificação da origem do problema, enquanto severidade e prioridade apoiam a definição da ordem de tratamento.