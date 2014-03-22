package com.no9.jfixture;

import java.sql.DriverManager;
import java.sql.SQLException;

import static com.no9.jfixture.YAMLDSL.fromYAML;

public class JDBCConnect extends JDBCOperation {
    public JDBCConnect(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        YAMLDSL.YAMLMap fixtureMap = fromYAML(fixtureInput).mapElseException(exceptionMessagePrefix() + ": Excepts a map");

        String driverName = fixtureMap.field("driver").ifBlankException(exceptionMessagePrefix() + ": Field driver has not been set.").asString();
        try {
            Class.forName(driverName);
            handler.connection(DriverManager.getConnection(
                    fixtureMap.field("url").ifBlankException(exceptionMessagePrefix() + ": Field driver has not been set.").asString(),
                    fixtureMap.field("username").ifBlankDefault("").asString(),
                    fixtureMap.field("password").ifBlankDefault("").asString()));

            handler.autoCloseConnection(fixtureMap.field("autoclose").ifEmptyDefault(true).asBoolean());

        } catch (ClassNotFoundException e) {
            throw new FixtureException(exceptionMessagePrefix() + "Loading of the driver class " + driverName + " failed:" + e.getMessage());
        } catch (SQLException e) {
            throw new FixtureException(exceptionMessagePrefix() + "Unable to connect: " + e.getMessage());
        }
    }
}
