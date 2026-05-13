CREATE TABLE transactions (
    id         BIGSERIAL      PRIMARY KEY,
    payer_id   BIGINT         NOT NULL,
    payee_id   BIGINT         NOT NULL,
    amount     NUMERIC(19, 2) NOT NULL,
    status     VARCHAR(20)    NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_transactions_payer FOREIGN KEY (payer_id) REFERENCES wallets(id),
    CONSTRAINT fk_transactions_payee FOREIGN KEY (payee_id) REFERENCES wallets(id),
    CONSTRAINT chk_amount_positive   CHECK (amount > 0)
);
