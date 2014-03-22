package com.no9.jfixture;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static com.no9.jfixture.HandlerTest.parseContent;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImportHandlerTest {
    private ImportHandler handler = new ImportHandler();

    @Test
    public void should_accept_selector() {
        Map<String, Object> importContent = parseContent("import: resource:initial.yaml");
        assertTrue(handler.canProcess(importContent));
    }

    @Test
    public void should_not_accept_selector() {
        Map<String, Object> importContent = parseContent("jdbc-connect:\n" +
                "   driver: org.h2.Driver\n" +
                "   url: 'jdbc:h2:mem:'\n" +
                "   username: sa\n" +
                "   password:");
        assertFalse(handler.canProcess(importContent));
    }

    @Test
    public void should_import_fixtures_from_a_constant_string() throws IOException, FixtureException {
        Fixtures fixtures = Fixtures.load(FixturesInput.fromString("- import: 'string:- new-handler: com.no9.jfixture.EchoHandlerDummy'"));

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isNotPresent());

        fixtures.processFixtures();

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isPresent());
    }

    @Test
    public void should_import_fixtures_from_a_resource_file() throws IOException, FixtureException {
        Fixtures fixtures = Fixtures.load(FixturesInput.fromString("- import: resource:includeTest.yaml"));

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isNotPresent());

        fixtures.processFixtures();

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isPresent());
    }
}
