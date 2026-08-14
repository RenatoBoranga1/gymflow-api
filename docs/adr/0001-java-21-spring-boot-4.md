# ADR 0001 — Java 21 e Spring Boot 4

- Status: aceito
- Data: 2026-08-14

## Contexto

O projeto usava Spring Boot 2.5.4 e APIs `javax`, com dependências e convenções incompatíveis com uma base atual.

## Decisão

Adotar Java 21 LTS, Spring Boot 4.1 e Jakarta EE. Manter Maven Wrapper no repositório e validar a migração antes de mudanças funcionais posteriores.

## Consequências

Há acesso a APIs e correções atuais, mas o ambiente de build precisa de JDK 21. Mudanças modulares do Boot 4 exigem starters específicos para MVC, Flyway e testes.
