package com.no9.jfixture;

import java.util.Map;

import static com.no9.jfixture.YAMLDSL.fromYAML;

public class JDBCInsert extends JDBCOperation {
    public JDBCInsert(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        for (Object rowAsObject : fromYAML(fixtureInput)
                .mapElseException(exceptionMessagePrefix() + ": Excepts a map")
                .field("fields")
                .ifNullException(exceptionMessagePrefix() + ": The parameter rows is missing")
                .iterableElseException(exceptionMessagePrefix() + ": The parameter rows is not a list")) {

            Map<String, Object> row = (Map) rowAsObject;

            StringBuilder buffer = new StringBuilder();
            StringBuilder values = new StringBuilder();

            buffer
                    .append("insert into ")
                    .append(fromYAML(fixtureInput).map().field("name").ifBlankException(": The parameter name is missing").asString())
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
    }
}
