  CREATE TABLE client(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(20),
    email VARCHAR(20),
    phone VARCHAR(20)
  );

  CREATE TABLE transaction(
    id SERIAL PRIMARY KEY NOT NULL,
    from_client_id INTEGER,
    to_client_id INTEGER,
    amount INTEGER
  );

  CREATE INDEX idx_transaction_from_client ON transaction(from_client_id);
  CREATE INDEX idx_transaction_to_client ON transaction(to_client_id);