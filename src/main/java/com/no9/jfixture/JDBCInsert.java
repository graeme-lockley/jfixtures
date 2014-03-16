package com.no9.jfixture;

import java.util.List;
import java.util.Map;

public class JDBCInsert extends JDBCOperation {
    public JDBCInsert(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        if (fixtureInput instanceof Map) {
            Map<String, Object> connectParams = (Map<String, Object>) fixtureInput;

            Object rowsObject = connectParams.get("rows");

            if (rowsObject == null) {
                throw new FixtureException(exceptionMessagePrefix() + "The expected parameter rows is missing.");
            } else if (rowsObject instanceof List) {
                List<Map<String, Object>> rows = (List<Map<String, Object>>) rowsObject;

                for (Map<String, Object> row : rows) {
                    StringBuilder buffer = new StringBuilder();
                    StringBuilder values = new StringBuilder();

                    buffer
                            .append("insert into ")
                            .append(parameter(connectParams, "name"))
                            .append(" (");
                    values.append(" values (");

                    for (String key : row.keySet()) {
                        buffer
                                .append(key)
                                .append(", ");
                        Object value = row.get(key);

                        if (value instanceof String) {
                            values.append("'")
                                    .append(String.valueOf(value))
                                    .append("'");
                        } else {
                            values.append(String.valueOf(value));
                        }
                        values.append(", ");
                    }

                    buffer
                            .delete(buffer.length() - 2, buffer.length())
                            .append(")");
                    values
                            .delete(values.length() - 2, values.length())
                            .append(")");
                    buffer.append(values);

                    executeStatement(handler, buffer.toString());
                }
            } else {
                throw new FixtureException(exceptionMessagePrefix() + "Expects rows defined as a mapping.");
            }
        } else {
            throw new FixtureException(exceptionMessagePrefix() + "Expects a mapping.");
        }
    }
}
