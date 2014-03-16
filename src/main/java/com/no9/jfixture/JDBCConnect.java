package com.no9.jfixture;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class JDBCConnect extends JDBCOperation {
    public JDBCConnect(String selector) {
        super(selector);
    }

    @Override
    protected void processOperation(JDBCHandler handler, Object fixtureInput) throws FixtureException {
        if (fixtureInput instanceof Map) {
            Map<String, Object> connectParams = (Map<String, Object>) fixtureInput;

            try {
                Class.forName(parameter(connectParams, "driver"));
                handler.connection(DriverManager.getConnection(parameter(connectParams, "url"), parameter(connectParams, "username", ""), parameter(connectParams, "password", "")));
            } catch (ClassNotFoundException e) {
                throw new FixtureException(exceptionMessagePrefix() + "Loading of the driver class " + parameter(connectParams, "driver") + " failed.");
            } catch (SQLException e) {
                throw new FixtureException(exceptionMessagePrefix() + "Unable to connect: " + e.getMessage());
            }
        } else {
            throw new FixtureException(exceptionMessagePrefix() + "Expects a mapping.");
        }
    }
}
