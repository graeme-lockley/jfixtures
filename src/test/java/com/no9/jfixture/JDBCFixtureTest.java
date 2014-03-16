package com.no9.jfixture;

import org.junit.Test;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.*;

public class JDBCFixtureTest extends HandlerTest {
    @Test
    public void should_be_able_to_connect_to_database() throws FixtureException, IOException {
        JDBCHandler handler = JDBCHandler.create();
        Map<String, Object> content = parseContent("jdbc-connect:\n" +
                "   driver: org.h2.Driver\n" +
                "   url: 'jdbc:h2:mem:'\n" +
                "   username: sa\n" +
                "   password: ");

        assertTrue(handler.canProcess(content));
        handler.process(content);

        assertTrue(handler.isConnected());

        handler.close();
    }

    @Test
    public void should_be_able_to_create_a_table() throws FixtureException, SQLException, IOException {
        JDBCHandler handler = JDBCHandler.create();

        Map<String, Object> connectContent = parseContent("jdbc-connect:\n" +
                "   driver: org.h2.Driver\n" +
                "   url: 'jdbc:h2:mem:'\n" +
                "   username: sa\n" +
                "   password:");
        handler.process(connectContent);

        Map<String, Object> createTableContent = parseContent("jdbc-create-table:\n" +
                "   name: people\n" +
                "   rows: {id: 'bigint not null primary key auto_increment', name: 'varchar(50)'}");
        assertTrue(handler.canProcess(createTableContent));
        handler.process(createTableContent);

        try (PreparedStatement preparedStatement = handler.connection().prepareStatement("select count(*) from people");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            assertEquals(0, resultSet.getInt(1));
        }

        handler.close();
    }

    @Test
    public void should_be_able_to_insert_rows_into_a_table() throws FixtureException, SQLException, IOException {
        JDBCHandler handler = JDBCHandler.create();

        Map<String, Object> connectContent = parseContent("jdbc-connect:\n" +
                "   driver: org.h2.Driver\n" +
                "   url: 'jdbc:h2:mem:'\n" +
                "   username: sa\n" +
                "   password:");
        handler.process(connectContent);

        Map<String, Object> createTableContent = parseContent("jdbc-create-table:\n" +
                "   name: people\n" +
                "   rows: {id: 'bigint not null primary key auto_increment', name: 'varchar(50)'}");
        handler.process(createTableContent);

        Map<String, Object> insertTableContent = parseContent("jdbc-insert:\n" +
                "   name: people\n" +
                "   rows: [{name: Graeme}, {name: Tim}]");
        assertTrue(handler.canProcess(insertTableContent));
        handler.process(insertTableContent);

        try (PreparedStatement preparedStatement = handler.connection().prepareStatement("select id, name from people order by name");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            assertEquals("Graeme", resultSet.getString(2));
            resultSet.next();
            assertEquals("Tim", resultSet.getString(2));
            assertFalse(resultSet.next());
        }

        handler.close();
    }
}
