CREATE TABLE user_accounts (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(254) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_user_accounts_role CHECK (role IN ('USER', 'ADMIN'))
);

CREATE UNIQUE INDEX uk_user_accounts_email_lower ON user_accounts (LOWER(email));
