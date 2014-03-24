package za.co.no9.jfixture;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public abstract class JDBCOperation {
    protected final String selector;

    public JDBCOperation(String selector) {
        this.selector = selector;
    }

    protected boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey(selector());
    }

    protected void process(JDBCHandler handler, Map<String, Object> fixtureInput) throws FixtureException {
        processOperation(handler, fixtureInput.get(selector));
    }

    protected abstract void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException;

    protected String selector() {
        return selector;
    }

    protected String exceptionMessagePrefix() {
        return "JDBCHandler: " + selector() + ": ";
    }

    protected void executeStatement(JDBCHandler handler, String buffer) throws FixtureException {
        Statement statement = null;
        try {
            statement = handler.connection().createStatement();
            statement.execute(String.valueOf(buffer));
        } catch (SQLException e) {
            throw new FixtureException(exceptionMessagePrefix() + "Error executing statement: " + buffer + ": " + e.toString());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
