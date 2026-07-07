# SmartSplit -- Visão do Produto

## Objetivo

Criar uma alternativa ao Splitwise para praticar Java, Spring Boot,
Maven, PostgreSQL, Kafka e Docker.

## Stack

-   Java 21 (LTS)
-   Spring Boot
-   Maven
-   PostgreSQL
-   Spring Security
-   Docker
-   Kafka (fase 2)
-   Flyway
-   Testcontainers

## Arquitetura

Monólito modular organizado por funcionalidade.

src/main/java/pt/teunome/smartsplit - common - config - user - group -
expense - category - balance - settlement - notification

Cada módulo: - controller - service - repository - entity - dto -
mapper - validator - exception

## Épicos

1.  Utilizadores
2.  Grupos
3.  Despesas
4.  Balanços
5.  Liquidação Inteligente
6.  Pagamentos
7.  Histórico
8.  Notificações
9.  Dashboard

## Backlog (MVP)

-   [ ] US001 Registar utilizador
-   [ ] US002 Login JWT
-   [ ] US003 Criar grupo
-   [ ] US004 Convidar membro
-   [ ] US005 Remover membro
-   [ ] US006 Criar despesa
-   [ ] US007 Categorizar despesa
-   [ ] US008 Divisão igual
-   [ ] US009 Divisão por percentagem
-   [ ] US010 Divisão personalizada
-   [ ] US011 Consultar saldo
-   [ ] US012 Consultar quem deve a quem
-   [ ] US013 Algoritmo inteligente de liquidação
-   [ ] US014 Visualizar liquidações
-   [ ] US015 Marcar pagamento
-   [ ] US016 Confirmar pagamento
-   [ ] US017 Histórico e filtros
-   [ ] US018 Notificação de nova despesa
-   [ ] US019 Notificação de pagamento
-   [ ] US020 Notificação de convite
-   [ ] US021 Dashboard

## Requisitos Funcionais

-   Gestão de utilizadores
-   Gestão de grupos
-   Registo de despesas
-   Categorias
-   Divisão flexível
-   Cálculo automático de saldos
-   Otimização de transferências
-   Histórico
-   Notificações

## Requisitos Não Funcionais

-   API REST
-   Validação de dados
-   Segurança JWT
-   Migrações Flyway
-   Testes unitários e integração
-   Docker Compose
-   Eventos Kafka
-   Documentação Swagger/OpenAPI
-   CI/CD

## Roadmap

### Fase 1

Autenticação, grupos, despesas e saldos.

### Fase 2

Liquidação inteligente.

### Fase 3

Kafka, notificações e auditoria.

### Fase 4

Dashboard, estatísticas e melhorias.
