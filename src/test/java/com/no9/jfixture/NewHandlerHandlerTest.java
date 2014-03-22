package com.no9.jfixture;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class NewHandlerHandlerTest {
    private Fixtures fixtures;

    @Test
    public void should_auto_register_the_handler_handler() throws IOException {
        fixtures = Fixtures.load(FixturesInput.fromResources("initial.yaml"));
        assertTrue(fixtures.findHandler(NewHandlerHandler.class).isPresent());
    }

    @Test
    public void should_confirm_that_a_handler_was_added_following_a_use_handler() throws IOException, FixtureException {
        fixtures = Fixtures.load(FixturesInput.fromString("- new-handler: com.no9.jfixture.EchoHandlerDummy"));

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isNotPresent());

        fixtures.processFixtures();

        assertTrue(fixtures.findHandler(EchoHandlerDummy.class).isPresent());
    }
}
