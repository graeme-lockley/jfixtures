package com.no9.jfixture;

import java.util.Map;

enum JDBCOperations {
    CONNECT(new JDBCConnect("jdbc-connect")),
    CREATE_TABLE(new JDBCCreateTable("jdbc-create-table")),
    INSERT(new JDBCInsert("jdbc-insert"));

    private JDBCOperation operation;

    JDBCOperations(JDBCOperation operation) {
        this.operation = operation;
    }

    public boolean canProcess(Map<String, Object> fixtureInput) {
        return operation.canProcess(fixtureInput);
    }

    public void process(JDBCHandler handler, Map<String, Object> fixtureInput) throws FixtureException {
        operation.process(handler, fixtureInput);
    }
}
