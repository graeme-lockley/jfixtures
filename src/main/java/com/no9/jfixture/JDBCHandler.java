package com.no9.jfixture;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class JDBCHandler implements FixtureHandler {
    public static final String JDBC_CONNECT = "jdbc-connect";
    public static final String JDBC_CREATE_TABLE = "jdbc-create-table";
    public static final String JDBC_INSERT = "jdbc-insert";
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
//        if (fixtureInput.containsKey(JDBC_CONNECT)) {
//            processConnect(fixtureInput.get(JDBC_CONNECT));
//        } else if (fixtureInput.containsKey(JDBC_CREATE_TABLE)) {
//            processCreateTable(fixtureInput.get(JDBC_CREATE_TABLE));
//        } else if (fixtureInput.containsKey(JDBC_INSERT)) {
//            processInsert(fixtureInput.get(JDBC_INSERT));
//        }
    }

    private void processConnect(Object input) throws FixtureException {
    }

    private void processCreateTable(Object input) throws FixtureException {
    }

    private void processInsert(Object input) throws FixtureException {

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
