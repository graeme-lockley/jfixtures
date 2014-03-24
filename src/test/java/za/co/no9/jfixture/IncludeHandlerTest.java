package za.co.no9.jfixture;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static za.co.no9.jfixture.HandlerTest.parseContent;

public class IncludeHandlerTest {
    private IncludeHandler handler = new IncludeHandler();

    @Test
    public void should_accept_selector() {
        Map<String, Object> importContent = parseContent("include: resource:initial.yaml");
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
        Fixtures fixtures = Fixtures.load(FixturesInput.fromString("- include: 'string:- new-handler: za.co.no9.jfixture.EchoHandlerDummy'"));

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isNotPresent());

        fixtures.processFixtures();

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isPresent());
    }

    @Test
    public void should_import_fixtures_from_a_resource_file() throws IOException, FixtureException {
        Fixtures fixtures = Fixtures.load(FixturesInput.fromString("- include: resource:includeTest.yaml"));

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isNotPresent());

        fixtures.processFixtures();

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isPresent());
    }
}
