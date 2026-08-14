# ADR 0004 — JWT stateless e RBAC

- Status: aceito
- Data: 2026-08-14

## Contexto

O domínio não possuía autenticação. Operações de leitura e mutação têm níveis de risco diferentes e não devem compartilhar permissão irrestrita.

## Decisão

Usar senhas BCrypt, access tokens JWT HMAC-SHA256 de curta duração e papéis `USER`/`ADMIN`. Cadastro cria somente `USER`; leituras aceitam ambos e mutações exigem `ADMIN`. O segredo é externo, Base64 e mínimo de 256 bits.

## Consequências

A API permanece stateless e falha sem segredo válido. Refresh token foi deliberadamente adiado porque não há requisito de sessão longa; adicioná-lo exigiria persistência, rotação, revogação e novos controles de segurança.
