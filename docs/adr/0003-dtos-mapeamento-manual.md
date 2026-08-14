# ADR 0003 — DTOs e mapeamento manual

- Status: aceito
- Data: 2026-08-14

## Contexto

Entidades JPA eram usadas diretamente como contratos HTTP, expondo detalhes de persistência e aumentando o risco de recursão e lazy loading acidental.

## Decisão

Separar DTOs de entrada e saída, manter associações JPA unidirecionais e usar mapeadores manuais estáticos.

## Consequências

O contrato evolui sem alterar o schema. MapStruct foi avaliado e não adotado: há apenas quatro agregados com mapeamentos curtos, e uma dependência/etapa de geração não reduziria complexidade material.
