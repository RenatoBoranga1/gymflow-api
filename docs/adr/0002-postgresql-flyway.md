# ADR 0002 — PostgreSQL e Flyway

- Status: aceito
- Data: 2026-08-14

## Contexto

O H2 em memória não representa o comportamento do banco alvo e a criação automática de schema não oferece evolução auditável.

## Decisão

Usar PostgreSQL 17, migrations Flyway incrementais e `hibernate.ddl-auto=validate`. Testes de integração executam o mesmo engine por Testcontainers.

## Consequências

O startup falha diante de schema incompatível. Desenvolvimento e CI precisam de Docker, mas deixam de depender de diferenças silenciosas do H2.
