package com.no9.jfixture;

import java.util.List;

public class JDBCSql extends JDBCOperation {
    public JDBCSql(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        if (fixtureInput instanceof List) {
            List<Object> rows = (List<Object>) fixtureInput;

            for (Object row : rows) {
                executeStatement(handler, String.valueOf(row));
            }
        } else {
            throw new FixtureException(exceptionMessagePrefix() + "Expected a list of SQL statements.");
        }

    }
}
