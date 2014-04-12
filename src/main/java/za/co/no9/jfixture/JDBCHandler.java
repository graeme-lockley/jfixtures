package za.co.no9.jfixture;

import za.co.no9.util.Optional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JDBCHandler extends BasicFixtureHandler {
    private Optional<Connection> connection;
    private List<JDBCOperation> operations = Arrays.asList(
            new JDBCConnect("jdbc-connect"),
            new JDBCCreateTable("jdbc-create-table"),
            new JDBCInsert("jdbc-insert"),
            new JDBCSql("jdbc-sql"));
    private boolean autoCloseConnection = true;

    public JDBCHandler(Connection connection) {
        this.connection = Optional.of(connection);
        this.autoCloseConnection = false;
    }

    public JDBCHandler() {
    }

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return findOperation(fixtureInput).isPresent();
    }

    private Optional<JDBCOperation> findOperation(Map<String, Object> fixtureInput) {
        for (JDBCOperation operation : operations) {
            if (operation.canProcess(fixtureInput)) {
                return Optional.of(operation);
            }
        }
        return Optional.empty();
    }

    @Override
    public void process(final Map<String, Object> fixtureInput) throws FixtureException {
        Optional<JDBCOperation> operation = findOperation(fixtureInput);

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

    protected void autoCloseConnection(boolean autoCloseConnection) {
        this.autoCloseConnection = autoCloseConnection;
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
            connection = Optional.empty();
        }
    }
}
