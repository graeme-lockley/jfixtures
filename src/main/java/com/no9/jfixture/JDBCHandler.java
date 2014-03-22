package com.no9.jfixture;

import com.no9.utils.Optional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class JDBCHandler implements BasicFixtureHandler {
    private Optional<Connection> connection;
    private boolean autoCloseConnection = true;

    public JDBCHandler(Connection connection) {
        this.connection = Optional.of(connection);
        this.autoCloseConnection = false;
    }

    public JDBCHandler() {
    }

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return findOperation(fixtureInput) != null;
    }

    private Optional<JDBCOperations> findOperation(Map<String, Object> fixtureInput) {
        for (JDBCOperations operation : JDBCOperations.values()) {
            if (operation.canProcess(fixtureInput)) {
                return Optional.of(operation);
            }
        }
        return Optional.none();
    }

    @Override
    public void process(final Map<String, Object> fixtureInput) throws FixtureException {
        Optional<JDBCOperations> operation = findOperation(fixtureInput);

        if (operation.isPresent()) {
            operation.get().process(JDBCHandler.this, fixtureInput);
        }
    }

    public static JDBCHandler create() {
        return new JDBCHandler();
    }

    public static JDBCHandler create(Connection connection) {
        return new JDBCHandler(connection);
    }

    public boolean isConnected() {
        return connection.isPresent();
    }

    public Connection connection() {
        return connection.get();
    }

    protected void connection(Connection connection) {
        this.connection = Optional.of(connection);
        this.autoCloseConnection = true;
    }

    @Override
    public void close() throws IOException {
        if (autoCloseConnection) {
            if (connection.isPresent()) {
                try {
                    connection.get().rollback();
                    connection.get().close();
                } catch (SQLException e) {
                    throw new IOException(e);
                }
            }
            connection = Optional.none();
        }
    }
}
