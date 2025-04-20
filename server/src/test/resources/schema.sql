CREATE SCHEMA IF NOT EXISTS my_crypto_app;

DROP TABLE IF EXISTS user_transactions;
DROP TABLE IF EXISTS user_holdings;
DROP TABLE IF EXISTS purchase_lots;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS holdings;
DROP TABLE IF EXISTS users;

CREATE TABLE holdings (
                               id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(30),
                               symbol VARCHAR(30),
                               cost DECIMAL(10, 2),
                               quantity DECIMAL(10, 2)
);

CREATE TABLE transactions (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               type ENUM('BUY', 'SELL') NOT NULL,
                               coin_symbol VARCHAR(10) NOT NULL,
                               amount DECIMAL(18, 8) NOT NULL,
                               price_per_unit DECIMAL(18, 2) NOT NULL,
                               profit DECIMAL(18, 2) NOT NULL,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_lots (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               coin_symbol VARCHAR(10) NOT NULL,
                               original_amount DECIMAL(18, 8) NOT NULL,
                               remaining_amount DECIMAL(18, 8) NOT NULL,
                               price_per_unit DECIMAL(18, 2) NOT NULL,
                               buy_transaction_id INT NOT NULL,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
                               id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               username VARCHAR(255) NOT NULL,
                               password VARCHAR(255) NOT NULL,
                               balance DECIMAL(15, 2) NOT NULL DEFAULT 10000.00
);

CREATE TABLE user_holdings (
                               user_id INT,
                               holding_id INT,
                               PRIMARY KEY (user_id, holding_id),
                               FOREIGN KEY (user_id) REFERENCES users(id),
                               FOREIGN KEY (holding_id) REFERENCES holdings(id)
);

CREATE TABLE user_transactions (
                                user_id INT,
                                transaction_id INT,
                                PRIMARY KEY (user_id, transaction_id),
                                FOREIGN KEY (user_id) REFERENCES users(id),
                                FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);
