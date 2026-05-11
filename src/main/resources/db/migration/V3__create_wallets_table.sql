CREATE TABLE wallets (
    id         BIGSERIAL      PRIMARY KEY,
    user_id    BIGINT         NOT NULL,
    balance    NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP      NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_wallets_user   FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uk_wallets_user   UNIQUE (user_id),
    CONSTRAINT chk_balance_non_negative CHECK (balance >= 0)
);
