package com.no9.jfixture;

import org.junit.Test;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class FixturesTest {
    @Test(expected = com.no9.jfixture.FixtureException.class)
    public void should_fail_because_a_fixture_has_multiple_selectors() throws IOException, FixtureException {
        Fixtures fixtures = Fixtures.load(FixturesInput.fromString("- {objective: peace, place: world}"));
        fixtures.processFixtures();
    }

    @Test(expected = com.no9.jfixture.FixtureException.class)
    public void should_fail_because_the_fixtures_are_not_a_sequence() throws IOException, FixtureException {
        Fixtures fixtures = Fixtures.load(FixturesInput.fromString("objective: peace\nplace: world"));
        fixtures.processFixtures();
    }

    @Test(expected = com.no9.jfixture.FixtureException.class)
    public void should_be_unable_process_the_fixtures_as_the_handler_is_not_registered() throws IOException, FixtureException {
        Fixtures fixtures = Fixtures.load(FixturesInput.fromResources("initial.yaml"));
        fixtures.processFixtures();
    }

    @Test
    public void should_process_the_fixtures_with_the_registered_handler() throws IOException, FixtureException {
        EchoHandlerDummy echoHandlerDummy = EchoHandlerDummy.create();

        Fixtures fixtures = Fixtures.load(FixturesInput.fromResources("initial.yaml"), echoHandlerDummy);

        assertFalse(echoHandlerDummy.hasBeenClosed());
        fixtures.processFixtures();

        assertEquals(
                asList("Hello World", "Bye bye love", "Hello loneliness", "Redemption... starts with the acknowledgment"),
                echoHandlerDummy.messages());

        assertTrue(echoHandlerDummy.hasBeenClosed());
    }
}
