package com.no9.jfixture;

import java.util.Map;

public class JDBCCreateTable extends JDBCOperation {
    public JDBCCreateTable(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        if (fixtureInput instanceof Map) {
            Map<String, Object> connectParams = (Map<String, Object>) fixtureInput;

            StringBuilder buffer = new StringBuilder();

            buffer
                    .append("create table if not exists ")
                    .append(parameter(connectParams, "name"))
                    .append(" (");

            Object rowsObject = connectParams.get("rows");

            if (rowsObject == null) {
                throw new FixtureException(exceptionMessagePrefix() + "The expected parameter rows is missing.");
            } else if (rowsObject instanceof Map) {
                Map<String, Object> rows = (Map<String, Object>) rowsObject;

                for (String key : rows.keySet()) {
                    buffer
                            .append(key)
                            .append(" ")
                            .append(String.valueOf(rows.get(key)))
                            .append(", ");
                }

                buffer
                        .delete(buffer.length() - 2, buffer.length())
                        .append(")");

                executeStatement(handler, buffer.toString());
            } else {
                throw new FixtureException(exceptionMessagePrefix() + "Expects rows defined as a mapping.");
            }
        } else {
            throw new FixtureException(exceptionMessagePrefix() + "Expects a mapping.");
        }
    }
}
