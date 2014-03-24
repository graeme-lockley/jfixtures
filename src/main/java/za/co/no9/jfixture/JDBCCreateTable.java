package za.co.no9.jfixture;

import static za.co.no9.jfixture.YAMLDSL.YAMLMap;
import static za.co.no9.jfixture.YAMLDSL.fromYAML;

public class JDBCCreateTable extends JDBCOperation {
    public JDBCCreateTable(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        YAMLMap fixtureMap = fromYAML(fixtureInput).mapElseException(exceptionMessagePrefix() + ": Excepts a map");
        StringBuilder buffer = new StringBuilder()
                .append("create table if not exists ")
                .append(fixtureMap.field("name").ifBlankException(exceptionMessagePrefix() + ": Field name has not been set.").asString())
                .append(" (");

        YAMLMap rows = fixtureMap.field("rows").ifNullException(exceptionMessagePrefix() + ": Field rows has not been set.").map();

        for (String key : rows.keySet()) {
            buffer.append(key)
                    .append(" ")
                    .append(String.valueOf(rows.get(key)))
                    .append(", ");
        }

        buffer.delete(buffer.length() - 2, buffer.length())
                .append(")");

        executeStatement(handler, buffer.toString());
    }
}
