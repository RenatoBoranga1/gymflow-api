# ADR 0005 — Monolito modular sem cache distribuído

- Status: aceito
- Data: 2026-08-14

## Contexto

Cache, mensageria e decomposição em serviços aumentariam o número de falhas operacionais sem evidência de volume, latência ou trabalho assíncrono no escopo atual.

## Decisão

Manter um monolito modular em camadas, transações locais e acesso direto ao PostgreSQL. Não adicionar Redis, Kafka, Kubernetes ou cache especulativo.

## Consequências

O projeto continua simples de executar, testar e observar. Novos componentes só deverão ser introduzidos após métricas e requisitos que justifiquem seu custo.
