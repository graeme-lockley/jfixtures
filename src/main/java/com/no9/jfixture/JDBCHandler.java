package com.no9.jfixture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class JDBCHandler implements FixtureHandler {
    public static final String JDBC_CONNECT = "jdbc-connect";
    public static final String JDBC_CREATE_TABLE = "jdbc-create-table";
    private Connection connection;

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey(JDBC_CONNECT) || fixtureInput.containsKey(JDBC_CREATE_TABLE);
    }

    @Override
    public void process(Map<String, Object> fixtureInput) throws FixtureException {
        if (fixtureInput.containsKey(JDBC_CONNECT)) {
            processConnect(fixtureInput.get(JDBC_CONNECT));
        } else if (fixtureInput.containsKey(JDBC_CREATE_TABLE)) {
            processCreateTable(fixtureInput.get(JDBC_CREATE_TABLE));
        }
    }

    private void processConnect(Object input) throws FixtureException {
        if (input instanceof Map) {
            Map<String, Object> connectParams = (Map<String, Object>) input;

            try {
                Class.forName(parameter(connectParams, "driver"));
                connection = DriverManager.getConnection(parameter(connectParams, "url"), parameter(connectParams, "username", ""), parameter(connectParams, "password", ""));
            } catch (ClassNotFoundException e) {
                throw new FixtureException("JDBCHandler: " + JDBC_CONNECT + ": Loading of the driver class " + parameter(connectParams, "driver") + " failed.");
            } catch (SQLException e) {
                throw new FixtureException("JDBCHandler: " + JDBC_CONNECT + ": Unable to connect: " + e.getMessage());
            }
        } else {
            throw new FixtureException("JDBCHandler: " + JDBC_CONNECT + ": Expects a mapping.");
        }
    }

    private void processCreateTable(Object input) throws FixtureException {
        if (input instanceof Map) {
            Map<String, Object> connectParams = (Map<String, Object>) input;

            StringBuilder buffer = new StringBuilder();

            buffer
                    .append("create table if not exists ")
                    .append(parameter(connectParams, "name"))
                    .append(" (");

            Object rowsObject = connectParams.get("rows");

            if (rowsObject == null) {
                throw new FixtureException("JDBCHandler: " + JDBC_CREATE_TABLE + ": No rows defined as a mapping.");
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

                try {
                    connection.createStatement().execute(String.valueOf(buffer));
                } catch (SQLException e) {
                    throw new FixtureException("JDBCHandler: " + JDBC_CREATE_TABLE + ": Error executing create table: " + buffer + ": " + e.toString());
                }
            } else {
                throw new FixtureException("JDBCHandler: " + JDBC_CREATE_TABLE + ": Expects rows defined as a mapping.");
            }
        } else {
            throw new FixtureException("JDBCHandler: " + JDBC_CONNECT + ": Expects a mapping.");
        }
    }

    private String parameter(Map<String, Object> connectParams, String name) throws FixtureException {
        String result = parameter(connectParams, name, null);
        if (result == null) {
            throw new FixtureException("JDBCHandler: The expected parameter " + name + " is missing.");
        } else {
            return result;
        }
    }

    private String parameter(Map<String, Object> connectParams, String name, String defaultValue) {
        Object result = connectParams.get(name);
        if (result == null) {
            return defaultValue;
        } else {
            return String.valueOf(result);
        }
    }

    public static JDBCHandler create() {
        return new JDBCHandler();
    }

    public boolean isConnected() {
        return connection != null;
    }

    protected Connection connection() {
        return this.connection;
    }
}
