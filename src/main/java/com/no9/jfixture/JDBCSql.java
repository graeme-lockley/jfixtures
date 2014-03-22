package com.no9.jfixture;

import static com.no9.jfixture.YAMLDSL.fromYAML;

public class JDBCSql extends JDBCOperation {
    public JDBCSql(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        for (Object row : fromYAML(fixtureInput).iterableElseException(exceptionMessagePrefix() + ": Expected a list of SQL statements.")) {
            executeStatement(handler, String.valueOf(row));
        }
    }
}
