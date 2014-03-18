package com.no9.jfixture;

import static com.no9.jfixture.YAMLDSL.YAMLMap;
import static com.no9.jfixture.YAMLDSL.fromYAML;

public class JDBCCreateTable extends JDBCOperation {
    public JDBCCreateTable(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        if (fromYAML(fixtureInput).isMap()) {
            StringBuilder buffer = new StringBuilder()
                    .append("create table if not exists ")
                    .append(fromYAML(fixtureInput).map().field("name").ifBlankException(exceptionMessagePrefix() + ": Field name has not been set.").asString())
                    .append(" (");

            YAMLMap rows = fromYAML(fixtureInput).map().field("rows").ifNullException(exceptionMessagePrefix() + ": Field rows has not been set.").map();

            for (String key : rows.keySet()) {
                buffer.append(key)
                        .append(" ")
                        .append(String.valueOf(rows.get(key)))
                        .append(", ");
            }

            buffer.delete(buffer.length() - 2, buffer.length())
                    .append(")");

            executeStatement(handler, buffer.toString());
        } else {
            throw new FixtureException(exceptionMessagePrefix() + "Expects rows defined as a mapping.");
        }
    }
}
