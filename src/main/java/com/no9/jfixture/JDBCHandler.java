package com.no9.jfixture;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class JDBCHandler implements FixtureHandler {
    private Connection connection;
    private boolean autoCloseConnection = true;

    public JDBCHandler(Connection connection) {
        this.connection = connection;
        this.autoCloseConnection = false;
    }

    public JDBCHandler() {
    }

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return findOperation(fixtureInput) != null;
    }

    private JDBCOperations findOperation(Map<String, Object> fixtureInput) {
        for (JDBCOperations operation : JDBCOperations.values()) {
            if (operation.canProcess(fixtureInput)) {
                return operation;
            }
        }
        return null;
    }

    @Override
    public void process(Map<String, Object> fixtureInput) throws FixtureException {
        JDBCOperations operation = findOperation(fixtureInput);

        if (operation != null) {
            operation.process(this, fixtureInput);
        }
    }

    public static JDBCHandler create() {
        return new JDBCHandler();
    }

    public static JDBCHandler create(Connection connection) {
        return new JDBCHandler(connection);
    }

    public boolean isConnected() {
        return connection != null;
    }

    protected Connection connection() {
        return this.connection;
    }

    protected void connection(Connection connection) {
        this.connection = connection;
        this.autoCloseConnection = true;
    }

    @Override
    public void close() throws IOException {
        if (connection != null && autoCloseConnection) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                throw new IOException(e);
            }
            connection = null;
        }
    }
}
