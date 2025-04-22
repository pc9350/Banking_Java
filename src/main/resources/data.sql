-- Insert sample accounts
INSERT INTO account (name, balance) VALUES ('John Doe', 1000.00);
INSERT INTO account (name, balance) VALUES ('Jane Smith', 2500.00);
INSERT INTO account (name, balance) VALUES ('Bob Johnson', 500.00);

-- Insert sample transactions
INSERT INTO transaction (account_id, amount, type, timestamp) 
VALUES (1, 1000.00, 'DEPOSIT', CURRENT_TIMESTAMP());

INSERT INTO transaction (account_id, amount, type, timestamp) 
VALUES (2, 2500.00, 'DEPOSIT', CURRENT_TIMESTAMP());

INSERT INTO transaction (account_id, amount, type, timestamp) 
VALUES (3, 500.00, 'DEPOSIT', CURRENT_TIMESTAMP()); 