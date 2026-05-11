CREATE TABLE users (
    id         BIGSERIAL    PRIMARY KEY,
    full_name  VARCHAR(100) NOT NULL,
    cpf        VARCHAR(11)  NOT NULL,
    email      VARCHAR(150) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    type       VARCHAR(10)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_users_cpf   UNIQUE (cpf),
    CONSTRAINT uk_users_email UNIQUE (email)
);
