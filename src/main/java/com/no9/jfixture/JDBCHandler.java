package com.no9.jfixture;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class JDBCHandler implements FixtureHandler {
    public static final String JDBC_CONNECT = "jdbc-connect";
    public static final String JDBC_CREATE_TABLE = "jdbc-create-table";
    public static final String JDBC_INSERT = "jdbc-insert";
    private Connection connection;
    private boolean autoCloseConnection = true;

    public JDBCHandler(Connection connection) {
        this.connection = connection;
        this.autoCloseConnection = false;
    }

    public JDBCHandler() {
    }

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey(JDBC_CONNECT) || fixtureInput.containsKey(JDBC_CREATE_TABLE) || fixtureInput.containsKey(JDBC_INSERT);
    }

    @Override
    public void process(Map<String, Object> fixtureInput) throws FixtureException {
        if (fixtureInput.containsKey(JDBC_CONNECT)) {
            processConnect(fixtureInput.get(JDBC_CONNECT));
        } else if (fixtureInput.containsKey(JDBC_CREATE_TABLE)) {
            processCreateTable(fixtureInput.get(JDBC_CREATE_TABLE));
        } else if (fixtureInput.containsKey(JDBC_INSERT)) {
            processInsert(fixtureInput.get(JDBC_INSERT));
        }
    }

    private void processConnect(Object input) throws FixtureException {
        if (input instanceof Map) {
            Map<String, Object> connectParams = (Map<String, Object>) input;

            try {
                Class.forName(parameter(connectParams, "driver"));
                connection = DriverManager.getConnection(parameter(connectParams, "url"), parameter(connectParams, "username", ""), parameter(connectParams, "password", ""));
                autoCloseConnection = true;
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
                throw new FixtureException("JDBCHandler: " + JDBC_CREATE_TABLE + ": The expected parameter rows is missing.");
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

                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    statement.execute(String.valueOf(buffer));
                } catch (SQLException e) {
                    throw new FixtureException("JDBCHandler: " + JDBC_CREATE_TABLE + ": Error executing create table: " + buffer + ": " + e.toString());
                } finally {
                   if (statement != null) {
                       try {
                           statement.close();
                       } catch (SQLException ignored) {

                       }
                   }

                }
            } else {
                throw new FixtureException("JDBCHandler: " + JDBC_CREATE_TABLE + ": Expects rows defined as a mapping.");
            }
        } else {
            throw new FixtureException("JDBCHandler: " + JDBC_CONNECT + ": Expects a mapping.");
        }
    }

    private void processInsert(Object input) throws FixtureException {
        if (input instanceof Map) {
            Map<String, Object> connectParams = (Map<String, Object>) input;

            Object rowsObject = connectParams.get("rows");

            if (rowsObject == null) {
                throw new FixtureException("JDBCHandler: " + JDBC_INSERT + ": The expected parameter rows is missing.");
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

                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        statement.execute(String.valueOf(buffer));
                    } catch (SQLException e) {
                        throw new FixtureException("JDBCHandler: " + JDBC_INSERT + ": Error executing statement: " + buffer + ": " + e.toString());
                    } finally {
                        if (statement != null) {
                            try {
                                statement.close();
                            } catch (SQLException ignored) {

                            }
                        }
                    }
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

    public static JDBCHandler create(Connection connection) {
        return new JDBCHandler(connection);
    }

    public boolean isConnected() {
        return connection != null;
    }

    protected Connection connection() {
        return this.connection;
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                throw new IOException(e);
            }
            connection = null;
        }
    }
}
