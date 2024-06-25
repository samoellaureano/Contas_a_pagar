CREATE TABLE account
(
    id           SERIAL PRIMARY KEY,
    due_date     DATE           NOT NULL,
    payment_date DATE,
    value        NUMERIC(19, 2) NOT NULL,
    description  VARCHAR(255),
    situation    VARCHAR(255)   NOT NULL
);