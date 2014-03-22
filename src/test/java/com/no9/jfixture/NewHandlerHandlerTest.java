package com.no9.jfixture;

import org.junit.Test;

import java.io.IOException;

import static com.no9.jfixture.HandlerTest.hasHandler;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NewHandlerHandlerTest {
    private Fixtures fixtures;

    @Test
    public void should_auto_register_the_handler_handler() throws IOException {
        fixtures = Fixtures.load(FixturesInput.fromResources("initial.yaml"));
        assertTrue(hasHandler(fixtures, NewHandlerHandler.class));
    }

    @Test
    public void should_confirm_that_a_handler_was_added_following_a_use_handler() throws IOException, FixtureException {
        fixtures = Fixtures.load(FixturesInput.fromString("- new-handler: com.no9.jfixture.EchoHandlerDummy"));

        assertFalse(hasHandler(fixtures, EchoHandlerDummy.class));

        fixtures.processFixtures();

        assertTrue(hasHandler(fixtures, EchoHandlerDummy.class));
    }
}
