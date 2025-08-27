package com.crypto.server.config.exceptions;

import java.sql.SQLException;

public class DatabaseException extends CryptoException {
    public DatabaseException(String message, SQLException e) {
        super(message);
    }

    public DatabaseException(String message) {
        super(message);
    }
}
